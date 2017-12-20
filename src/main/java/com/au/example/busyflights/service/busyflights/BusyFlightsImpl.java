package com.au.example.busyflights.service.busyflights;


import com.au.example.busyflights.domain.busyflights.BusyFlightsRequest;
import com.au.example.busyflights.domain.busyflights.BusyFlightsResponse;
import com.au.example.busyflights.service.flight.Supplier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusyFlightsImpl implements BusyFlights, ApplicationListener<ContextRefreshedEvent> {


    private Optional<List<Supplier>> flights;


    @Override
    public List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest) {
        return flights
                .map(f ->
                        f.stream()
                                .map(supplier ->
                                        supplier.searchFlight(busyFlightsRequest))
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        flights = Optional.of(new ArrayList<>(contextRefreshedEvent.getApplicationContext().getBeansOfType(Supplier.class).values()));

    }
}
