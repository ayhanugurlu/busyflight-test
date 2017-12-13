package com.travix.medusa.busyflights.service.flight;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;

public interface Flight {

    BusyFlightsResponse searchFlight(BusyFlightsRequest busyFlightsRequest);

}
