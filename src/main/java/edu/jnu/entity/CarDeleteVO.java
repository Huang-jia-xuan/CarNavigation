package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDeleteVO {
    private int carId;
    private String type;
    private String energyType;
    private int userId;
}
