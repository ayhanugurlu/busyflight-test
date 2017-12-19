package com.travix.medusa.busyflights.service.flight.toughjet;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.flight.Supplier;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ToughJetImpl implements Supplier {

    private static Logger logger = LoggerFactory.getLogger(ToughJetImpl.class);

    @Autowired
    Tracer tracer;

    @Autowired
    @Qualifier("busyFlightServiceMapper")
    MapperFacade mapperFacade;

    @Autowired
    RestTemplate restTemplate;

    @Value("${tough.air.endpoint}")
    private String endpoint;


    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        logger.debug("searchFlight method start", tracer.getCurrentSpan().getTraceId());
        ToughJetRequest toughJetRequest = mapperFacade.map(busyFlightsRequest, ToughJetRequest.class);
        ResponseEntity<ToughJetResponse[]> resp = restTemplate.postForEntity(endpoint, toughJetRequest, ToughJetResponse[].class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            logger.info("Could not get flights from Tough Jet");
            return Collections.emptyList();
        }
        ToughJetResponse[] toughJetResponses = resp.getBody();

        List<BusyFlightsResponse> response = Arrays.stream(toughJetResponses).map(toughJetResponse -> mapperFacade.map(toughJetResponse, BusyFlightsResponse.class)).collect(Collectors.toList());
        logger.debug("searchFlight method finish", tracer.getCurrentSpan().getTraceId());
        return response;
    }
}
