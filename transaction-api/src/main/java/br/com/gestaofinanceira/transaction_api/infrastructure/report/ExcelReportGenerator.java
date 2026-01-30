package br.com.gestaofinanceira.transaction_api.infrastructure.report;

import br.com.gestaofinanceira.transaction_api.application.usecase.ReportGeneratorUseCase;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelReportGenerator implements ReportGeneratorUseCase {

    @Override
    public byte[] generate(List<Transaction> transactions) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Transactions");
            Row header = sheet.createRow(0);

            String[] cols = {
                    "Date", "Type", "Category", "Amount", "Status"
            };

            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            int rowIdx = 1;
            for (Transaction t : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(t.getCreatedAt().toString());
                row.createCell(1).setCellValue(t.getType().name());
                row.createCell(2).setCellValue(t.getCategory().name());
                row.createCell(3).setCellValue(t.getOriginalAmount().getAmount().doubleValue());
                row.createCell(4).setCellValue(t.getStatus().toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
}
