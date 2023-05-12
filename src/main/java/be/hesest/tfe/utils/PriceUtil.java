package be.hesest.tfe.utils;

import be.hesest.tfe.entities.ProductEntity;
import be.hesest.tfe.models.ColruytModel;
import be.hesest.tfe.models.DelhaizeModel;
import be.hesest.tfe.models.PriceModel;
import be.hesest.tfe.units.ProductTypeUnit;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class PriceUtil {

    private static String colruytApiKey;

    @Value("${spring.colruyt.api-key}")
    private void setColruytApiKey(String colruytApiKeyValue) {
        colruytApiKey = colruytApiKeyValue;
    }

    public static PriceModel getPrice(ProductEntity product) {
        RestTemplate restTemplate = new RestTemplate();
        switch (product.getMarketName()) {
            case "Colruyt" -> {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("User-Agent", "Mozilla/5.0 Firefox/26.0");
                httpHeaders.set("X-CG-APIKey", colruytApiKey); // Ce header permet de s'authentifier sur l'API
                String json = restTemplate.exchange(product.getMarketLink(), HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class).getBody();
                ColruytModel colruytModel = new Gson().fromJson(json, ColruytModel.class);
                return new PriceModel(product, colruytModel.getPrice().getBasicPrice(), colruytModel.getPrice().getMeasurementUnit().equals("K") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, colruytModel.getPrice().getMeasurementUnitPrice());
            }
            case "Delhaize" -> {
                String[] map1 = product.getMarketLink().split("\\?");
                String[] map2 = map1[1].split("&");
                String[] map21 = map2[0].split("=");
                String[] map22 = map2[1].split("=");
                String[] map23 = map2[2].split("=");
                try {
                    String json = restTemplate.getForObject(map1[0] + "?" + map21[0] + "={" + map21[0] + "}&" + map22[0] + "={" + map22[0] + "}&" + map23[0] + "={" + map23[0] + "}", String.class, Map.of(map21[0], map21[1], map22[0], map22[1], map23[0], map23[1]));
                    DelhaizeModel delhaizeModel = new Gson().fromJson(json, DelhaizeModel.class);
                    String[] array = delhaizeModel.getData().getProductDetails().getPrice().getSupplementaryPriceLabel1().split("/"); // On sépare car il y a le prix par unité
                    return new PriceModel(product, delhaizeModel.getData().getProductDetails().getPrice().getUnitPrice(), array[1].equals("kg") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, Float.valueOf(array[0].substring(0, array[0].length() - 2).replaceAll(",", "."))); // Le "substring" sert à retirer des éléments indésirables
                } catch (RestClientException e) {
                    return null;
                }
            }
            case "Carrefour" -> {
                try {
                    Document document = Jsoup.connect(product.getMarketLink()).get();
                    Elements priceElements = document.getElementsByClass("showRed");
                    Elements unitPriceElements = document.getElementsByClass("prod-price-comparison");
                    String[] array = unitPriceElements.get(0).text().split("/"); // On sépare car il y a le prix par unité
                    return new PriceModel(product, Float.valueOf(priceElements.get(0).text().substring(0, priceElements.get(0).text().length() - 1).replaceAll(",", ".")), array[1].equals("kg") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, Float.valueOf(array[0].substring(0, array[0].length() - 1).replaceAll(",", "."))); // Le "substring" sert à retirer des éléments indésirables
                } catch (IOException e) {
                    return null;
                }
            }
            case "Aldi" -> {
                try {
                    Document document = Jsoup.connect(product.getMarketLink()).get();
                    Elements priceElements = document.getElementsByClass("price__wrapper");
                    Elements unitPriceElements = document.getElementsByClass("price__base");
                    String[] array = unitPriceElements.get(0).text().split("/"); // On sépare car il y a le prix par unité
                    return new PriceModel(product, Float.valueOf(priceElements.get(0).text()), array[1].equals("kg") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, Float.valueOf(array[0]));
                } catch (IOException e) {
                    return null;
                }
            }
            case "Kruitvat" -> {
                try {
                    Document document = Jsoup.connect(product.getMarketLink()).get();
                    Elements decimalPriceElements = document.getElementsByClass("pricebadge__new-price-decimal");
                    Elements fractionalPriceElements = document.getElementsByClass("pricebadge__new-price-fractional");
                    Elements unitPriceElements = document.getElementsByClass("e2-cta__price-per-unit");
                    return new PriceModel(product, Float.valueOf(decimalPriceElements.get(0).text() + "." + fractionalPriceElements.get(0).text()), unitPriceElements.get(0).text().contains("kg") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, Float.valueOf(unitPriceElements.get(0).text().replace("Prix par kg € ", "").replaceAll(",", ".")));
                } catch (IOException e) {
                    return null;
                }
            }
            case "Action" -> {
                try {
                    Document document = Jsoup.connect(product.getMarketLink()).get();
                    Elements decimalPriceElements = document.getElementsByClass("font-bold text-price-whole-xl sm:text-price-whole-xxl");
                    Elements fractionalPriceElements = document.getElementsByClass("font-bold text-price-fraction-lg sm:text-price-fraction-xl");
                    Elements unitPriceElements = document.getElementsByClass("flex flex-col text-xxs leading-xxs text-func-dark-grey-60 sm:text-xs sm:leading-none");
                    String[] array = unitPriceElements.get(0).text().split("/"); // On sépare car il y a le prix par unité
                    return new PriceModel(product, Float.valueOf(decimalPriceElements.get(0).text() + "." + fractionalPriceElements.get(0).text()), array[1].equals("kg") ? ProductTypeUnit.DRY : ProductTypeUnit.LIQUID, Float.valueOf(array[0].substring(0, array[0].length() - 2).replaceAll(",", "."))); // Le "substring" sert à retirer des éléments indésirables
                } catch (IOException e) {
                    return null;
                }
            }
            default -> {
                return null;
            }
        }
    }

}