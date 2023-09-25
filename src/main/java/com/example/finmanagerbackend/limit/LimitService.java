package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LimitService {
    private final LimitRepository limitRepository;
    private final FinAnalyser finAnalyser;

    public LimitService( LimitRepository limitRepository, FinAnalyser finAnalyser ) {
        this.limitRepository = limitRepository;
        this.finAnalyser = finAnalyser;
    }

    @Transactional
    public void deleteLimit( Long limitId ) {
        boolean isLimitExists = limitRepository.existsById( limitId );
        if ( !isLimitExists )
            throw new IllegalStateException( "Limit with id " + limitId + " is not exists!" );
        limitRepository.deleteById( limitId );
//        finAnalyser.updateLimits();
    }

    public List<Limit> getLimits() {
        return limitRepository.findAll();
    }

    public void addLimit( LimitDTO limitDTO ) {
        Limit limit = createLimit( limitDTO );
        limitRepository.save( limit );
    }

    public void updateLimit( Long limitId, Limit limit ) {
        Optional<Limit> optimalLimit = limitRepository.findById( limitId );
        if ( !optimalLimit.isPresent() ) {
            throw new IllegalStateException( "Limit z id " + limitId + " nie istnieje w bazie!" );
        }
        limitRepository.save( limit );
    }


    // not DB using functions

    public List<String> getLimitTypes() {
        List<String> list = new ArrayList<>();
        for ( LimitType limType : LimitType.values() ) {
            list.add( limType.toString() );
        }
        return list;
    }

    private Limit createLimit( LimitDTO limitDTO ) {
        return new Limit(
                limitDTO.getLimitAmount(),
                limitDTO.getLimitType()
        );
    }
}
