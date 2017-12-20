package com.au.example.busyflights.service.busyflights;

import com.au.example.busyflights.domain.busyflights.BusyFlightsRequest;
import com.au.example.busyflights.domain.busyflights.BusyFlightsResponse;
import com.au.example.busyflights.service.flight.Supplier;
import com.au.example.busyflights.service.flight.crazyair.CrazyAirImpl;
import com.au.example.busyflights.service.flight.toughjet.ToughJetImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;


public class BusyFlightsImplTest {

    public static final String DEPARTURE_DATE = "2017-05-12";
    public static final String DEST = "dest";
    public static final int NUMBER_OF_PASSENGERS = 1;
    public static final String ORIGIN = "origin";
    public static final String RETURN_DATE = "2017-05-13";


    private Optional<List<Supplier>> flights = Optional.empty();

    private BusyFlightsRequest busyFlightsRequest;

    @Mock
    private CrazyAirImpl one;

    @Mock
    private ToughJetImpl two;

    @Mock
    private ContextRefreshedEvent contextRefreshedEvent;

    @Mock
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void searchFlight() throws Exception {

        busyFlightsRequest = BusyFlightsRequest.builder()
                .departureDate(DEPARTURE_DATE)
                .destination(DEST)
                .numberOfPassengers(NUMBER_OF_PASSENGERS)
                .origin(ORIGIN)
                .returnDate(RETURN_DATE)
                .build();
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

        Mockito.when(one.searchFlight(busyFlightsRequest)).thenReturn(Collections.singletonList(busyFlightsResponse1));


        Mockito.when(two.searchFlight(busyFlightsRequest)).thenReturn(Collections.singletonList((busyFlightsResponse2)));
        flights = Optional.of(Arrays.asList(one, two));


        BusyFlightsImpl busyFlights = new BusyFlightsImpl();


        ReflectionTestUtils.setField(busyFlights, "flights", flights);
        List<BusyFlightsResponse> busyFlightsResponses = busyFlights.searchFlight(busyFlightsRequest);
        Assertions.assertThat(busyFlightsResponses.size()).isEqualTo(2);

    }

    @Test
    public void onApplicationEvent() throws Exception {

        BusyFlightsImpl busyFlights = new BusyFlightsImpl();
        Mockito.when(contextRefreshedEvent.getApplicationContext()).thenReturn(applicationContext);
        Map<String, Supplier> map = new HashMap<>();
        map.put("crazyAirImpl",new CrazyAirImpl());
        map.put("toughJetImpl",new ToughJetImpl());
        Mockito.when(applicationContext.getBeansOfType(Supplier.class)).thenReturn(map);
        busyFlights.onApplicationEvent(contextRefreshedEvent);
        flights = (Optional<List<Supplier>>) ReflectionTestUtils.getField(busyFlights,"flights");
        Assertions.assertThat(flights.map(suppliers -> suppliers.size()).orElse(0)).isEqualTo(2);

    }

}