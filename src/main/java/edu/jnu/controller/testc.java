package edu.jnu.controller;
import edu.jnu.entity.Do;
import edu.jnu.entity.VehicleQueryDTO;
import edu.jnu.service.VehicleQueryService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class testc {
    @Autowired
    VehicleQueryService vehicleQueryService;
    @GetMapping("/test")
    public ResponseEntity<ArrayList<Do>> test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type","sedan");
        jsonObject.put("energyType","oil");
        jsonObject.put("minPrice",0);
        jsonObject.put("maxPrice",10000000);
        jsonObject.put("sortBy","ASK");
        VehicleQueryDTO vehicleQueryDTO = VehicleQueryService.parseFromJson(jsonObject);

        ArrayList<Do> result = vehicleQueryService.queryVehicleFromDataBase(vehicleQueryDTO);
        if(!result.isEmpty()){
            return ResponseEntity.ok().body(result);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
