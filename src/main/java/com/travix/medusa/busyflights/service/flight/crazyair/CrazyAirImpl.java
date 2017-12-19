package com.travix.medusa.busyflights.service.flight.crazyair;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.flight.Supplier;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrazyAirImpl implements Supplier {


    private static Logger logger = LoggerFactory.getLogger(CrazyAirImpl.class);


    @Autowired
    Tracer tracer;

    @Autowired
    MapperFacade mapperFacade;

    @Autowired
    RestTemplate restTemplate;


    @Value("${crazy.air.endpoint}")
    private String endpoint;


    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        logger.debug("searchFlight method start", tracer.getCurrentSpan().getTraceId());
        CrazyAirRequest crazyAirRequest = mapperFacade.map(busyFlightsRequest, CrazyAirRequest.class);

        ResponseEntity<CrazyAirResponse[]> resp = restTemplate.postForEntity(endpoint, crazyAirRequest, CrazyAirResponse[].class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            logger.info("Could not get flights from Crazy Air");
            return Collections.emptyList();
        }
        CrazyAirResponse[] crazyAirResponses = resp.getBody();
        List<BusyFlightsResponse> response = Arrays.stream(crazyAirResponses).map(crazyAirResponse -> mapperFacade.map(crazyAirResponse, BusyFlightsResponse.class)).collect(Collectors.toList());
        logger.debug("searchFlight method finish", tracer.getCurrentSpan().getTraceId());
        return response;
    }
}
