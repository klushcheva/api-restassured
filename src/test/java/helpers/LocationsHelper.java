package helpers;

import models.PlacesMatrixResponseModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationsHelper {
    public static List<Set<String>> getDestinationSets(PlacesMatrixResponseModel model) {
        List<String> destinationsFromResponse = model.getPrices().stream()
                .map(PlacesMatrixResponseModel.PriceData::getDestination)
                .toList();
        Set<String> destinationsSet = new HashSet<>(destinationsFromResponse);
        Set<String> expectedDestinations = new HashSet<>(model.getDestinations());
        return List.of(destinationsSet, expectedDestinations);
    }

    public static List<Set<String>> getOriginSets(PlacesMatrixResponseModel model) {
        List<String> originsFromResponse = model.getPrices().stream()
                .map(PlacesMatrixResponseModel.PriceData::getOrigin)
                .toList();
        Set<String> originsSet = new HashSet<>(originsFromResponse);
        Set<String> expectedOrigins = new HashSet<>(model.getOrigins());
        return List.of(originsSet, expectedOrigins);
    }
}
