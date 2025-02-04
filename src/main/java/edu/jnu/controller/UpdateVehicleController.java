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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<JSONObject> updateVehicle(@RequestBody String jsonString) {
        System.out.println(jsonString);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        // 解析 JSON 为 VO 对象
        CarUpdateVO carUpdateVO = VehicleUpdateService.parseFromJson(jsonObject);

        JSONObject response = new JSONObject();

        try {
            // 调用更新方法
            VehicleUpdateService.updateCar(carUpdateVO);
            // 如果执行没有异常，则返回成功
            response.put("success", true);
            response.put("message", "车辆信息更新成功！");
        } catch (Exception e) {
            // 捕获异常并返回失败消息
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "车辆信息更新失败：" + e.getMessage());
        }

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/insert")
    public ResponseEntity<JSONObject> insertVehicle(@RequestBody String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarInsertVO carInsertVO = VehicleUpdateService.parseFromJson2(jsonObject);

        JSONObject response = new JSONObject();

        try {
            VehicleUpdateService.insertCar(carInsertVO);
            response.put("success", true);
            response.put("message", "车辆信息添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "车辆信息添加失败：" + e.getMessage());
        }

        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/delete")
    public ResponseEntity<JSONObject> deleteVehicle(@RequestBody String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        CarDeleteVO carDeleteVO = VehicleUpdateService.parseFromJson3(jsonObject);

        JSONObject response = new JSONObject();

        try {
            VehicleUpdateService.deleteCar(carDeleteVO);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
