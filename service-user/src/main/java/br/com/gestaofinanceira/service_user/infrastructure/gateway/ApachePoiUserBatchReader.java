package br.com.gestaofinanceira.service_user.infrastructure.gateway;

import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserBatchReader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApachePoiUserBatchReader implements UserBatchReader {

    @Override
    public List<CreateUserCommand> read(InputStream inputStream) {

        List<CreateUserCommand> commands = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String name = row.getCell(0).getStringCellValue();
                String email = row.getCell(1).getStringCellValue();
                String cpf = row.getCell(2).getStringCellValue();
                LocalDate birthDate =
                        row.getCell(3).getLocalDateTimeCellValue().toLocalDate();
                String passwordHash = row.getCell(4).getStringCellValue();

                CreateUserCommand command = new CreateUserCommand(
                        cpf,
                        name,
                        email,
                        passwordHash,
                        birthDate
                );

                commands.add(command);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo Excel", e);
        }

        return commands;
    }
}
