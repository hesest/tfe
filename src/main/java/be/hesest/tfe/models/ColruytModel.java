package be.hesest.tfe.models;

public class ColruytModel {

    private Price price;

    public ColruytModel(Price price) {
        this.price = price;
    }

    public Price getPrice() {
        return price;
    }

    public class Price {

        private Float basicPrice;
        private Float measurementUnitPrice;
        private String measurementUnit;

        public Price(Float basicPrice, Float measurementUnitPrice, String measurementUnit) {
            this.basicPrice = basicPrice;
            this.measurementUnitPrice = measurementUnitPrice;
            this.measurementUnit = measurementUnit;
        }

        public Float getBasicPrice() {
            return basicPrice;
        }

        public Float getMeasurementUnitPrice() {
            return measurementUnitPrice;
        }

        public String getMeasurementUnit() {
            return measurementUnit;
        }

    }

}