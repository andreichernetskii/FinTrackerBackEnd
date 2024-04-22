package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.alert.analyser.FinAnalyser;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import com.example.finmanagerbackend.security.application_user.response.MessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class ) // Mockito for simulate objects job
public class LimitServiceTest {
    @InjectMocks
    private LimitService limitService;
    @Mock // Repo's job sim
    private LimitRepository limitRepository;
    @Mock
    private AccountService accountService;

    //region delete limit tests section
    @Test
    public void deleteLimitTest_SuccessfulDeletion() {
        Long limitId = 1L;
        Limit limit = mock( Limit.class );

        when( limit.getId() ).thenReturn( limitId );
        when( limitRepository.findById( limitId ) ).thenReturn( Optional.of( limit ) );

        ResponseEntity<?> responseEntity = limitService.deleteLimit( limitId );

        // assert
        verify( limitRepository, times( 1 ) ).deleteById( limitId );
        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );
        assertEquals( "Limit successfully deleted", ( ( MessageResponse ) responseEntity.getBody() ).getMessage() );
    }

    @Test
    public void deleteLimitTest_LimitNotFound() {
        Long limitId = 1L;

        when( limitRepository.findById( limitId ) ).thenReturn( Optional.empty() );

        assertThrows( NotFoundException.class, () -> limitService.deleteLimit( limitId ) );
        verify( limitRepository, never() ).deleteById( limitId );
    }
    //endregion
    //region get limit tests section
    @Test
    public void getLimitsTest_SuccessfulReturning() {
        Long accountId = 1L;
        Account account = mock( Account.class );

        when( account.getId() ).thenReturn( accountId );
        when( accountService.getAccount() ).thenReturn( account );

        List<Limit> limits = new ArrayList<>();
        limits.add( new Limit() );
        limits.add( new Limit() );

        when( limitRepository.getAllLimitsWithoutZero( accountId ) ).thenReturn( limits );

        List<Limit> result = limitService.getLimits();

        assertEquals( limits.size(), result.size() );
        verify( accountService, times( 1 ) ).getAccount();
        verify( limitRepository, times( 1 ) ).getAllLimitsWithoutZero( accountId );
    }
    //endregion
    //region add limit tests section
    @Test
    public void addLimitTest_SuccessfulAddedLimit() {
        Account account = mock( Account.class );
        when( accountService.getAccount() ).thenReturn( account );

        LimitDTO limitDTO = new LimitDTO(
                LimitType.DAY,
                new BigDecimal( 100 ),
                "Category",
                LocalDate.now()
        );

        ResponseEntity<?> responseEntity = limitService.addLimit( limitDTO );

        ArgumentCaptor<Limit> limitCaptor = ArgumentCaptor.forClass( Limit.class );
        // capturing saved object
        verify( limitRepository ).save( limitCaptor.capture() );
        // and creating a new limit with captured values
        Limit capturedLimit = limitCaptor.getValue();

        // asserting got and expected values
        assertEquals( limitDTO.getLimitAmount(), capturedLimit.getLimitAmount() );
        assertEquals( limitDTO.getLimitType(), capturedLimit.getLimitType() );
        assertEquals( limitDTO.getCategory(), capturedLimit.getCategory() );
        assertEquals( limitDTO.getCreationDate(), capturedLimit.getCreationDate() );

        assertEquals( HttpStatus.OK, responseEntity.getStatusCode() );

        verify( accountService, times( 1 ) ).getAccount();
        verify( limitRepository, times( 1 ) ).save( any( Limit.class ) );
    }

    @Test
    public void addLimitTest_LimitAlreadyExist() {
        Account account = mock( Account.class );
        when( accountService.getAccount() ).thenReturn( account );
        when( account.getId() ).thenReturn( 1L );

        Limit existsLimit = new Limit(
                LimitType.DAY,
                new BigDecimal( 100 ),
                "Category",
                LocalDate.now()
        );

        LimitDTO limitDto = new LimitDTO(
                LimitType.DAY,
                new BigDecimal( 100 ),
                "Category",
                LocalDate.now()
        );

        when( limitRepository.existsBy( account.getId(), existsLimit.getLimitType(), existsLimit.getCategory() ) )
                .thenReturn( true );

        assertThrows( UnprocessableEntityException.class, () -> limitService.addLimit( limitDto ) );
        verify( accountService, times( 1 ) ).getAccount();
        verify( limitRepository, never() ).save( any( Limit.class ) );
    }
    //endregion
}
