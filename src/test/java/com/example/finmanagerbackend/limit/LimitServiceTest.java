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

        mockAccount = new Account();
        mockAccount.setId(1L);

        sampleLimit = new Limit(LimitType.MONTH, BigDecimal.valueOf(1000), "Groceries", LocalDate.now());
        sampleLimit.setId(1L);
        sampleLimit.setAccount(mockAccount);

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

        when(limitRepository.findById(1L)).thenReturn(Optional.of(sampleLimit));

        assertDoesNotThrow(() -> limitService.deleteLimit(1L));

        verify(limitRepository, times(1)).findById(1L);
        verify(limitRepository, times(1)).deleteById(1L);
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

        sampleLimit.setLimitType(LimitType.ZERO);
        when(limitRepository.findById(1L)).thenReturn(Optional.of(sampleLimit));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> limitService.deleteLimit(1L));
        assertEquals("Cannot delete the default limit.", exception.getMessage());

        verify(limitRepository, times(1)).findById(1L);
        verify(limitRepository, never()).deleteById(anyLong());
    }

    // --- getLimits ---
    @Test
    void getLimits_whenLimitsExist_shouldReturnListOfLimitDTOs() {

        Limit limit2 = new Limit(LimitType.WEEK, BigDecimal.valueOf(200), "Transport", LocalDate.now().minusDays(1));
        limit2.setId(2L);
        limit2.setAccount(mockAccount);

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

        Limit newLimitEntity = new Limit(newLimitDTO.getLimitType(), newLimitDTO.getLimitAmount(), newLimitDTO.getCategory(), newLimitDTO.getCreationDate());
        newLimitEntity.setAccount(mockAccount); // Account will be set by service

        Limit savedLimitEntity = new Limit(newLimitDTO.getLimitType(), newLimitDTO.getLimitAmount(), newLimitDTO.getCategory(), newLimitDTO.getCreationDate());
        savedLimitEntity.setId(2L); // Assume DB generates ID
        savedLimitEntity.setAccount(mockAccount);


        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.existsBy(mockAccount.getId(), newLimitDTO.getLimitType(), newLimitDTO.getCategory())).thenReturn(false);
        // Use ArgumentCaptor or any(Limit.class) and thenReturn to simulate save
        when(limitRepository.save(any(Limit.class))).thenAnswer(invocation -> {
            Limit limitToSave = invocation.getArgument(0);
            // Simulate saving by assigning an ID if not present, and return a new instance
            // to mimic what JPA might do. Here, we'll just return a copy with an ID.
            Limit saved = new Limit(limitToSave.getLimitType(), limitToSave.getLimitAmount(), limitToSave.getCategory(), limitToSave.getCreationDate());
            saved.setId(2L); // Simulate ID generation
            saved.setAccount(limitToSave.getAccount());
            return saved;
        });


        LimitDTO result = limitService.addLimit(newLimitDTO);

        assertNotNull(result);
        assertEquals(2L, result.getId()); // Check the generated ID
        assertEquals(newLimitDTO.getLimitType(), result.getLimitType());
        assertEquals(newLimitDTO.getLimitAmount(), result.getLimitAmount());
        assertEquals(newLimitDTO.getCategory(), result.getCategory());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).existsBy(mockAccount.getId(), newLimitDTO.getLimitType(), newLimitDTO.getCategory());
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
        Limit existingLimitInDb = new Limit(sampleLimit.getLimitType(), sampleLimit.getLimitAmount(), sampleLimit.getCategory(), sampleLimit.getCreationDate());
        existingLimitInDb.setId(sampleLimit.getId());
        existingLimitInDb.setAccount(mockAccount);


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

        sampleLimit.setLimitType(LimitType.ZERO); // This is the existing limit in DB
        when(accountService.getAccount()).thenReturn(mockAccount);
        when(limitRepository.findLimit(sampleLimit.getId(), mockAccount.getId())).thenReturn(Optional.of(sampleLimit));

        LimitDTO updateAttemptDTO = LimitDTO.builder() // DTO with changes
                .limitType(LimitType.MONTH)
                .limitAmount(BigDecimal.valueOf(100))
                .category("TryUpdate")
                .creationDate(LocalDate.now())
                .build();

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> limitService.updateLimit(sampleLimit.getId(), updateAttemptDTO));
        assertEquals("Cannot update the default limit.", exception.getMessage());

        verify(accountService, times(1)).getAccount();
        verify(limitRepository, times(1)).findLimit(sampleLimit.getId(), mockAccount.getId());
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
