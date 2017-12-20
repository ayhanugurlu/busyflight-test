package com.travix.medusa.busyflights.domain.toughjet;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ToughJetResponse {

    private String carrier;
    private double basePrice;
    private double tax;
    private double discount;
    private String departureAirportName;
    private String arrivalAirportName;
    private String outboundDateTime;
    private String inboundDateTime;

}
