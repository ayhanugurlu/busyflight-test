package com.travix.medusa.busyflights.service.busyflights;


import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.flight.Flight;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusyFlightsImpl implements BusyFlights, ApplicationListener<ContextRefreshedEvent> {


    private Optional<List<Flight>> flights;


    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        return flights
                .map(f ->
                        f.stream()
                                .map(flight ->
                                        flight.searchFlight(busyFlightsRequest))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        flights = Optional.of(new ArrayList<>(contextRefreshedEvent.getApplicationContext().getBeansOfType(Flight.class).values()));

    }
}
