package be.hesest.tfe.units;

public enum TransportModeUnit {

    DRIVING_CAR("Voiture", "driving-car"),
    DRIVING_HGV("Poids lourd", "driving-hgv"),
    CYCLING_ELECTRIC("Vélo électrique", "cycling-electric"),
    CYCLING_REGULAR("Vélo de ville", "cycling-regular"),
    CYCLING_MOUNTAIN("Vélo tout terrain", "cycling-mountain"),
    CYCLING_ROAD("Vélo de route", "cycling-road"),
    FOOT_WALKING("Marche", "foot-walking"),
    FOOT_HIKING("Randonnée", "foot-hiking"),
    WHEELCHAIR("Chaise roulante", "wheelchair");

    private final String name;
    private final String openRouteServiceName;

    TransportModeUnit(String name, String openRouteServiceName) {
        this.name = name;
        this.openRouteServiceName = openRouteServiceName;
    }

    public String getName() {
        return name;
    }

    public String getOpenRouteServiceName() {
        return openRouteServiceName;
    }

    public static TransportModeUnit findByName(String name) {
        for (TransportModeUnit transportMode : values()) {
            if (transportMode.getName().equals(name)) {
                return transportMode;
            }
        }
        throw new IllegalArgumentException("Mode de transport non trouvé pour " + name);
    }

}