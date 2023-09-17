package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import com.example.finmanagerbackend.limit.Limit;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitService;
import com.example.finmanagerbackend.limit.LimitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    public void deleteLimitTest_SuccessfulDeletion() {
        // podłaczamy mock
        when( limitRepository.existsById( anyLong() ) ).thenReturn( true );

        // robimy usunięcie
        limitService.deleteLimit( 1L );

        // sprawdzamy, czy metody były używane
        verify( limitRepository ).existsById( 1L );
        verify( limitRepository ).deleteById( 1L );
        verify( finAnalyser ).updateLimits();

        // że już nie ma limita z takim ID
        when( limitRepository.existsById( 1L ) ).thenReturn( false );
        assertFalse( limitRepository.existsById( 1L ) );
    }

    @Test
    public void deleteLimitTest_LimitNotFound() {
        // podłączamy mock, żeby on symulował brak istnienia limita
        when( limitRepository.existsById( anyLong() ) ).thenReturn( false );

        // sprawdzamy, czy wysakuje wyjątek po próbie usunięcia nieistniejącego limita
        assertThrows( IllegalStateException.class, () -> limitService.deleteLimit( 1L ) );

        // czy była metoda zawołana
        verify( limitRepository ).existsById( 1L );

        // czy nie było więcej niepotrzebnych wyławań
        verifyNoMoreInteractions( limitRepository, finAnalyser );
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
    public void addOrUpdateLimit_SuccessfulUpdatingLimit() {
        Limit oldLimit = new Limit( new BigDecimal( 100 ), LimitType.DAY );
        when( limitRepository.save( any() ) ).thenReturn( oldLimit );

        Limit newLimit = new Limit( new BigDecimal( 120 ), LimitType.DAY );

    }
}
