package com.phv.foodptit.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.phv.foodptit.entity.Response;
import com.phv.foodptit.entity.DTO.DataResponse;
import com.phv.foodptit.entity.DTO.ResponseResponse;
import com.phv.foodptit.repository.ResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;

    public DataResponse createNewResponse(Response response)
    {
        try {
            response.setCreatedAt(new Date());
            this.responseRepository.save(response);

            return new DataResponse(HttpStatus.CREATED.value(), "Gửi phản hồi thành công", null);
        } catch (Exception e) {
            // TODO: handle exception
            return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra!", null);
        }
    }
    public DataResponse getAllResponse(Pageable pageable)
    {
        try {
            Page<Response> page= this.responseRepository.findAll(pageable);
            ResponseResponse response= new ResponseResponse(page.getContent(), page.getNumber()+1, page.getTotalPages(),page.getTotalElements());
            return new DataResponse(HttpStatus.OK.value(), null, response);
        } catch (Exception e) {
            // TODO: handle exception
             return new DataResponse(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra!", null);
        }
    }
}
