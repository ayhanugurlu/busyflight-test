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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToughJetImpl implements Flight {


    @Autowired
    @Qualifier("busyFlightServiceMapper")
    MapperFacade mapperFacade;



    @Value("${tough.air.endpoint}")
    private String endpoint;


    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        ToughJetRequest toughJetRequest = mapperFacade.map(busyFlightsRequest, ToughJetRequest.class);
        RestTemplate restTemplate = new RestTemplate();
        ToughJetResponse[] toughJetResponses = restTemplate.postForObject(endpoint,toughJetRequest, ToughJetResponse[].class);
        List<BusyFlightsResponse> response = Arrays.stream(toughJetResponses).map(toughJetResponse -> mapperFacade.map(toughJetResponse, BusyFlightsResponse.class)).collect(Collectors.toList());
        return response;
    }
}
