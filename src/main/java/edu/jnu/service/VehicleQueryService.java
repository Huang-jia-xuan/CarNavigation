package edu.jnu.service;
import edu.jnu.Operation.BasicOperation;
import edu.jnu.entity.VehicleQueryByIdDTO;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.jnu.entity.VehicleQueryDTO;
import com.alibaba.fastjson2.JSONObject;
import edu.jnu.entity.Do;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleQueryService {

    public static VehicleQueryByIdDTO parseFromJson2(JSONObject jsonObject) {
        int id = jsonObject.getIntValue("id",0);
        // Parse the type list
        String type = jsonObject.getString("type");

        // Parse the energyType list
        String energyType = jsonObject.getString("energyType");


        // Create and return VehicleSearchCriteria object
        return new VehicleQueryByIdDTO(id,type,energyType);
    }

    public static VehicleQueryDTO parseFromJson(JSONObject jsonObject) {
        // Parse the type list
        String type = jsonObject.getString("type");

        // Parse the energyType list
        String energyType = jsonObject.getString("energyType");

        // Parse minPrice and maxPrice
        int minPrice = jsonObject.getIntValue("minPrice",0); // Default to 0 if not present
        int maxPrice = jsonObject.getIntValue("maxPrice",Integer.MAX_VALUE); // Default to Integer.MAX_VALUE if not present

        // Parse sortBy
        String sortBy = jsonObject.getString("sortBy"); // Default to empty string if not present
        String sortOrder = jsonObject.getString("sortOrder"); // Default to empty string if not present

        // Create and return VehicleSearchCriteria object
        return new VehicleQueryDTO(type, energyType, minPrice, maxPrice, sortBy,sortOrder);
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
                vehicleQueryDTO.getSortOrder()
        );
    }
    public Do queryVehicleFromDataBaseById(VehicleQueryByIdDTO vehicleQueryByIdDTO){
        BasicOperation basicOperation = new BasicOperation();
        return basicOperation.selectCarById(vehicleQueryByIdDTO.getId(),
                vehicleQueryByIdDTO.getType(),
                vehicleQueryByIdDTO.getEnergyType());
    }
}
