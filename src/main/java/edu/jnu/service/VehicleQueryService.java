package edu.jnu.service;
import edu.jnu.Operation.BasicOperation;
import edu.jnu.entity.VehicleQueryByIdDTO;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.jnu.entity.VehicleQueryDTO;
import org.json.JSONObject;
import edu.jnu.entity.Do;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleQueryService {

    public static VehicleQueryByIdDTO parseFromJson2(JSONObject jsonObject) {
        int id = jsonObject.optInt("id", 0);
        // Parse the type list
        String type = jsonObject.optString("type","");

        // Parse the energyType list
        String energyType = jsonObject.optString("energyType","");


        // Create and return VehicleSearchCriteria object
        return new VehicleQueryByIdDTO(id,type,energyType);
    }

    public static VehicleQueryDTO parseFromJson(JSONObject jsonObject) {
        // Parse the type list
        String type = jsonObject.optString("type","");

        // Parse the energyType list
        String energyType = jsonObject.optString("energyType","");

        // Parse minPrice and maxPrice
        int minPrice = jsonObject.optInt("minPrice", 0); // Default to 0 if not present
        int maxPrice = jsonObject.optInt("maxPrice", Integer.MAX_VALUE); // Default to Integer.MAX_VALUE if not present

        // Parse sortBy
        String sortBy = jsonObject.optString("sortBy", ""); // Default to empty string if not present

        // Create and return VehicleSearchCriteria object
        return new VehicleQueryDTO(type, energyType, minPrice, maxPrice, sortBy);
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

    public ArrayList<Do> queryVehicleFromDataBase(VehicleQueryDTO vehicleQueryDTO){
        return BasicOperation.UserSelect(
                vehicleQueryDTO.getType(),
                vehicleQueryDTO.getEnergyType(),
                vehicleQueryDTO.getMinPrice(),
                vehicleQueryDTO.getMaxPrice(),
                vehicleQueryDTO.getSortBy(),
                null
        );
    }
    public Do queryVehicleFromDataBaseById(VehicleQueryByIdDTO vehicleQueryByIdDTO){
        BasicOperation basicOperation = new BasicOperation();
        return basicOperation.selectCarById(vehicleQueryByIdDTO.getId(),
                vehicleQueryByIdDTO.getType(),
                vehicleQueryByIdDTO.getEnergyType());
    }
}
