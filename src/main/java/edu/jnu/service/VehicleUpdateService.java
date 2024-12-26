package edu.jnu.service;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.Operation.BasicOperation;
import edu.jnu.entity.CarDeleteVO;
import edu.jnu.entity.CarInsertVO;
import edu.jnu.entity.CarUpdateVO;
import org.springframework.stereotype.Service;

@Service
public class VehicleUpdateService {
    public static CarInsertVO parseFromJson2(JSONObject jsonObject){
        return CarInsertVO.builder().type(jsonObject.getString("type")).
                energyType(jsonObject.getString("energyType")).
                carName(jsonObject.getString("carName")).
                minPrice(jsonObject.getDoubleValue("minPrice")).
                maxPrice(jsonObject.getDoubleValue("maxPrice")).
                rating(jsonObject.getDoubleValue("rating")).
                image(jsonObject.getString("image")).
                userId(jsonObject.getIntValue("userId")).build();
    }

    public static CarDeleteVO parseFromJson3(JSONObject jsonObject)
    {
        return CarDeleteVO.builder().type(jsonObject.getString("type")).
                energyType(jsonObject.getString("energyType")).
                carName(jsonObject.getString("car_name")).
                userId(jsonObject.getIntValue("userId")).build();
    }
    public static CarUpdateVO parseFromJson(JSONObject jsonObject){
        int carId = jsonObject.getIntValue("carId");
        String carName = jsonObject.getString("carName");
        Double minPrice = jsonObject.getDouble("carMinPrice");
        Double maxPrice = jsonObject.getDouble("carMaxPrice");
        Double rating = jsonObject.getDouble("carRating");
        String image = jsonObject.getString("carImage");
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

    public  static void insertCar(CarInsertVO carInsertVO){
        BasicOperation basicOperation = new BasicOperation();
        basicOperation.insertCar(carInsertVO.getType(), carInsertVO.getEnergyType(),
                carInsertVO.getCarName(),
                carInsertVO.getMinPrice(),
                carInsertVO.getMaxPrice(),
                carInsertVO.getRating(),
                carInsertVO.getImage(),
                carInsertVO.getUserId());
    }
    public static void deleteCar(CarDeleteVO carDeleteVO)
    {
        BasicOperation basicOperation = new BasicOperation();
        basicOperation.deleteCar(carDeleteVO.getCarName(), carDeleteVO.getType(), carDeleteVO.getEnergyType(), carDeleteVO.getUserId());
    }
}
