package com.travix.medusa.busyflights.crazyair;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.flight.crazyair.CrazyAirImpl;
import jdk.internal.instrumentation.Tracer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RunWith(SpringRunner.class)
@RestClientTest(CrazyAirImpl.class)
public class CrazyAirImplTest {


    @Autowired
    Tracer tracer;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private CrazyAirImpl crazyAirClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {

        CrazyAirResponse crazyAirResponse1 = CrazyAirResponse.builder().
                airline("thy").
                arrivalDate("2018-12-03T15:15:30").
                cabinclass("E").
                departureAirportCode("LHR").
                departureDate("2018-12-03T10:15:30").
                destinationAirportCode("ALM").
                price(12.3434).
                build();

        CrazyAirResponse crazyAirResponse2 = CrazyAirResponse.builder().
                airline("KLM").
                arrivalDate("2018-11-03T15:15:30").
                cabinclass("B").
                departureAirportCode("LHR").
                departureDate("2018-11-03T10:15:30").
                destinationAirportCode("ALM").
                price(15).
                build();

        CrazyAirResponse[] crazyAirResponses = {crazyAirResponse1, crazyAirResponse2};
        String detailsString =
                objectMapper.writeValueAsString(crazyAirResponses);

        this.server.expect(requestTo("/crazyair"))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }

    @Test
    public void searchFlightTest() {


        BusyFlightsRequest busyFlightsRequest = BusyFlightsRequest.builder()
                .returnDate("2012-05-23")
                .origin("origin")
                .numberOfPassengers(3)
                .destination("dest")
                .departureDate("2012-05-23")
                .build();

        List<BusyFlightsResponse> response = crazyAirClient.searchFlight(busyFlightsRequest);

        assertThat(response.size()).isEqualTo(2);
    }
}
