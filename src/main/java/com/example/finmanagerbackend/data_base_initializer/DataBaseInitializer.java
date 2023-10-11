package com.example.finmanagerbackend.data_base_initializer;

import com.example.finmanagerbackend.limit.LimitDTO;
import com.example.finmanagerbackend.limit.LimitRepository;
import com.example.finmanagerbackend.limit.LimitService;
import com.example.finmanagerbackend.limit.LimitType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
                new BigDecimal( 0 )
        );
    }

    @Override
    public void run( ApplicationArguments args ) throws Exception {
        if ( !limitRepository.existsById( LimitType.ZERO ) ) limitService.addLimit( createZeroLimit() );
    }
}