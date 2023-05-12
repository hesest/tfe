package be.hesest.tfe.models;

import be.hesest.tfe.entities.ProductEntity;
import be.hesest.tfe.units.ProductTypeUnit;

public class PriceModel {

    private ProductEntity product;
    private Float price;
    private ProductTypeUnit type;
    private Float unitPrice;

    public PriceModel(ProductEntity product, Float price, ProductTypeUnit type, Float unitPrice) {
        this.product = product;
        this.price = price;
        this.type = type;
        this.unitPrice = unitPrice;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public Float getPrice() {
        return price;
    }

    public ProductTypeUnit getType() {
        return type;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

}