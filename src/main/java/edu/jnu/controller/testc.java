package edu.jnu.controller;
import edu.jnu.entity.Do;
import edu.jnu.entity.VehicleQueryDTO;
import edu.jnu.service.VehicleQueryService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class testc {
    @Autowired
    VehicleQueryService vehicleQueryService;
    @GetMapping("/test")
    public ResponseEntity<ArrayList<Do>> test(){
        JSONObject jsonObject = new JSONObject().put("type","sedan").put("energyType","oil").put("minPrice",0).put("maxPrice",10000000).put("sortBy","ASK");
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
