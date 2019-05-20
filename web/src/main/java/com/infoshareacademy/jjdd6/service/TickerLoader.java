package com.infoshareacademy.jjdd6.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class TickerLoader {

    @Inject
    TickerService tickerService;

    @PostConstruct
    private void tickerInit(){
        tickerService.checkDataInDatabase();
    }
}
