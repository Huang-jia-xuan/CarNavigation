package edu.jnu.service;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.Operation.BasicOperation;
import edu.jnu.entity.CarUpdateVO;
import org.springframework.stereotype.Service;

@Service
public class VehicleUpdateService {
    public static CarUpdateVO parseFromJson(JSONObject jsonObject){
        int carId = jsonObject.getIntValue("carId");
        String carName = jsonObject.getString("carName");
        Double minPrice = jsonObject.getDouble("minPrice");
        Double maxPrice = jsonObject.getDouble("maxPrice");
        Double rating = jsonObject.getDouble("rating");
        String image = jsonObject.getString("image");
        String type = jsonObject.getString("type");
        String energyType = jsonObject.getString("energyType");
        String newType = jsonObject.getString("newType");
        String newEnergyType = jsonObject.getString("newEnergyType");
        int userId = jsonObject.getIntValue("userId");
        return CarUpdateVO.builder().carId(carId).carName(carName).minPrice(minPrice).maxPrice(maxPrice).rating(rating).image(image).type(type).energyType(energyType).newType(newType).newEnergyType(newEnergyType).userId(userId).build();
    }

    public static void updateCar(CarUpdateVO carUpdateVO){
        BasicOperation basicOperation = new BasicOperation();
        basicOperation.updateCar(carUpdateVO.getCarId(),carUpdateVO.getCarName(),carUpdateVO.getMinPrice(),carUpdateVO.getMaxPrice(),carUpdateVO.getRating(),carUpdateVO.getImage(),carUpdateVO.getType(),carUpdateVO.getEnergyType(),carUpdateVO.getNewType(),carUpdateVO.getNewEnergyType(),carUpdateVO.getUserId());
    }
}
