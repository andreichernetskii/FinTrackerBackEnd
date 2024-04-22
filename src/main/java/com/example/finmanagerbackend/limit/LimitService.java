package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.ForbiddenException;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import com.example.finmanagerbackend.security.application_user.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Limit entities.
 */
@Service
public class LimitService {
    private final LimitRepository limitRepository;
    private final AccountService accountService;

    public LimitService( LimitRepository limitRepository, AccountService accountService ) {
        this.limitRepository = limitRepository;
        this.accountService = accountService;
    }

    // Deletes a specific limit.
    @Transactional
    public ResponseEntity<?> deleteLimit( Long limitId ) {  // +
        Optional<Limit> optionalLimit = limitRepository.findById( limitId );

        if ( !optionalLimit.isPresent() ) {
            throw new NotFoundException( "This limit does not exist." );
        }

        if ( optionalLimit.get().getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot delete the default limit." );
        }

        limitRepository.deleteById( limitId );

        return ResponseEntity.ok(new MessageResponse( "Limit successfully deleted" ) );
    }

    // Retrieves all limits associated with the current account except for the ZERO type.
    public List<Limit> getLimits() {    // +
        Account account = accountService.getAccount();
        return limitRepository.getAllLimitsWithoutZero( account.getId() );
    }

    // Adds a new limit.
    public ResponseEntity<?> addLimit( LimitDTO limitDTO ) {    // +
        Account account = accountService.getAccount();

        Limit limit = createLimit( limitDTO );
        limit.setAccount( account );

        // if limit exists (amount or amount with category) will be thrown the exception
        if ( isLimitExists( account, limit ) ) {
            throw new UnprocessableEntityException( "Limit already exist!" );
        }

        limitRepository.save( limit );

        return ResponseEntity.ok(new MessageResponse( "Limit successfully added" ));
    }

    // Updates an existing limit.
    public ResponseEntity<?> updateLimit( Long limitId, Limit limit ) {
        Account account = accountService.getAccount();
        Optional<Limit> optimalLimit = limitRepository.findLimit( limitId, account.getId() );

        if ( !optimalLimit.isPresent() ) {
            throw new NotFoundException( "Limit with ID " + limitId + " not exist!" );
        }

        if ( optimalLimit.get().getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot delete the default limit." );
        }

        limit.setAccount( account );
        limitRepository.save( limit );

        return ResponseEntity.ok(new MessageResponse( "Limit successfully updated" ));
    }

    // not DB using functions

    // Checks if a limit of a specific type exists for a given account.
    private boolean isLimitExists( Account account, Limit limitToCheck ) {
        return limitRepository.existsBy( account.getId(), limitToCheck.getLimitType(), limitToCheck.getCategory() );
    }

    // Retrieves a list of available limit types.
    public List<String> getLimitTypes() {
        return Arrays.stream( LimitType.values() )
                .map( Enum::toString )
                .toList();
    }

    // Creates a new Limit entity based on the given DTO.
    private Limit createLimit( LimitDTO limitDTO ) {
        return new Limit(
                limitDTO.getLimitType(),
                limitDTO.getLimitAmount(),
                limitDTO.getCategory(),
                limitDTO.getCreationDate()
        );
    }
}
