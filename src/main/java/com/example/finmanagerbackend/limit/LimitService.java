package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LimitService {
    private final LimitRepository limitRepository;

    public LimitService( LimitRepository limitRepository ) {
        this.limitRepository = limitRepository;
    }

    @Transactional
    public void deleteLimit( Long limitId ) {
        Optional<Limit> optionalLimit = limitRepository.findById( limitId );
        if ( !optionalLimit.isPresent() ) return;
        if ( optionalLimit.get().getLimitType() == LimitType.ZERO ) {
            throw new LimitException( "Cannot delete the default limit." );
        }

        limitRepository.deleteById( limitId );
    }

    public List<Limit> getLimits() {
        return limitRepository.getAllLimitsWithoutZero();
    }

    public void addLimit( LimitDTO limitDTO ) {
        Limit limit = createLimit( limitDTO );
        if ( isLimitExists( limit ) ) {
            throw new IllegalStateException( "Taki limit już istneije!" );
        }

        limitRepository.save( limit );
    }

    public void updateLimit( Long limitId, Limit limit ) {
        // todo zabronić updatować limit ZERO
        Optional<Limit> optimalLimit = limitRepository.findById( limitId );
        if ( !optimalLimit.isPresent() ) {
            // todo: za dużo IllegalStateException!!!
            throw new IllegalStateException( "Limit z id " + limitId + " nie istnieje w bazie!" );
        }

        if ( isLimitExists( optimalLimit.get() ) ) {
            throw new IllegalStateException( "Taki limit już istneije!" );
        }

        limitRepository.save( limit );
    }


    // not DB using functions

    private boolean isLimitExists( Limit limitToCheck ) {
        return limitRepository.existsBy( limitToCheck.getLimitType() );
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
