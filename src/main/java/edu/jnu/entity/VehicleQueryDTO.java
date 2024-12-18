package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleQueryDTO {
    private String type;
    private String energyType;
    private int minPrice;
    private int maxPrice;
    private String sortBy;
    private String sortOrder;
    @Override
    public String toString() {
        return "VehicleSearchCriteria{" +
                "type=" + type +
                ", energyType=" + energyType +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", sortBy='" + sortBy + '\'' +
                '}';
    }
}
