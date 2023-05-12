package be.hesest.tfe.managers;

import be.hesest.tfe.entities.MarketEntity;

import java.util.HashMap;
import java.util.Map;

public class MarketsManager {

    private static Map<String, MarketEntity> markets = new HashMap<>();

    public static Map<String, MarketEntity> getMarkets() {
        return markets;
    }

}