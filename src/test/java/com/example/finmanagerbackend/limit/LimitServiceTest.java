package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.ForbiddenException;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith( MockitoExtension.class )
public class LimitServiceTest {

    @InjectMocks
    private LimitService limitService;

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private AccountService accountService;

    private Account mockAccount;
    private Limit sampleLimit;
    private LimitDTO sampleLimitDTO;

    @BeforeEach
    void setUp() {

        mockAccount = Account.builder()
                .id(1L)
                .isDemo(false)
                .build();

        sampleLimit = Limit.builder()
                .id(1L)
                .limitType(LimitType.MONTH)
                .limitAmount(BigDecimal.valueOf(1000))
                .category("Groceries")
                .creationDate(LocalDate.now())
                .account(mockAccount)
                .build();

        sampleLimitDTO = LimitDTO.builder()
                .id(1L)
                .limitType(LimitType.MONTH)
                .limitAmount(BigDecimal.valueOf(1000))
                .category("Groceries")
                .creationDate(LocalDate.now())
                .build();
    }

    @Test
    void deleteLimit_whenLimitExistsAndNotZeroType_shouldDeleteLimit() {

        when(limitRepository.findById(sampleLimit.getId())).thenReturn(Optional.of(sampleLimit));

        assertDoesNotThrow(() -> limitService.deleteLimit(sampleLimit.getId()));

        verify(limitRepository, times(1)).findById(sampleLimit.getId());
        verify(limitRepository, times(1)).deleteById(sampleLimit.getId());
    }

