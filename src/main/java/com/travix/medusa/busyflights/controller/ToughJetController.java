package com.travix.medusa.busyflights.controller;


import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for mock response generation
 */
@RestController
@RequestMapping(value = "/toughJet")
public class ToughJetController {

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<ToughJetResponse[]> search(@Valid @RequestBody ToughJetRequest request) {

        final ToughJetResponse resp1 =
                ToughJetResponse.builder()
                        .arrivalAirportName(request.getTo())
                        .carrier("KLM")
                        .departureAirportName(request.getFrom())
                        .basePrice(100D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(request.getInboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(01, 59).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(request.getOutboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(21, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();

        final ToughJetResponse resp2 =
                ToughJetResponse.builder()
                        .arrivalAirportName(request.getTo())
                        .carrier("THY")
                        .departureAirportName(request.getTo())
                        .arrivalAirportName(request.getFrom())
                        .basePrice(125D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(request.getInboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(07, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(request.getOutboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(23, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();

        return new ResponseEntity<>(new ToughJetResponse[]{resp1, resp2}, HttpStatus.OK);
    }
}