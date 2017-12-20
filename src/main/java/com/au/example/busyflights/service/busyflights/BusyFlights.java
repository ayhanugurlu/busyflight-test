package com.au.example.busyflights.service.busyflights;

import com.au.example.busyflights.domain.busyflights.BusyFlightsRequest;
import com.au.example.busyflights.domain.busyflights.BusyFlightsResponse;

import java.util.List;

public interface BusyFlights {

    List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest);

}
