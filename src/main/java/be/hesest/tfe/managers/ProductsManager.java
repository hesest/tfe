package be.hesest.tfe.managers;

import be.hesest.tfe.entities.ProductEntity;

import java.util.HashMap;
import java.util.Map;

public class ProductsManager {

    private static Map<String, ProductEntity> products = new HashMap<>();

    public static Map<String, ProductEntity> getProducts() {
        return products;
    }

}