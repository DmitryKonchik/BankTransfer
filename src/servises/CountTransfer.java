package servises;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountTransfer {
    private String pathToInputFolder = "src/files/input";
    private String pathToReportFile = "src/files/report.txt";
    private String pathToArchiveFolder = "src/files/archive/";
    private String patternToCheckTransactionLine = "from:[0-9]{5}-[0-9]{5} to:\\d{5}-\\d{5} \\| -?\\d+\\.?\\d*";

    private ParseInfoAboutCount parseInfoAboutCount = new ParseInfoAboutCount();
    private Map<String, Double> mapOfCounts = parseInfoAboutCount.getCounts();


    public void parseTransactionFiles() {
        File inputFolder = new File(pathToInputFolder);
        File[] files = inputFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    readAndTransaction(file);
                    moveFileToArchive(file);
                } else {
                    moveFileToArchive(file);
                }
            }
        }
        parseInfoAboutCount.parseInfoAboutCountsToFile();
    }

    private void readAndTransaction(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tranzaktion(line, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with transaction");
        }
    }


    public void tranzaktion(String line, File file) {

        BankCount countToTransaction;
        BankCount countFromTransaction;
        String resultOfTransaction;

        Pattern pattern = Pattern.compile(patternToCheckTransactionLine);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            line = matcher.group();
            if (isCountExist(line.substring(5, 16)) && isCountExist(line.substring(20, 31))) {
                countFromTransaction = findCount(line.substring(5, 16));
                countToTransaction = findCount(line.substring(20, 31));
                double money = Double.parseDouble(line.substring(34));
                if (money > 0) {
                    if (countFromTransaction.isEnoughMoneyOnBalance(money)) {
                        countToTransaction.addMoney(money);
                        countFromTransaction.withdrawMoney(money);
                        resultOfTransaction = "Successful transaction";
                        parseCountToMap(countFromTransaction);
                        parseCountToMap(countToTransaction);

                    } else {
                        resultOfTransaction = "Not enough money";
                    }
                } else {
                    resultOfTransaction = "The amount is incorrect";
                }
            } else {
                resultOfTransaction = "No such account exists";
            }
        } else {
            resultOfTransaction = "Invalid string value";
        }
        parseReport(file, resultOfTransaction, line.substring(5, 16), line.substring(20, 31));
    }

    public boolean isCountExist(String count) {
        boolean res = false;
        for (Map.Entry e : mapOfCounts.entrySet()) {
            if (e.getKey().equals(count)) {
                res = true;
            }
        }
        return res;
    }

    public BankCount findCount(String count) {
        for (Map.Entry e : mapOfCounts.entrySet()) {
            if (e.getKey().equals(count)) {
                return new BankCount((String) e.getKey(), (Double) e.getValue());
            }
        }
        return null;
    }

    public void parseCountToMap(BankCount count) {
        mapOfCounts.put(count.getName(), count.getBalance());
    }

    public void moveFileToArchive(File file) {  //метод для перемещения файла в архив
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSS");
        try {
            Files.move(Paths.get(file.getPath()), Paths.get(pathToArchiveFolder + LocalDateTime.now().format(formatter) + file.getName()));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
    }

    public void parseReport(File file, String resultOfTransaction, String countNameFrom, String countNameTo) {
        try (FileWriter report = new FileWriter(pathToReportFile, true)) {
            report.write("Date: " + LocalDateTime.now() + " | File name: " + file.getName() + " | Transfer from " +
                    countNameFrom + " to " + countNameTo + " | Result: " + resultOfTransaction + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
