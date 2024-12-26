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
    public static CarUpdateVO parseFromJson(JSONObject jsonObject) {
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

        return CarUpdateVO.builder()
                .carName(carName)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .rating(rating)
                .image(image)
                .type(type)
                .energyType(energyType)
                .newType(newType)
                .newEnergyType(newEnergyType)
                .userId(userId)
                .build();
    }


    public static void updateCar(CarUpdateVO carUpdateVO) {
        // 打印 CarUpdateVO 的所有字段
        System.out.println("updateCar 方法接收到的参数：");
        System.out.println("carName: " + carUpdateVO.getCarName());
        System.out.println("minPrice: " + carUpdateVO.getMinPrice());
        System.out.println("maxPrice: " + carUpdateVO.getMaxPrice());
        System.out.println("rating: " + carUpdateVO.getRating());
        System.out.println("image: " + carUpdateVO.getImage());
        System.out.println("type: " + carUpdateVO.getType());
        System.out.println("energyType: " + carUpdateVO.getEnergyType());
        System.out.println("newType: " + carUpdateVO.getNewType());
        System.out.println("newEnergyType: " + carUpdateVO.getNewEnergyType());
        System.out.println("userId: " + carUpdateVO.getUserId());

        // 调用 BasicOperation 的方法
        BasicOperation basicOperation = new BasicOperation();
        basicOperation.updateCar(
                carUpdateVO.getCarName(),
                carUpdateVO.getMinPrice(),
                carUpdateVO.getMaxPrice(),
                carUpdateVO.getRating(),
                carUpdateVO.getImage(),
                carUpdateVO.getType(),
                carUpdateVO.getEnergyType(),
                carUpdateVO.getNewType(),
                carUpdateVO.getNewEnergyType(),
                carUpdateVO.getUserId()
        );
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
