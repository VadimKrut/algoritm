package com.vado.sic.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    public static void createExcelFile(String fileName, String sheetName, List<List<String>> data) {

        String resourceFolder = "src/main/resources/exel/";
        String filePath = resourceFolder + fileName + ".xlsx";

        File folder = new File(resourceFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Алгоритм", "Длинна зашифрованной информации в байтах", "Длинна расшифрованной информации в байтах", "Время шифрования в мс", "Время дешифрования в мс", "Время выполнения в целом в мс"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                List<String> rowData = data.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }
            workbook.write(fileOut);
            System.out.println("Excel файл успешно создан!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}