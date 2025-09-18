package com.phv.foodptit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Map data= cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return data.get("secure_url").toString();
    }
     public List<String> uploadMultipleFiles(MultipartFile[] files) throws IOException {
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            Map data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            results.add(data.get("secure_url").toString());
        }
        return results;
    }
}