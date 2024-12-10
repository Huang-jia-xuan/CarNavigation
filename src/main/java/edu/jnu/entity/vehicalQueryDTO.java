package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class vehicalQueryDTO {
    private List<String> type;
    private List<String> energyType;
    private int minPrice;
    private int maxPrice;
    private String sortBy;

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