    @Test
    void deleteLimit_whenLimitNotFound_shouldThrowNotFoundException() {

        when(limitRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> limitService.deleteLimit(1L));
        assertEquals("Limit with ID: 1 does not exist in the database!", exception.getMessage());

        verify(limitRepository, times(1)).findById(1L);
        verify(limitRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteLimit_whenLimitTypeIsZero_shouldThrowForbiddenException() {

        Limit zeroTypeLimit = Limit.builder()
                .id(2L)
                .account(mockAccount)
                .limitType(LimitType.ZERO)
                .limitAmount(BigDecimal.valueOf(500))
                .category("Default")
                .creationDate(LocalDate.now())
                .build();

        when(limitRepository.findById(zeroTypeLimit.getId())).thenReturn(Optional.of(zeroTypeLimit));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> limitService.deleteLimit(zeroTypeLimit.getId()));
        assertEquals("Cannot delete the default limit.", exception.getMessage());

        verify(limitRepository, times(1)).findById(zeroTypeLimit.getId());
        verify(limitRepository, never()).deleteById(anyLong());
    }

    // --- getLimits ---
    @Test
    void getLimits_whenLimitsExist_shouldReturnListOfLimitDTOs() {

        Limit limit2 = Limit.builder()
                .id(2L)
                .account(mockAccount)
                .limitType(LimitType.WEEK)
                .limitAmount(BigDecimal.valueOf(200))
                .category("Transport")
                .creationDate(LocalDate.now().minusDays(1))
                .build();

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.getAllLimitsWithoutZero(mockAccount.getId())).thenReturn(Arrays.asList(sampleLimit, limit2));

        List<LimitDTO> result = limitService.getLimits();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(sampleLimit.getId(), result.get(0).getId());
        assertEquals(sampleLimit.getCategory(), result.get(0).getCategory());
        assertEquals(limit2.getId(), result.get(1).getId());
        assertEquals(limit2.getCategory(), result.get(1).getCategory());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).getAllLimitsWithoutZero(mockAccount.getId());
    }

    @Test
    void getLimits_whenNoLimitsExist_shouldReturnEmptyList() {

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.getAllLimitsWithoutZero(mockAccount.getId())).thenReturn(Collections.emptyList());

        List<LimitDTO> result = limitService.getLimits();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).getAllLimitsWithoutZero(mockAccount.getId());
    }

    // --- addLimit ---
    @Test
    void addLimit_whenLimitDoesNotExist_shouldSaveAndReturnLimitDTO() {

        LimitDTO newLimitDTO = LimitDTO.builder()
                .limitType(LimitType.YEAR)
                .limitAmount(BigDecimal.valueOf(5000))
                .category("Vacation")
                .creationDate(LocalDate.now())
                .build();

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.existsBy(mockAccount.getId(), newLimitDTO.getLimitType(), newLimitDTO.getCategory())).thenReturn(false);

        long generatedId = 3L;
        when(limitRepository.save(any(Limit.class))).thenAnswer(invocation -> {

            Limit limitToSave = invocation.getArgument(0);

            return Limit.builder()
                    .id(generatedId)
                    .account(limitToSave.getAccount())
                    .limitType(limitToSave.getLimitType())
                    .limitAmount(limitToSave.getLimitAmount())
                    .category(limitToSave.getCategory())
                    .creationDate(limitToSave.getCreationDate())
                    .build();
        });


        LimitDTO result = limitService.addLimit(newLimitDTO);

        assertNotNull(result);
        assertEquals(generatedId, result.getId());
        assertEquals(newLimitDTO.getLimitType(), result.getLimitType());
        assertEquals(newLimitDTO.getLimitAmount(), result.getLimitAmount());
        assertEquals(newLimitDTO.getCategory(), result.getCategory());
        assertEquals(newLimitDTO.getCreationDate(), result.getCreationDate());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).existsBy(mockAccount.getId(), newLimitDTO.getLimitType(), newLimitDTO.getCategory());
        verify(limitRepository, times(1)).save(argThat(limit ->
            limit.getId() == null &&    // before saving ID must be null
                    limit.getAccount().equals(mockAccount) &&
                    limit.getLimitType().equals(newLimitDTO.getLimitType()) &&
                    limit.getLimitAmount().compareTo(newLimitDTO.getLimitAmount()) == 0 &&
                    limit.getCategory().equals(newLimitDTO.getCategory()) &&
                    limit.getCreationDate().equals(newLimitDTO.getCreationDate())
        ));
        verify(limitRepository, times(1)).save(any(Limit.class));
    }

    @Test
    void addLimit_whenLimitAlreadyExists_shouldThrowUnprocessableEntityException() {

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.existsBy(mockAccount.getId(), sampleLimitDTO.getLimitType(), sampleLimitDTO.getCategory())).thenReturn(true);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> limitService.addLimit(sampleLimitDTO));
        assertEquals("Limit already exist!", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).existsBy(mockAccount.getId(), sampleLimitDTO.getLimitType(), sampleLimitDTO.getCategory());
        verify(limitRepository, never()).save(any(Limit.class));
    }

    // --- updateLimit ---
    @Test
    void updateLimit_whenLimitExistsAndNotZeroType_shouldUpdateAndReturnLimitDTO() {

        LimitDTO updatedInfoDTO = LimitDTO.builder()
                .limitType(LimitType.WEEK)
                .limitAmount(BigDecimal.valueOf(1500))
                .category("Entertainment")
                .creationDate(LocalDate.now().minusDays(5))
                .build();

        // Mock the existing limit found in DB
        Limit existingLimitInDb = Limit.builder()
                .id(sampleLimit.getId())
                .account(sampleLimit.getAccount())
                .limitType(sampleLimit.getLimitType())
                .limitAmount(sampleLimit.getLimitAmount())
                .category(sampleLimit.getCategory())
                .creationDate(sampleLimit.getCreationDate())
                .build();


        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.findLimit(sampleLimit.getId(), mockAccount.getId())).thenReturn(Optional.of(existingLimitInDb));
        when(limitRepository.save(any(Limit.class))).thenAnswer(invocation -> invocation.getArgument(0)); // return the same object passed to save

        LimitDTO result = limitService.updateLimit(sampleLimit.getId(), updatedInfoDTO);

        assertNotNull(result);
        assertEquals(sampleLimit.getId(), result.getId());
        assertEquals(updatedInfoDTO.getLimitType(), result.getLimitType());
        assertEquals(updatedInfoDTO.getLimitAmount(), result.getLimitAmount());
        assertEquals(updatedInfoDTO.getCategory(), result.getCategory());
        assertEquals(updatedInfoDTO.getCreationDate(), result.getCreationDate());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).findLimit(sampleLimit.getId(), mockAccount.getId());
        verify(limitRepository, times(1)).save(argThat(savedLimit ->
                savedLimit.getId().equals(sampleLimit.getId()) &&
                        savedLimit.getLimitType().equals(updatedInfoDTO.getLimitType()) &&
                        savedLimit.getLimitAmount().compareTo(updatedInfoDTO.getLimitAmount()) == 0 &&
                        savedLimit.getCategory().equals(updatedInfoDTO.getCategory()) &&
                        savedLimit.getCreationDate().equals(updatedInfoDTO.getCreationDate())
        ));
    }

    @Test
    void updateLimit_whenLimitNotFound_shouldThrowNotFoundException() {

        Long nonExistentLimitId = 99L;
        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.findLimit(nonExistentLimitId, mockAccount.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> limitService.updateLimit(nonExistentLimitId, sampleLimitDTO));
        assertEquals("Limit with ID: " + nonExistentLimitId + " does not exist in the database!", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).findLimit(nonExistentLimitId, mockAccount.getId());
        verify(limitRepository, never()).save(any(Limit.class));
    }

    @Test
    void updateLimit_whenLimitTypeIsZero_shouldThrowForbiddenException() {

        Limit zeroTypeLimitInDb = Limit.builder()
                .id(2L)
                .account(mockAccount)
                .limitType(LimitType.ZERO) // Ключевой момент
                .limitAmount(BigDecimal.valueOf(100))
                .category("Default Zero")
                .creationDate(LocalDate.now())
                .build();

        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.findLimit(zeroTypeLimitInDb.getId(), mockAccount.getId())).thenReturn(Optional.of(zeroTypeLimitInDb));

        LimitDTO updateAttemptDTO = LimitDTO.builder() // DTO with changes
                .limitType(LimitType.MONTH)
                .limitAmount(BigDecimal.valueOf(100))
                .category("TryUpdate")
                .creationDate(LocalDate.now())
                .build();

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> limitService.updateLimit(zeroTypeLimitInDb.getId(), updateAttemptDTO));
        assertEquals("Cannot update the default limit.", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).findLimit(zeroTypeLimitInDb.getId(), mockAccount.getId());
        verify(limitRepository, never()).save(any(Limit.class));
    }

    // --- getLimitTypes ---
    @Test
    void getLimitTypes_shouldReturnAllLimitTypeNames() {

        List<String> expectedTypes = Arrays.stream(LimitType.values())
                .map(Enum::toString)
                .toList();

        List<String> actualTypes = limitService.getLimitTypes();

        assertNotNull(actualTypes);
        assertEquals(expectedTypes.size(), actualTypes.size());
        assertTrue(actualTypes.containsAll(expectedTypes));
        assertTrue(expectedTypes.containsAll(actualTypes)); // Ensure exact match
    }
}
