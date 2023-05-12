package be.hesest.tfe.models;

public class DelhaizeModel {

    private Data data;

    public DelhaizeModel(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data {

        private ProductDetails productDetails;

        public Data(ProductDetails productDetails) {
            this.productDetails = productDetails;
        }

        public ProductDetails getProductDetails() {
            return productDetails;
        }

        public class ProductDetails {

            private Price price;

            public ProductDetails(Price price) {
                this.price = price;
            }

            public Price getPrice() {
                return price;
            }

            public class Price {

                private Float unitPrice;
                private String supplementaryPriceLabel1;

                public Price(Float unitPrice, String supplementaryPriceLabel1) {
                    this.unitPrice = unitPrice;
                    this.supplementaryPriceLabel1 = supplementaryPriceLabel1;
                }

                public Float getUnitPrice() {
                    return unitPrice;
                }

                public String getSupplementaryPriceLabel1() {
                    return supplementaryPriceLabel1;
                }

            }

        }

    }

}