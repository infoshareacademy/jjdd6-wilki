package com.infoshareacademy.jjdd6.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.time.LocalDateTime;

@Startup
@Singleton
public class TickerLoader {

    private static Logger logger = LoggerFactory.getLogger(DownloaderService.class);


    @Inject
    TickerService tickerService;

    @PostConstruct
    private void tickerInit(){
        tickerService.checkDataInDatabase();
        logger.info(String.valueOf(LocalDateTime.now()));
    }
}
