package com.travix.medusa.busyflights.service.busyflights;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.flight.Flight;

import java.util.List;

public interface BusyFlights {

    List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest);

}
