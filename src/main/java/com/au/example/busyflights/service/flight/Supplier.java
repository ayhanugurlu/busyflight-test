package com.au.example.busyflights.service.flight;

import com.au.example.busyflights.domain.busyflights.BusyFlightsRequest;
import com.au.example.busyflights.domain.busyflights.BusyFlightsResponse;

import java.util.List;

public interface Supplier {

    List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest);

}
