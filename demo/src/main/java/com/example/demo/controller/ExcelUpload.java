package com.example.demo.controller;


import com.example.demo.service.ExlProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController

public class ExcelUpload {
    @Autowired
    ExlProcess process;
          @PostMapping("/upload")
    public ResponseEntity<List<Map<String,Object>>> processExl(@RequestParam(name = "file") MultipartFile file) throws IOException {


              List<Map<String, Object>> exl =  process.upload(file);

              return ResponseEntity.ok(exl);
    }
}
