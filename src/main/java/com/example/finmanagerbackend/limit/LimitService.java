package com.example.finmanagerbackend.limit;

import com.example.finmanagerbackend.analyser.FinAnalyser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LimitService {
    private final LimitRepository limitRepository;
    private final FinAnalyser finAnalyser;

    public LimitService( LimitRepository limitRepository, FinAnalyser finAnalyser ) {
        this.limitRepository = limitRepository;
        this.finAnalyser = finAnalyser;
    }

//    public void addNewLimit( LimitDTO limitDTO ) {
//        limitRepository.save( new Limit(
//                limitDTO.getLimitAmount(),
//                limitDTO.getLimitType()
//        ) );
//        finAnalyser.updateLimits();
//    }

    @Transactional
    public void deleteLimit( Long limitId ) {
        boolean isLimitExists = limitRepository.existsById( limitId );
        if ( !isLimitExists )
            throw new IllegalStateException( "Limit with id " + limitId + " is not exists!" );
        limitRepository.deleteById( limitId );
        finAnalyser.updateLimits();
    }

    public List<Limit> getLimits() {
        return limitRepository.findAll();
    }

//    public void updateLimit( Limit limit ) {
//        Optional<Limit> limitOptional = limitRepository.findById( limit.getId() );
//        if ( !limitOptional.isPresent() )
//            throw new IllegalStateException( "Limit z id " + limit.getId() + " nie istnieje w bazie!" );
//        limitRepository.save( limit );
//        finAnalyser.updateLimits();
//    }

    @Transactional
    public void addOrUpdateLimit( LimitDTO limitDTO ) {
        Limit limit = createLimit( limitDTO );

        limitRepository.deleteByLimitType( limit.getLimitType() );
        limitRepository.save( limit );
        finAnalyser.updateLimits();
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
