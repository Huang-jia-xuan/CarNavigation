package edu.jnu.controller;
import edu.jnu.entity.Do;
import edu.jnu.entity.VehicleQueryByIdDTO;
import edu.jnu.entity.VehicleQueryDTO;
import edu.jnu.service.VehicleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.ArrayList;

@RestController
@Slf4j
public class QueryController {
    @Autowired
    private VehicleQueryService vehicleQueryService;

    @PostMapping("/query")
    public ResponseEntity<ArrayList<Do>> queryVehicle(@RequestBody JSONObject jsonObject){
        VehicleQueryDTO vehicleQueryDTO = VehicleQueryService.parseFromJson(jsonObject);
        ArrayList<Do> result = vehicleQueryService.queryVehicleFromDataBase(vehicleQueryDTO);
        if(!result.isEmpty()){
            return ResponseEntity.ok().body(result);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/queryById")
    public ResponseEntity<Do> queryVehicleById(@RequestBody JSONObject jsonObject){
        VehicleQueryByIdDTO vehicleQueryByIdDTO = vehicleQueryService.parseFromJson2(jsonObject);
        Do result = vehicleQueryService.queryVehicleFromDataBaseById(vehicleQueryByIdDTO);
        if(result!=null){
            return ResponseEntity.ok().body(result);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
