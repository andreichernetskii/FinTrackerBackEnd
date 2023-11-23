package com.example.finmanagerbackend.global;

import com.example.finmanagerbackend.limit.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DataBaseInitializer implements ApplicationRunner {
    private LimitService limitService;
    private LimitRepository limitRepository;

    public DataBaseInitializer( LimitService limitService, LimitRepository limitRepository ) {
        this.limitService = limitService;
        this.limitRepository = limitRepository;
    }

    private LimitDTO createZeroLimit() {
        return new LimitDTO(
                LimitType.ZERO,
                new BigDecimal( 0 ),
                null,
                LocalDate.now()
        );
    }

    @Override
    public void run( ApplicationArguments args ) throws Exception {
        LimitDTO limitDTO = createZeroLimit();
        if ( !isZeroLimExists( limitDTO ) ) limitService.addLimit( limitDTO );
    }

    private boolean isZeroLimExists(LimitDTO limitDTO) {
        return limitRepository.existsBy( limitDTO.getLimitType() );
    }
}
