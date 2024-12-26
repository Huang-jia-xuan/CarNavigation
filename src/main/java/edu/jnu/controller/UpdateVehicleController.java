package edu.jnu.controller;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.entity.CarDeleteVO;
import edu.jnu.entity.CarInsertVO;
import edu.jnu.entity.CarUpdateVO;
import edu.jnu.service.VehicleQueryService;
import edu.jnu.service.VehicleUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
public class UpdateVehicleController {
    @Autowired
    private VehicleUpdateService vehicleUpdateService;

    @PostMapping("/updateVehicle")
    public ResponseEntity<String> updateVehicle(@RequestBody String jsonString){
        System.out.println(jsonString);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarUpdateVO carUpdateVO = VehicleUpdateService.parseFromJson(jsonObject);
        VehicleUpdateService.updateCar(carUpdateVO);
        return ResponseEntity.ok().body("完成操作，但是不知道是否成功，因为下一层的代码返回的是void");
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertVehicle(@RequestBody String jsonString){
        System.out.println(jsonString);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarInsertVO carInsertVO = VehicleUpdateService.parseFromJson2(jsonObject);
        VehicleUpdateService.insertCar(carInsertVO);
        return ResponseEntity.ok().body("none");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteVehicle(@RequestBody String jsonString){
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarDeleteVO carDeleteVO = VehicleUpdateService.parseFromJson3(jsonObject);
        VehicleUpdateService.deleteCar(carDeleteVO);
        return ResponseEntity.ok().body("none");
    }
}
