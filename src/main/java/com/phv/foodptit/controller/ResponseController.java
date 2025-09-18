package com.phv.foodptit.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phv.foodptit.entity.Response;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.service.ResponseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;
    @PostMapping("/response")
    public DataResponse createNewResponse(@RequestBody Response response)
    {
        return this.responseService.createNewResponse(response);
    }
    @GetMapping("/api/v1/admin/response")
    public DataResponse getAllResponse(@RequestParam String page,@RequestParam String limit){
        Pageable pageable = PageRequest.of(Integer.parseInt(page)-1, Integer.parseInt(limit), Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.responseService.getAllResponse(pageable);
    }


}
