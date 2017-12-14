package com.travix.medusa.busyflights.validation.validator;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BusyFlightRequestValidator implements Validator<BusyFlightsRequest> {
    @Override
    public void validate(BusyFlightsRequest param, List<String> errors) {
        if (StringUtils.isEmpty(param.getDepartureDate())) {
            errors.add(String.format(FIELD_CANNOT_BE_EMPTY, "Departure Date"));
        } else {
            try {
                LocalDate.parse(param.getDepartureDate(), DateTimeFormatter.ISO_LOCAL_DATE);

            } catch (DateTimeParseException e) {
                errors.add(String.format(DATE_FORMAT_ERROR, "Departure Date", param.getDepartureDate()));
            }
        }


        if (StringUtils.isEmpty(param.getReturnDate())) {
            errors.add(String.format(FIELD_CANNOT_BE_EMPTY, "Return Date"));
        } else {
            try {
                LocalDate.parse(param.getReturnDate(), DateTimeFormatter.ISO_LOCAL_DATE);

            } catch (DateTimeParseException e) {
                errors.add(String.format(DATE_FORMAT_ERROR, "Return Date", param.getReturnDate()));
            }
        }

        if (StringUtils.isEmpty(param.getDestination())) {
            errors.add(String.format(FIELD_CANNOT_BE_EMPTY, "Destination"));
        }
        if (param.getNumberOfPassengers() == 0) {
            errors.add(String.format(FIELD_CANNOT_BE_EMPTY, "Number of Passenger"));
        }
        if (StringUtils.isEmpty(param.getOrigin())) {
            errors.add(String.format(FIELD_CANNOT_BE_EMPTY, "Origin"));
        }
    }
}
