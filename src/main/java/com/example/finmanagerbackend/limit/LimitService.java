package com.example.finmanagerbackend.limit;

import org.springframework.stereotype.Service;

@Service
public class LimitService {
    ILimitRepository limitRepository;

    public LimitService( ILimitRepository limitRepository ) {
        this.limitRepository = limitRepository;
    }
}
