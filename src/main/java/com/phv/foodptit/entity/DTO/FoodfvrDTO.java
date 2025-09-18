package com.phv.foodptit.entity.DTO;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodfvrDTO {
    private long id;
    private String name;
    @Column(length = 1000)
    private List<String>images = new ArrayList<>();
}
