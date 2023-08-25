package com.example.finmanagerbackend.limit;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LimitService {
    ILimitRepository limitRepository;

    public LimitService( ILimitRepository limitRepository ) {
        this.limitRepository = limitRepository;
    }

    public void addNewLimit( LimitDTO limitDTO ) {
        limitRepository.save( new Limit(
                limitDTO.getAmountLimit(),
                limitDTO.getLimitType()
        ) );
    }

    public void deleteLimit( Long limitId ) {
        boolean isLimitExists = limitRepository.existsById( limitId );
        if ( !isLimitExists ) throw new IllegalStateException( "Limit with id " + limitId + " is not exists!" );
        limitRepository.deleteById( limitId );
    }

    public List<Limit> getLimits() {
        return limitRepository.findAll();
    }

    public void updateLimit( Limit limit ) {
        Optional<Limit> limitOptional = limitRepository.findById( limit.getId() );
        if ( !limitOptional.isPresent() ) throw new IllegalStateException( "Limit z id " + limit.getId() + " nie istnieje w bazie!" );
        limitRepository.save( limit );
    }
}
