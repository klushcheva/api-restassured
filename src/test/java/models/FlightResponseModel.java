package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FlightResponseModel {
    private boolean success;
    private List<FlightData> data;

    @Data
    public static class FlightData {
        private double price;
        private int transfers, duration;
        @JsonProperty("return_transfers")
        private int returnTransfers;
        @JsonProperty("duration_to")
        private int durationTo;
        @JsonProperty("duration_back")
        private int durationBack;
        private String origin, destination, airline, link;
        @JsonProperty("origin_airport")
        private String originAirport;
        @JsonProperty("destination_airport")
        private String destinationAirport;
        private String flightNumber;
        private String departureAt;
        private String returnAt;
    }
}