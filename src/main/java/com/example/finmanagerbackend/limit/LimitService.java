package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LimitService {
    private final ILimitRepository limitRepository;
    private FinAnalyser finAnalyser;

    public LimitService( ILimitRepository limitRepository, FinAnalyser finAnalyser ) {
        this.limitRepository = limitRepository;
        this.finAnalyser = finAnalyser;
    }

    public void addNewLimit( LimitDTO limitDTO ) {
        limitRepository.save( new Limit(
                limitDTO.getLimitAmount(),
                limitDTO.getLimitType()
        ) );
        finAnalyser.updateLimits();
    }

    public void deleteLimit( Long limitId ) {
        boolean isLimitExists = limitRepository.existsById( limitId );
        if ( !isLimitExists ) throw new IllegalStateException( "Limit with id " + limitId + " is not exists!" );
        limitRepository.deleteById( limitId );
        finAnalyser.updateLimits();
    }

    public List<Limit> getLimits() {
        return limitRepository.findAll();
    }

    public void updateLimit( Limit limit ) {
        Optional<Limit> limitOptional = limitRepository.findById( limit.getId() );
        if ( !limitOptional.isPresent() ) throw new IllegalStateException( "Limit z id " + limit.getId() + " nie istnieje w bazie!" );
        limitRepository.save( limit );
        finAnalyser.updateLimits();
    }
}