package com.travix.medusa.busyflights.service.crazyair;


import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.flight.crazyair.CrazyAirImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CrazyAirImplTest {

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private CrazyAirImpl crazyAirClient;

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


        Mockito.when(restTemplate.postForEntity(Matchers.anyString(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ResponseEntity<>(new CrazyAirResponse[]{crazyAirResponse1, crazyAirResponse2}, HttpStatus.OK));


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
