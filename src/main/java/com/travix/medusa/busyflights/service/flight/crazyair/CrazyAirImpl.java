package com.travix.medusa.busyflights.service.flight.crazyair;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.flight.Supplier;
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
public class CrazyAirImpl implements Supplier {




    @Autowired
    @Qualifier("busyFlightServiceMapper")
    MapperFacade mapperFacade;


    @Value("${crazy.air.endpoint}")
    private String endpoint;



    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {

        CrazyAirRequest crazyAirRequest = mapperFacade.map(busyFlightsRequest, CrazyAirRequest.class);
        RestTemplate restTemplate = new RestTemplate();
        CrazyAirResponse[] crazyAirResponses = restTemplate.postForObject(endpoint,crazyAirRequest, CrazyAirResponse[].class);
        List<BusyFlightsResponse> response = Arrays.stream(crazyAirResponses).map(crazyAirResponse -> mapperFacade.map(crazyAirResponse, BusyFlightsResponse.class)).collect(Collectors.toList());
        return response;
    }
}
