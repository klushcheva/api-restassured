package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class PopularDirectionsResponseModel {
    private boolean success;
    private Map<String, FlightData> data;
    private String currency;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightData {
        private double price;

        @JsonProperty("flight_number")
        private int flightNumber;
        private int transfers;

        private String origin, destination, airline;
        @JsonProperty("departure_at")
        private String departureAt;
        @JsonProperty("return_at")
        private String returnAt;
        @JsonProperty("expires_at")
        private String expiresAt;
    }
}
