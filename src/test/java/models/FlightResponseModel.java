package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlightResponseModel {
    private boolean success;
    private List<FlightData> data;
    private String currency;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class FlightData {
        private boolean actual;

        private double price;

        private int transfers, duration;
        @JsonProperty("return_transfers")
        private int returnTransfers;
        @JsonProperty("duration_to")
        private int durationTo;
        @JsonProperty("duration_back")
        private int durationBack;

        private String origin, destination, airline, link;
        @JsonProperty("destination_airport")
        private String destinationAirport;
        @JsonProperty("flight_number")
        private String flightNumber;
        @JsonProperty("depart_date")
        private String departDate;
        @JsonProperty("return_date")
        private String returnDate;
        @JsonProperty("origin_airport")
        private String originAirport;
    }
}