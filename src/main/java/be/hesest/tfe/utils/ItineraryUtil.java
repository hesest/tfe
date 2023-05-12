package be.hesest.tfe.utils;

import be.hesest.tfe.entities.MarketEntity;
import be.hesest.tfe.managers.MarketsManager;
import be.hesest.tfe.models.GeocodeModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryUtil {

    private static String openRouteServiceApiKey;

    @Value("${spring.openrouteservice.api-key}")
    private void setOpenRouteServiceApiKey(String openRouteServiceApiKeyValue) {
        openRouteServiceApiKey = openRouteServiceApiKeyValue;
    }

    public static List<MarketEntity> findMarkets(String transportMode, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        List<MarketEntity> markets = new ArrayList<>();
        List<List<Double>> coordinates = getItinerary(transportMode, startLatitude, startLongitude, endLatitude, endLongitude);
        for (List<Double> coordinate : coordinates) {
            double coordinateLatitude = coordinate.get(0);
            double coordinateLongitude = coordinate.get(1);
            for (MarketEntity market : MarketsManager.getMarkets().values()) {
                double marketLatitude = market.getAddressLatitude();
                double marketLongitude = market.getAddressLongitude();
                // Vérifier si la coordonnée se trouve dans une zone carrée de 1 kilomètre autour du magasin
                if (coordinateLatitude >= marketLatitude - 0.01 && coordinateLatitude <= marketLatitude + 0.01 && coordinateLongitude >= marketLongitude - 0.01 && coordinateLongitude <= marketLongitude + 0.01) {
                    // La coordonnée se trouve dans la zone, on ajoute le magasin s'il n'est pas déjà dedans
                    if (!markets.contains(market)) {
                        markets.add(market);
                    }
                    // En procédant de la sorte, on s'assure d'avoir le magasin d'une enseigne le plus proche de chez soi car on commence à vérifier les coordonnées les plus proches du point de départ
                }
            }
        }
        return markets;
    }

    public static List<Double> getCoordinates(String address) {
        // Appel de l'API OpenRouteService pour récupérer l'itinéraire
        String response = new RestTemplate().getForObject("https://api.openrouteservice.org/geocode/search?api_key=" + openRouteServiceApiKey + "&text=" + address.replaceAll(" ", "+"), String.class);

        // Récupération des informations
        GeocodeModel geocodeModel = new Gson().fromJson(response, GeocodeModel.class);
        if (geocodeModel.getFeatures().isEmpty()) {
            return new ArrayList<>();
        } else {
            return List.of(geocodeModel.getFeatures().get(0).getGeometry().getCoordinates().get(1), geocodeModel.getFeatures().get(0).getGeometry().getCoordinates().get(0));
        }
    }

    // Fonction pour calculer un itinéraire
    private static List<List<Double>> getItinerary(String transportMode, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        try {
            // Appel de l'API OpenRouteService pour récupérer l'itinéraire
            String response = new RestTemplate().getForObject("https://api.openrouteservice.org/v2/directions/" + transportMode + "?api_key=" + openRouteServiceApiKey + "&start=" + startLongitude + "," + startLatitude + "&end=" + endLongitude + "," + endLatitude + "&format=geojson&geometry_format=geojson", String.class);

            // Récupération de la liste des points le long de l'itinéraire
            JsonNode rootNode = new ObjectMapper().readTree(response);
            ArrayNode coordinatesNode = (ArrayNode) rootNode.path("features").get(0).path("geometry").path("coordinates");

            // Initialisation des variables pour le parcours de la ligne géométrique
            double totalDistance = 0.0;
            List<List<Double>> result = new ArrayList<>();

            // Ajout de la première coordonnée de l'itinéraire
            List<Double> firstCoordinate = new ArrayList<>();
            firstCoordinate.add(coordinatesNode.get(0).get(1).asDouble());
            firstCoordinate.add(coordinatesNode.get(0).get(0).asDouble());
            result.add(firstCoordinate);

            // Parcours de la ligne géométrique pour récupérer les coordonnées tous les 1 km
            for (int i = 1; i < coordinatesNode.size(); i++) {
                double latitude = coordinatesNode.get(i).get(1).asDouble();
                double longitude = coordinatesNode.get(i).get(0).asDouble();
                double currentDistance = distance(firstCoordinate.get(0), firstCoordinate.get(1), latitude, longitude);
                if (totalDistance + currentDistance >= 1.0) {
                    double distanceRatio = (1.0 - totalDistance / currentDistance);
                    double newLatitude = firstCoordinate.get(0) + distanceRatio * (latitude - firstCoordinate.get(0));
                    double newLongitude = firstCoordinate.get(1) + distanceRatio * (longitude - firstCoordinate.get(1));
                    List<Double> newCoordinate = new ArrayList<>();
                    newCoordinate.add(newLatitude);
                    newCoordinate.add(newLongitude);
                    result.add(newCoordinate);
                    totalDistance = 0.0;
                    firstCoordinate = newCoordinate;
                } else {
                    totalDistance += currentDistance;
                }
            }

            // Ajout de la dernière coordonnée de l'itinéraire
            List<Double> lastCoordinate = new ArrayList<>();
            lastCoordinate.add(coordinatesNode.get(coordinatesNode.size() - 1).get(1).asDouble());
            lastCoordinate.add(coordinatesNode.get(coordinatesNode.size() - 1).get(0).asDouble());
            result.add(lastCoordinate);

            return result;
        } catch (RestClientException | JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    // Fonction pour calculer la distance entre deux points GPS
    private static double distance(double latitude1, double longitude1, double latitude2, double longitude2) {
        final int R = 6371; // Rayon de la terre en kilomètres
        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}