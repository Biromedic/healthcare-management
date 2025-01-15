package com.management.medicineservice.helper;

import com.management.medicineservice.model.Medicine;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelHelper {

    public List<Medicine> readMedicinesFromExcel(String filePath) {
        List<Medicine> medicines = new ArrayList<>();
        Path path = Paths.get(filePath);

        try {
            long fileSize = Files.size(path);
            if (fileSize > Integer.MAX_VALUE) {
                throw new IOException("File size is too large");
            }

            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 Workbook workbook = new XSSFWorkbook(fileInputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getPhysicalNumberOfRows();

                for (int i = 3; i < rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        Medicine medicine = new Medicine();
                        medicine.setName(getCellValue(row, 0));
                        medicine.setBarcode(getCellValue(row, 1));
                        medicine.setCompanyName(getCellValue(row, 4));
                        medicine.setStatus(getCellValue(row, 6));
                        medicine.setPrice(Integer.parseInt(getCellValue(row, 5)));
                        medicines.add(medicine);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }

        return medicines;
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}