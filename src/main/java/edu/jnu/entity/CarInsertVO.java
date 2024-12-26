package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarInsertVO {
    private String type;
    private String energyType;
    private String carName;
    private double minPrice;
    private double maxPrice;
    private double rating;
    private String image;
    private int userId;

}
