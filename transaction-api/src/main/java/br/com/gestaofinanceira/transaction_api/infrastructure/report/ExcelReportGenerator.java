package br.com.gestaofinanceira.transaction_api.infrastructure.report;

import br.com.gestaofinanceira.transaction_api.application.usecase.ReportGeneratorUseCase;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ExcelReportGenerator implements ReportGeneratorUseCase {

    private static final Set<TransactionType> OUTGOING_TYPES = EnumSet.of(
            TransactionType.WITHDRAW,
            TransactionType.PAYMENT,
            TransactionType.PURCHASE,
            TransactionType.EXTERNAL_EXPENSE
    );

    @Override
    public byte[] generate(List<Transaction> transactions) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Transactions");

            // ================= STYLES =================
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle moneyStyle = createMoneyStyle(workbook);
            CellStyle textStyle = createTextStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);

            int rowIdx = 0;

            // ================= TITLE =================
            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Relatório de Transações");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            rowIdx++; // espaço

            // ================= HEADER =================
            Row header = sheet.createRow(rowIdx++);
            String[] columns = {
                    "Data", "Tipo", "Categoria", "Valor", "Moeda", "Status"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // ================= DATA =================
            double totalIn = 0.0;
            double totalOut = 0.0;

            for (Transaction t : transactions) {
                Row row = sheet.createRow(rowIdx++);

                // Data
                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(
                        t.getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                );
                dateCell.setCellStyle(dateStyle);

                // Tipo
                Cell typeCell = row.createCell(1);
                typeCell.setCellValue(t.getType().name());
                typeCell.setCellStyle(textStyle);

                // Categoria
                Cell catCell = row.createCell(2);
                catCell.setCellValue(t.getCategory().name());
                catCell.setCellStyle(textStyle);

                // Valor
                double amount = t.getOriginalAmount().getAmount().doubleValue();
                Cell valueCell = row.createCell(3);
                valueCell.setCellValue(amount);
                valueCell.setCellStyle(moneyStyle);

                // Moeda
                Cell currencyCell = row.createCell(4);
                currencyCell.setCellValue(t.getOriginalAmount().getCurrency().getCurrencyCode());
                currencyCell.setCellStyle(textStyle);

                // Status
                Cell statusCell = row.createCell(5);
                statusCell.setCellValue(t.getStatus().name());
                statusCell.setCellStyle(textStyle);

                // Totais
                if (t.getType() == TransactionType.DEPOSIT) {
                    totalIn += amount;
                } else if (OUTGOING_TYPES.contains(t.getType())) {
                    totalOut += amount;
                }
            }

            rowIdx++; // espaço

            // ================= TOTALS =================
            Row totalInRow = sheet.createRow(rowIdx++);
            totalInRow.createCell(2).setCellValue("Total Entradas");
            totalInRow.createCell(3).setCellValue(totalIn);
            totalInRow.getCell(3).setCellStyle(totalStyle);

            Row totalOutRow = sheet.createRow(rowIdx++);
            totalOutRow.createCell(2).setCellValue("Total Saídas");
            totalOutRow.createCell(3).setCellValue(totalOut);
            totalOutRow.getCell(3).setCellStyle(totalStyle);

            Row totalRow = sheet.createRow(rowIdx++);
            totalRow.createCell(2).setCellValue("Saldo");
            totalRow.createCell(3).setCellValue(totalIn - totalOut);
            totalRow.getCell(3).setCellStyle(totalStyle);

            // ================= COLUMN SIZE =================
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ================= OUTPUT =================
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório Excel", e);
        }
    }

    // ================= STYLES =================

    private CellStyle createTitleStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createHeaderStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTextStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(
                wb.getCreationHelper()
                        .createDataFormat()
                        .getFormat("dd/MM/yyyy HH:mm")
        );
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createMoneyStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setDataFormat(
                wb.getCreationHelper()
                        .createDataFormat()
                        .getFormat("R$ #,##0.00")
        );
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTotalStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true);

        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(
                wb.getCreationHelper()
                        .createDataFormat()
                        .getFormat("R$ #,##0.00")
        );
        return style;
    }
}
