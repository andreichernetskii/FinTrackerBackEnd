package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.account.Account;
import com.example.finmanagerbackend.account.AccountService;
import com.example.finmanagerbackend.global.exceptions.ForbiddenException;
import com.example.finmanagerbackend.global.exceptions.NotFoundException;
import com.example.finmanagerbackend.global.exceptions.UnprocessableEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LimitService {
    private final LimitRepository limitRepository;
    private final AccountService accountService;

    public LimitService( LimitRepository limitRepository, AccountService accountService ) {
        this.limitRepository = limitRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void deleteLimit( Long limitId ) {
        Account account = accountService.getAccount();
        Optional<Limit> optionalLimit = limitRepository.findById( limitId );

        if ( !optionalLimit.isPresent() ) return;
        if ( optionalLimit.get().getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot delete the default limit." );
        }

        limitRepository.deleteById( limitId );
    }

    public List<Limit> getLimits() {
        Account account = accountService.getAccount();

        return limitRepository.getAllLimitsWithoutZero( account.getId() );
    }

    public void addLimit( LimitDTO limitDTO ) {
        Account account = accountService.getAccount();

        Limit limit = createLimit( limitDTO );

        if ( isLimitExists( account, limit ) ) {
            throw new UnprocessableEntityException( "Taki limit już istnieje!" );
        }

        limit.setAccount( account );
        limitRepository.save( limit );
    }

    public void updateLimit( Long limitId, Limit limit ) {
        Account account = accountService.getAccount();
        Optional<Limit> optimalLimit = limitRepository.findLimit( limitId, account.getId() );

        if ( !optimalLimit.isPresent() ) {
            throw new NotFoundException( "Limit z id " + limitId + " nie istnieje w bazie!" );
        }

        if ( optimalLimit.get().getLimitType() == LimitType.ZERO ) {
            throw new ForbiddenException( "Cannot delete the default limit." );
        }

        if ( isLimitExists( account, optimalLimit.get() ) ) {
            throw new UnprocessableEntityException( "Taki limit już istnieje!" );
        }

        limit.setAccount( account );
        limitRepository.save( limit );
    }

    // not DB using functions

    private boolean isLimitExists( Account account, Limit limitToCheck ) {
        return limitRepository.existsBy( account.getId(), limitToCheck.getLimitType() );
    }

    public List<String> getLimitTypes() {
        List<String> list = new ArrayList<>();

        for ( LimitType limType : LimitType.values() ) {
            list.add( limType.toString() );
        }

        return list;
    }

    private Limit createLimit( LimitDTO limitDTO ) {
        return new Limit(
                limitDTO.getLimitType(),
                limitDTO.getLimitAmount(),
                limitDTO.getCategory(),
                limitDTO.getCreationDate()
        );
    }
}
