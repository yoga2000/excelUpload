package com.example.demo.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
public class ExlProcess {

    public List<Map<String, Object>> upload(MultipartFile file) throws IOException {
        System.out.println("hi");


        try{
            // convert multipartfile into excel object
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            // get the sheet index
            int sheetIdx = workbook.getActiveSheetIndex();

            // get sheet;
            XSSFSheet sheetToProcess = workbook.getSheetAt(sheetIdx);


            // get the header
            List<String> headers = new ArrayList<>();
            Iterator<Row> rows = sheetToProcess.rowIterator();
            // considerting the first rw as header
            rows.next().forEach(h->headers.add(h.getStringCellValue()));

            // process the remaining rows

            List<Map<String, Object>> rowsResult = new ArrayList<>();
            rows.forEachRemaining(row->{
                Map<String,Object> rowMap = new LinkedHashMap<>();
                for(int i=0;i<row.getPhysicalNumberOfCells();i++){
                    rowMap.put(headers.get(i),row.getCell(i).toString());
                }
                rowsResult.add(rowMap);
            });

     return rowsResult;

        } catch (IOException e){
            throw  new RuntimeException(e);
        }


    }

    public void CellType(Cell cell){
        if(cell == null){
            return;
        }
        if(cell.getCellType()== STRING){
            cell.getStringCellValue();
        }else if(cell.getCellType() == NUMERIC){
            cell.getNumericCellValue();
        }else{
            cell.getBooleanCellValue();
        }

    }
}









package com.example.demo.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExlProcess {

    public List<Map<String, Object>> upload(MultipartFile file) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            // Get the active sheet
            XSSFSheet sheetToProcess = workbook.getSheetAt(workbook.getActiveSheetIndex());

            // Get the header row
            Iterator<Row> rows = sheetToProcess.rowIterator();
            Row headerRow = rows.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell));
            }

            // Process remaining rows
            List<Map<String, Object>> rowsResult = new ArrayList<>();
            rows.forEachRemaining(row -> {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(i), getCellValueAsString(cell));
                }
                rowsResult.add(rowMap);
            });

            return rowsResult;

        } catch (IOException e) {
            throw new RuntimeException("Error processing Excel file", e);
        }
    }

    // Utility method to handle different cell types
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Format as needed
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}




