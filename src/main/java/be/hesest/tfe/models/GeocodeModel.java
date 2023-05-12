package be.hesest.tfe.models;

import java.util.List;

public class GeocodeModel {

    private List<Feature> features;

    public GeocodeModel(List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public class Feature {

        private Geometry geometry;

        public Feature(Geometry geometry) {
            this.geometry = geometry;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public class Geometry {

            List<Double> coordinates;

            public Geometry(List<Double> coordinates) {
                this.coordinates = coordinates;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }

        }

    }

}