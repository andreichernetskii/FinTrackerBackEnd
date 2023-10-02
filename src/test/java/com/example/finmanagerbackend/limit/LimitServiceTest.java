package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class ) // pozwala używać Mockito dla symulacji działania objektów
public class LimitServiceTest {
    @InjectMocks // wywołamy ten objekt
    private LimitService limitService;
    @Mock // dla symulacji działania repo
    private LimitRepository limitRepository;
    @Mock // symulacja działania finAnalyzer'a
    private FinAnalyser finAnalyser;

    // todo: przerobić testy zgodnie ze zmianami klasy LimitService
    @Test
    public void deleteLimitTest_SuccessfulDeletion() {
        LimitType id = LimitType.DAY;
        // podłaczamy mock
        when( limitRepository.existsById( id ) ).thenReturn( true );

        // robimy usunięcie
        limitService.deleteLimit( id );

        // sprawdzamy, czy metody były używane
        verify( limitRepository ).existsById( id );
        verify( limitRepository ).deleteById( id );

        // że już nie ma limita z takim ID
        when( limitRepository.existsById( id ) ).thenReturn( false );
        assertFalse( limitRepository.existsById( id ) );
    }

    @Test
    public void deleteLimitTest_LimitNotFound() {
        LimitType id = LimitType.DAY;
        // podłączamy mock, żeby on symulował brak istnienia limita
        when( limitRepository.existsById( id ) ).thenReturn( false );

        // sprawdzamy, czy wysakuje wyjątek po próbie usunięcia nieistniejącego limita
        assertThrows( IllegalStateException.class, () -> limitService.deleteLimit( id ) );

        // czy była metoda zawołana
        verify( limitRepository ).existsById( id );

        // czy nie było więcej niepotrzebnych wyławań
        verifyNoMoreInteractions( limitRepository );
    }

    @Test
    public void getLimitsTest_SuccessfulReturning() {
        // tworzymy fikcyjną listę limitów
        List<Limit> expectedLimits = new ArrayList<>();
        expectedLimits.add( new Limit( new BigDecimal( 100 ), LimitType.DAY ) );
        expectedLimits.add( new Limit( new BigDecimal( 23 ), LimitType.MONTH ) );

        // mock dla zwracania oczekiwanej listy limitów
        when( limitRepository.findAll() ).thenReturn( expectedLimits );

        // tworzymy aktualną listę limitów
        List<Limit> actualLimits = limitService.getLimits();

        // czy była wyłowana metoda
        verify( limitRepository ).findAll();

        // czy nie null
        assertNotNull( actualLimits );

        // czy listy są podobne
        assertEquals( expectedLimits, actualLimits );
    }


    @Test
    public void addLimitTest_SuccessfulAddedLimit() {
        // first, original limit adding to mock
        Limit oldLimit = new Limit( new BigDecimal( 100 ), LimitType.DAY );
        when( limitRepository.save( any() ) ).thenReturn( oldLimit );

        // preparing new limit for update operation
        LimitDTO newLimit = new LimitDTO(new BigDecimal( 120 ), LimitType.DAY);
        // and adding to mock for update
        limitService.addLimit( newLimit );

        // create object for capturing values
        ArgumentCaptor<Limit> limitCaptor = ArgumentCaptor.forClass( Limit.class );
        // capturing saved object
        verify( limitRepository ).save( limitCaptor.capture() );
        // and creating a new limit with captured values
        Limit capturedLimit = limitCaptor.getValue();

        // asserting got and expected values
        assertEquals( newLimit.getLimitAmount(), capturedLimit.getLimitAmount() );
        assertEquals( newLimit.getLimitType(), capturedLimit.getLimitType() );
    }
}
