package com.travix.medusa.busyflights.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.busyflights.BusyFlights;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BusyFlightsController.class)
public class BusyFlightsControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private Tracer tracer;

    @MockBean
    Span span;


    @MockBean
    BusyFlights busyFlights;


    @Before
    public void setupMock() throws JsonProcessingException {

        BusyFlightsResponse busyFlightsResponse1 = BusyFlightsResponse.builder().
                airline("klm1").
                arrivalDate("2017-05-13").
                cabinclass("class").
                departureAirportCode("depaicode").
                departureDate("2017-05-13").
                price(28).build();

        BusyFlightsResponse busyFlightsResponse2 = BusyFlightsResponse.builder().
                airline("klm2").
                arrivalDate("2017-05-13").
                cabinclass("class").
                departureAirportCode("depaicode").
                departureDate("2017-05-13").
                price(28).build();


        List<BusyFlightsResponse> responses = Arrays.asList(busyFlightsResponse1, busyFlightsResponse2);

        when(busyFlights.searchFlight(any(BusyFlightsRequest.class))).thenReturn(responses);

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

        ObjectMapper mapper = new ObjectMapper();
        MvcResult mvcResult = this.mvc.perform(post("/busyflight/search").content(mapper.writeValueAsBytes(busyFlightsRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        BusyFlightsResponse[] result = mapper.readValue(mvcResult.getResponse().getContentAsString(), BusyFlightsResponse[].class);
        assertThat(result.length).isEqualTo(2);
        assertThat(result[0].getAirline()).isEqualTo("klm1");

    }

}
