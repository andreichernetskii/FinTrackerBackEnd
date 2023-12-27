package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.limit.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ApplicationRunner that initializes the database with a zero limit if it doesn't already exist.
 */
@Component
public class DataBaseInitializer implements ApplicationRunner {
    private LimitService limitService;
    private LimitRepository limitRepository;

    public DataBaseInitializer( LimitService limitService, LimitRepository limitRepository ) {
        this.limitService = limitService;
        this.limitRepository = limitRepository;
    }

    // Creates a zero limit with the current date.
    private LimitDTO createZeroLimit() {
        return new LimitDTO(
                LimitType.ZERO,
                new BigDecimal( 0 ),
                null,
                LocalDate.now()
        );
    }

    // Runs the initialization logic to add a zero limit if it doesn't already exist.
    @Override
    public void run( ApplicationArguments args ) throws Exception {
        LimitDTO limitDTO = createZeroLimit();
        if ( !isZeroLimExists( limitDTO ) ) limitService.addLimit( limitDTO );
    }

    // Checks if a zero limit already exists in the database.
    private boolean isZeroLimExists(LimitDTO limitDTO) {
        return limitRepository.existsBy( null, limitDTO.getLimitType() );
    }
}
