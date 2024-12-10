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


package com.example.demo.controller;

import com.example.demo.service.ExlProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/excel")
public class ExcelUpload {

    @Autowired
    private ExlProcess process;

    @PostMapping("/upload")
    public ResponseEntity<?> processExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "File is empty. Please upload a valid file."));
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".xlsx")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid file format. Please upload an .xlsx file."));
            }

            // Process file
            List<Map<String, Object>> processedData = process.upload(file);

            return ResponseEntity.ok(Map.of(
                    "message", "File processed successfully",
                    "data", processedData
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error processing the file", "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An unexpected error occurred", "error", e.getMessage()));
        }
    }
}

