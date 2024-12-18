package edu.jnu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleQueryByIdDTO {
    private int userId;
    private int id;
    private String type;
    private String energyType;

    @Override
    public String toString() {
        return "VehicleSearchCriteria{" +
                "id=" + id +
                "type=" + type +
                ", energyType=" + energyType +
                '}';
    }
}
