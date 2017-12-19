package com.travix.medusa.busyflights.service.toughjet;


import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.flight.toughjet.ToughJetImpl;
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

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToughJetImplTest {


    @MockBean
    RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private ToughJetImpl toughJet;


    @Before
    public void setUp() throws Exception {

        final ToughJetResponse resp1 =
                ToughJetResponse.builder()
                        .arrivalAirportName("to")
                        .carrier("KLM")
                        .departureAirportName("from")
                        .basePrice(100D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse("2017-06-12", DateTimeFormatter.ISO_LOCAL_DATE).atTime(01, 59).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse("2017-06-12", DateTimeFormatter.ISO_LOCAL_DATE).atTime(21, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();


        final ToughJetResponse resp2 =
                ToughJetResponse.builder()
                        .arrivalAirportName("to")
                        .carrier("KLM")
                        .departureAirportName("from")
                        .basePrice(100D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse("2017-06-12", DateTimeFormatter.ISO_LOCAL_DATE).atTime(01, 59).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse("2017-06-12", DateTimeFormatter.ISO_LOCAL_DATE).atTime(21, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();

        Mockito.when(restTemplate.postForEntity(Matchers.anyString(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ResponseEntity<>(new ToughJetResponse[]{resp1, resp2}, HttpStatus.OK));


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

        List<BusyFlightsResponse> response = toughJet.searchFlight(busyFlightsRequest);

        assertThat(response.size()).isEqualTo(2);
    }

}
