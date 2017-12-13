package com.travix.medusa.busyflights.controller;


import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.busyflights.BusyFlights;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/busyflight", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class BusyFlightsController {

    private static Logger logger = LoggerFactory.getLogger(BusyFlightsController.class);

    @Autowired
    BusyFlights busyFlights;

    @Autowired
    Tracer tracer;

    @ApiOperation(value = "search flight",
            notes = "Returns registered flight and return all registered company flight information<br/>")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public
    @ResponseBody
    List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        logger.debug("searchFlight method start", tracer.getCurrentSpan().getTraceId());
        List<BusyFlightsResponse> resp = busyFlights.searchFlight(busyFlightsRequest);
        logger.debug("searchFlight method complete with message", tracer.getCurrentSpan().getTraceId());
        return resp;
    }


}
