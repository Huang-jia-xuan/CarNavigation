package edu.jnu.controller;

import com.alibaba.fastjson2.JSONObject;
import edu.jnu.entity.CarUpdateVO;
import edu.jnu.service.VehicleQueryService;
import edu.jnu.service.VehicleUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
public class UpdateVehicleController {
    @Autowired
    private VehicleUpdateService vehicleUpdateService;

    @PostMapping("/updateVehicle")
    public ResponseEntity<String> updateVehicle(String jsonString){
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarUpdateVO carUpdateVO = VehicleUpdateService.parseFromJson(jsonObject);
        VehicleUpdateService.updateCar(carUpdateVO);
        return ResponseEntity.ok().body("完成操作，但是不知道是否成功，因为下一层的代码返回的是void");
    }
}
