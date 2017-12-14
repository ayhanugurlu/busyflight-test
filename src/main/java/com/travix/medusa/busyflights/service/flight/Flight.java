package com.travix.medusa.busyflights.service.flight;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;

import java.util.List;

public interface Flight {

    List<BusyFlightsResponse> searchFlight(BusyFlightsRequest busyFlightsRequest);

}
