package be.hesest.tfe.utils;

import java.util.*;

public class ConverterUtil {

    public static Map<String, Integer> convertStringToMap(String string) {
        // Transformation du String en Map
        Map<String, Integer> map = new HashMap<>();
        if (!string.equals("")) {
            String[] array1 = string.split(",");
            if (array1.length > 0) {
                for (String s : array1) {
                    String[] array2 = s.split(":");
                    map.put(array2[0], Integer.valueOf(array2[1]));
                }
            } else {
                String[] array2 = string.split(":");
                map.put(array2[0], Integer.valueOf(array2[1]));
            }
        }
        return map;
    }

    public static String convertMapToString(Map<String, Integer> map) {
        // Transformation de la Map en String
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < map.size(); i++) {
            if (i > 0) {
                string.append(",").append(map.keySet().toArray()[i]).append(":").append(map.values().toArray()[i]);
            } else {
                string.append(map.keySet().toArray()[i]).append(":").append(map.values().toArray()[i]);
            }
        }
        return string.toString();
    }

    public static List<String> convertStringToList(String string) {
        // Transformation du String en List
        List<String> list = new ArrayList<>();
        if (!string.equals("")) {
            String[] array = string.split(",");
            if (array.length > 0) {
                list.addAll(Arrays.asList(array));
            } else {
                list.add(string);
            }
        }
        return list;
    }

    public static String convertListToString(List<String> list) {
        // Transformation de la List en String
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                string.append(",").append(list.get(i));
            } else {
                string.append(list.get(i));
            }
        }
        return string.toString();
    }

}