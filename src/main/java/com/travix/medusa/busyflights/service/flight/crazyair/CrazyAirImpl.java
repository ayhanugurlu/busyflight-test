package com.travix.medusa.busyflights.service.flight.crazyair;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.flight.Flight;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrazyAirImpl implements Flight {




    @Autowired
    @Qualifier("busyFlightServiceMapper")
    MapperFacade mapperFacade;


    @Value("${crazy.air.endpoint}")
    private String endpoint;



    @Override
    public BusyFlightsResponse searchFlight(BusyFlightsRequest busyFlightsRequest) {

        CrazyAirRequest crazyAirRequest = mapperFacade.map(busyFlightsRequest, CrazyAirRequest.class);
        RestTemplate restTemplate = new RestTemplate();
        CrazyAirResponse crazyAirResponse = restTemplate.postForObject(endpoint,crazyAirRequest, CrazyAirResponse.class);
        BusyFlightsResponse busyFlightsResponse = mapperFacade.map(crazyAirResponse, BusyFlightsResponse.class);
        return busyFlightsResponse;
    }
}
