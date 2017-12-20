package com.au.example.busyflights.configuration;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class BusyFlightsConfigTest {



    @Test
    public void getRestTemplate() throws Exception {
        BusyFlightsConfig busyFlightsConfig = new BusyFlightsConfig();
        RestTemplate restTemplate = busyFlightsConfig.getRestTemplate();
        assertNotNull(restTemplate);
    }

}