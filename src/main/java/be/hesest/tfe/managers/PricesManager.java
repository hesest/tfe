package be.hesest.tfe.managers;

import be.hesest.tfe.entities.ProductEntity;
import be.hesest.tfe.models.PriceModel;
import be.hesest.tfe.utils.PriceUtil;

import java.util.HashMap;
import java.util.Map;

public class PricesManager {

    private static Map<String, PriceModel> prices = new HashMap<>();

    public static PriceModel getProductPrice(ProductEntity product) {
        if (!prices.containsKey(product.getId())) {
            prices.put(product.getId(), PriceUtil.getPrice(product));
        }
        return prices.get(product.getId());
    }

}