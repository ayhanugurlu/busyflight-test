package com.au.example.busyflights.controller;


import com.au.example.busyflights.domain.busyflights.BusyFlightsRequest;
import com.au.example.busyflights.domain.busyflights.BusyFlightsResponse;
import com.au.example.busyflights.exception.ValidationException;
import com.au.example.busyflights.service.busyflights.BusyFlights;
import com.au.example.busyflights.validation.validator.BusyFlightRequestValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/busyflight")
public class BusyFlightsController {

    private static Logger logger = LoggerFactory.getLogger(BusyFlightsController.class);

    @Autowired
    private BusyFlights busyFlights;

    @Autowired
    private Tracer tracer;

    @ApiOperation(value = "search flight",
            notes = "Returns registered flight and return all registered company flight information<br/>")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<List<BusyFlightsResponse>> searchFlight(@ApiParam(value = "Search flight to busyflight")@Validated(BusyFlightRequestValidator.class)@RequestBody BusyFlightsRequest busyFlightsRequest) throws ValidationException{
        logger.debug("searchFlight method start", tracer.getCurrentSpan().getTraceId());
        List<BusyFlightsResponse> resp = busyFlights.searchFlight(busyFlightsRequest);
        logger.debug("searchFlight method complete with message", tracer.getCurrentSpan().getTraceId());
        return ResponseEntity.status(HttpStatus.OK).body(resp);

    }


}
