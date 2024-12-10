package edu.jnu.service;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.jnu.entity.vehicleQueryDTO;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class vehicleQueryService {

    public static vehicleQueryDTO parseFromJson(JSONObject jsonObject) {
        // Parse the type list
        JSONArray typeArray = jsonObject.optJSONArray("type");
        List<String> type = jsonArrayToList(typeArray);

        // Parse the energyType list
        JSONArray energyTypeArray = jsonObject.optJSONArray("energyType");
        List<String> energyType = jsonArrayToList(energyTypeArray);

        // Parse minPrice and maxPrice
        int minPrice = jsonObject.optInt("minPrice", 0); // Default to 0 if not present
        int maxPrice = jsonObject.optInt("maxPrice", Integer.MAX_VALUE); // Default to Integer.MAX_VALUE if not present

        // Parse sortBy
        String sortBy = jsonObject.optString("sortBy", ""); // Default to empty string if not present

        // Create and return VehicleSearchCriteria object
        return new vehicleQueryDTO(type, energyType, minPrice, maxPrice, sortBy);
    }

    private static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.optString(i));
            }
        }
        return list;
    }
}
