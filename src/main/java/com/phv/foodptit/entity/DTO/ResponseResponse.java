package com.phv.foodptit.entity.DTO;

import java.util.List;

import com.phv.foodptit.entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResponse {
    private List<Response> list;
    private long currentPage;
    private long totalPage;
    private long total;
}
