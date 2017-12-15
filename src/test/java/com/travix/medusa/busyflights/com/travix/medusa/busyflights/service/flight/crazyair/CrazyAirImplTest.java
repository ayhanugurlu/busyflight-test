package com.travix.medusa.busyflights.com.travix.medusa.busyflights.service.flight.crazyair;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.busyflights.BusyFlights;
import com.travix.medusa.busyflights.service.flight.crazyair.CrazyAirImpl;
import com.travix.medusa.busyflights.service.flight.toughjet.ToughJetImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CrazyAirImplTest {




    @Autowired
    private MockMvc mvc;

    @MockBean
    private Tracer tracer;

    @MockBean
    Span span;




    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Spy
    CrazyAirImpl crazyAir;


    @InjectMocks
    @Spy
    ToughJetImpl toughJet;


    @InjectMocks
    @Spy
    BusyFlights busyFlights;



    @Before
    public void setupMock() throws JsonProcessingException {


        MockitoAnnotations.initMocks(this);


        final CrazyAirRequest request = CrazyAirRequest.builder()
                .origin("origin")
                .departureDate("2015-12-15")
                .returnDate("2015-12-15")
                .destination("dest")
                .passengerCount(1)
                .build();


        final CrazyAirResponse resp1 =
                CrazyAirResponse.builder()
                        .airline("LH")
                        .cabinclass("E")
                        .departureAirportCode(request.getOrigin())
                        .destinationAirportCode(request.getDestination())
                        .price(112D)
                        .departureDate(
                                LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(12, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .arrivalDate(
                                LocalDate.parse(request.getReturnDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(21, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build();

        final CrazyAirResponse resp2 =
                CrazyAirResponse.builder()
                        .airline("BA")
                        .cabinclass("B")
                        .departureAirportCode(request.getOrigin())
                        .destinationAirportCode(request.getDestination())
                        .price(305D)
                        .departureDate(
                                LocalDate.parse(request.getDepartureDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(14, 20)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .arrivalDate(
                                LocalDate.parse(request.getReturnDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(22, 30)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build();


        final ToughJetRequest toughJetRequest = ToughJetRequest.builder()
                .from("from")
                .inboundDate("2017-12-26")
                .numberOfAdults(2)
                .to("to")
                .outboundDate("2017-12-27")
                .build();


        final ToughJetResponse toughJetResponse1 =
                ToughJetResponse.builder()
                        .arrivalAirportName(toughJetRequest.getTo())
                        .carrier("KLM")
                        .departureAirportName(toughJetRequest.getFrom())
                        .basePrice(100D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(toughJetRequest.getInboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(01, 59).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(toughJetRequest.getOutboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(21, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();

        final ToughJetResponse toughJetResponse2 =
                ToughJetResponse.builder()
                        .arrivalAirportName(toughJetRequest.getTo())
                        .carrier("THY")
                        .departureAirportName(toughJetRequest.getTo())
                        .arrivalAirportName(toughJetRequest.getFrom())
                        .basePrice(125D)
                        .discount(5)
                        .tax(40D)
                        .inboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(toughJetRequest.getInboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(07, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .outboundDateTime(DateTimeFormatter.ISO_INSTANT.format(
                                LocalDate.parse(toughJetRequest.getOutboundDate(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(23, 0).toInstant(
                                        ZoneOffset.UTC)))
                        .build();


        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(restTemplate.postForObject(anyString(), any(CrazyAirRequest.class), eq(String.class)))
                .thenReturn(objectMapper.writeValueAsString(new CrazyAirResponse[]{resp1, resp2}));

        Mockito.when(restTemplate.postForObject(anyString(), any(ToughJetRequest.class), eq(String.class)))
                .thenReturn(objectMapper.writeValueAsString(new ToughJetResponse[]{toughJetResponse1, toughJetResponse2}));


        when(span.getTraceId()).thenReturn(1l);

        when(tracer.getCurrentSpan()).thenReturn(span);


    }

    @Test
    public void searchFlightTest() throws Exception {
        BusyFlightsRequest busyFlightsRequest = BusyFlightsRequest.builder()
                .departureDate("2017-05-12")
                .destination("dest")
                .numberOfPassengers(1)
                .origin("origin")
                .returnDate("2017-05-13").build();


        final CrazyAirResponse resp1 =
                CrazyAirResponse.builder()
                        .airline("LH")
                        .cabinclass("E")
                        .departureAirportCode("Origin")
                        .destinationAirportCode("Dest")
                        .price(112D)
                        .departureDate(
                                LocalDate.parse("2017-12-12", DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(12, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .arrivalDate(
                                LocalDate.parse("2017-12-13", DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(21, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build();

        final CrazyAirResponse resp2 =
                CrazyAirResponse.builder()
                        .airline("LH")
                        .cabinclass("E")
                        .departureAirportCode("Origin")
                        .destinationAirportCode("Dest")
                        .price(112D)
                        .departureDate(
                                LocalDate.parse("2017-12-12", DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(12, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .arrivalDate(
                                LocalDate.parse("2017-12-13", DateTimeFormatter.ISO_LOCAL_DATE)
                                        .atTime(21, 15)
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build();

        ObjectMapper mapper = new ObjectMapper();

        MvcResult mvcResult = this.mvc.perform(post("/busyflight/search").content(mapper.writeValueAsBytes(busyFlightsRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println();


    }

}
