package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.annotations.SendLimits;
import com.example.finmanagerbackend.global.exceptions.ForbiddenException;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Service class for managing Limit entities.
 */
@RequiredArgsConstructor
@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final AccountService accountService;

    @Transactional
    @SendLimits
    public void deleteLimit( Long limitId ) {

        Limit existingLimit = limitRepository
                .findById( limitId )
                .orElseThrow(
                        () -> new NotFoundException( "Limit with ID: " + limitId + " does not exist in the database!" )
                );

        if ( existingLimit.getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot delete the default limit." );
        }

        limitRepository.deleteById( limitId );
    }

    public List<LimitDTO> getLimits() {

        List<Limit> listEntity =  limitRepository.getAllLimitsWithoutZero( accountService.getAccount().getId() );

        return listEntity.stream()
                .map(entity -> LimitDTO.builder()
                        .id(entity.getId())
                        .creationDate(entity.getCreationDate())
                        .category(entity.getCategory())
                        .limitType(entity.getLimitType())       
                        .limitAmount(entity.getLimitAmount())
                        .build())
                .toList();
    }

    @SendLimits
    public LimitDTO addLimit( LimitDTO limitDTO ) {

        Account account = accountService.getAccount();

        Limit limit = createLimit( limitDTO, account );

        if ( isLimitExists( account, limit ) ) {
            throw new UnprocessableEntityException( "Limit already exist!" );
        }

        Limit savedLimit = limitRepository.save( limit );

        return LimitDTO.builder()
                .id(savedLimit.getId())
                .limitAmount(savedLimit.getLimitAmount())
                .category(savedLimit.getCategory())
                .limitType(savedLimit.getLimitType())
                .creationDate(savedLimit.getCreationDate())
                .build();
    }

    @SendLimits
    public LimitDTO updateLimit( Long limitId, LimitDTO limitDTO ) {

        Account account = accountService.getAccount();
        Limit existingLimit = limitRepository
                .findLimit( limitId, account.getId() )
                .orElseThrow(
                        () -> new NotFoundException( "Limit with ID: " + limitId + " does not exist in the database!" )
                );

        if ( existingLimit.getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot update the default limit." );
        }

        existingLimit.updateFromDTO(limitDTO);

        Limit savedLimit = limitRepository.save( existingLimit );

        return LimitDTO.builder()
                .id(savedLimit.getId())
                .limitAmount(savedLimit.getLimitAmount())
                .category(savedLimit.getCategory())
                .limitType(savedLimit.getLimitType())
                .creationDate(savedLimit.getCreationDate())
                .build();
    }

    // not DB using functions

    private boolean isLimitExists( Account account, Limit limitToCheck ) {

        return limitRepository.existsBy( account.getId(), limitToCheck.getLimitType(), limitToCheck.getCategory() );
    }

    public List<String> getLimitTypes() {

        return Arrays.stream( LimitType.values() )
                .map( Enum::toString )
                .toList();
    }

    private Limit createLimit( LimitDTO limitDTO, Account account ) {

        return Limit.builder()
                        .account(account)
                        .limitType(limitDTO.getLimitType())
                        .category(limitDTO.getCategory())
                        .creationDate(limitDTO.getCreationDate())
                        .limitAmount(limitDTO.getLimitAmount())
                        .build();
    }
}
