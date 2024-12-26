package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarUpdateVO {
    String carName;
    Double minPrice;
    Double maxPrice;
    Double rating;
    String image;
    String type;
    String energyType;
    String newType;
    String newEnergyType;
    int userId;
}
