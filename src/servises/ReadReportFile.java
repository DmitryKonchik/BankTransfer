package servises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadReportFile {

    private static final String PATH_TO_REPORT_FILE = "src/files/report.txt";
    Scanner scanner = new Scanner(System.in);

    public void printReportFile() {
        // вывод из файла отчета всех записей
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_REPORT_FILE))) {
            String s;
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with report");
        }
    }

    public void printReportFileByDate() {
        // вывод из файла отчета по дате
        Pattern pattern = Pattern.compile("Date: \\d{4}-\\d{2}-\\d{2}");

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_REPORT_FILE))) {
            String line;
            LocalDate dateReportFrom;
            LocalDate dateReportTo;

            System.out.println("Enter date start format yyyy-MM-dd");
            dateReportFrom = LocalDate.parse(checkDate(scanner.nextLine()));
            System.out.println("Enter date finish format yyyy-MM-dd");
            dateReportTo = LocalDate.parse(checkDate(scanner.nextLine()));

            int count = 0;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String date = matcher.group();
                    LocalDate dateReport = LocalDate.of(Integer.parseInt(date.substring(6, 10)), Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)));
                    if (dateReport.isAfter(dateReportFrom) || dateReport.isEqual(dateReportFrom) && dateReport.isBefore(dateReportTo) || dateReport.isEqual(dateReportTo)) {
                        System.out.println(line);
                        count++;
                    }
                }
            }
            if (count == 0) {
                System.out.println("No matches");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with report");
        }
    }

    private String checkDate(String str) {
        //проверка даты введенной в консоли
        Pattern patternConsoleDate = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = patternConsoleDate.matcher(str);
        if (!matcher.find()) {
            System.out.println("Enter date again with correct format yyyy-MM-dd");
            str = scanner.nextLine();
            checkDate(str);
        }
        return str;
    }
}
