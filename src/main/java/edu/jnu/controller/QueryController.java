package edu.jnu.controller;
import edu.jnu.entity.Do;
import edu.jnu.entity.VehicleQueryByIdDTO;
import edu.jnu.entity.VehicleQueryDTO;
import edu.jnu.service.VehicleQueryService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.ArrayList;

@RestController
@Slf4j
@CrossOrigin
public class QueryController {
    @Autowired
    private VehicleQueryService vehicleQueryService;

    @PostMapping("/query")
    public ResponseEntity<ArrayList<Do>> queryVehicle(@RequestBody String jsonString){
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        VehicleQueryDTO vehicleQueryDTO = VehicleQueryService.parseFromJson(jsonObject);
        ArrayList<Do> result = vehicleQueryService.queryVehicleFromDataBase(vehicleQueryDTO);
        return ResponseEntity.ok().body(result);

    }
    @PostMapping("/queryById")
    public ResponseEntity<Do> queryVehicleById(@RequestBody String jsonString){
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        VehicleQueryByIdDTO vehicleQueryByIdDTO = VehicleQueryService.parseFromJson2(jsonObject);
        Do result = vehicleQueryService.queryVehicleFromDataBaseById(vehicleQueryByIdDTO);
        if(result!=null){
            return ResponseEntity.ok().body(result);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
