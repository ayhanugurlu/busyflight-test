package com.travix.medusa.busyflights.mapper;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.stereotype.Component;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class BusyFlightServiceMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(BusyFlightsRequest.class, CrazyAirRequest.class)
                .field("numberOfPassengers", "passengerCount")
                .byDefault()
                .register();

        factory.classMap(BusyFlightsRequest.class, ToughJetRequest.class)
                .field("origin", "from")
                .field("destination", "to")
                .field("departureDate", "outboundDate")
                .field("returnDate", "inboundDate")
                .field("numberOfPassengers", "numberOfAdults")
                .byDefault()
                .register();


        factory.classMap(CrazyAirResponse.class, BusyFlightsResponse.class)
                .byDefault()
                .register();


        factory.classMap(ToughJetResponse.class, BusyFlightsResponse.class)
                .field("carrier", "airline")
                .field("departureAirportName", "departureAirportCode")
                .field("arrivalAirportName", "destinationAirportCode")
                .field("outboundDateTime", "departureDate")
                .field("inboundDateTime", "arrivalDate").customize(new CustomMapper<ToughJetResponse, BusyFlightsResponse>() {
            @Override
            public void mapAtoB(ToughJetResponse toughJetResponse, BusyFlightsResponse busyFlightsResponse, MappingContext context) {
                double price = toughJetResponse.getBasePrice() + toughJetResponse.getTax() - toughJetResponse.getTax();
                busyFlightsResponse.setPrice(price);
            }
        }).register();
    }

}
