package com.travix.medusa.busyflights.service.flight.toughjet;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;


import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.flight.Flight;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ToughJetImpl implements Flight {


    @Autowired
    @Qualifier("busyFlightServiceMapper")
    MapperFacade mapperFacade;



    @Value("${tough.air.endpoint}")
    private String endpoint;


    @Override
    public BusyFlightsResponse searchFlight(BusyFlightsRequest busyFlightsRequest) {
        ToughJetRequest toughJetRequest = mapperFacade.map(busyFlightsRequest, ToughJetRequest.class);
        RestTemplate restTemplate = new RestTemplate();
        ToughJetResponse toughJetResponse = restTemplate.postForObject(endpoint,toughJetRequest, ToughJetResponse.class);
        BusyFlightsResponse busyFlightsResponse = mapperFacade.map(toughJetResponse, BusyFlightsResponse.class);
        return busyFlightsResponse;
    }
}
