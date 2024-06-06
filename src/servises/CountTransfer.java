package servises;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountTransfer {
    private String resultOfTransaction;
    private String pathToInputFolder = "src/files/input";
    private String pathToArchiveFolder = "src/files/archive";
    private BankCount countToTransaction;
    private BankCount countFromTransaction;

    private List<BankCount> bankCounts;
    private ParseInfoAboutCount parseInfoAboutCount = new ParseInfoAboutCount();


    public boolean transferMoney (double money) {
        //TODO
        return false;
    }

    public void initializeBankCounts() {
        parseInfoAboutCount.parseInfoAboutCountsFromFile();
        for (Map.Entry<String, Double> e : parseInfoAboutCount.getCounts().entrySet()) {
            bankCounts.add(new BankCount(e.getKey(), e.getValue()));
        }
    }

    public void parseTranzaktinFiles() {
        File inputFolder = new File(pathToInputFolder);
        File[] files = inputFolder.listFiles();
        if (files != null) {
            for (File file : files) {
             if (file.isFile()) {
                 //transaction
                 //TODO
                moveFileToArchive(file);
             }
            }
        }

    }
    private void readAndProcessFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {


                //метод для берем строку достаем из нее счета и деньги
                //метод для проверок счетов
                //метод для перевода денег
                //метод для записи в файл отчета
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveFileToArchive(File file) {  //метод для перемещения файла в архив
        try {
            Files.move(Paths.get(file.getPath()), Paths.get(pathToArchiveFolder + file.getName()));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
    }

    public void infoAboutTranzaktion(String line) {
        Pattern patern = Pattern.compile("from:[0-9]{5}-[0-9]{5} to:\\d{5}-\\d{5} \\| \\d+\\.?\\d*");
        Matcher matcher = patern.matcher(line);
        if (matcher.find()){
            line = matcher.group();
            if (isCountExist(line.substring(5, 16)) && isCountExist(line.substring(20, 31))) {
                countFromTransaction = findCount(line.substring(5, 16));
                countToTransaction = findCount(line.substring(20, 31));
                if (Double.parseDouble(line.substring(34)) > 0) {
                    if (isEnoughMoney(line)) {

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
    }

    public boolean isCountExist(String count){
        boolean res = false;
        for (BankCount count1 : bankCounts) {
            if (count1.getName().equals(count)) {
                res = true;
            }
        }
        return res;
    }
        public BankCount findCount(String count){
        for (BankCount count1 : bankCounts) {
            if (count1.getName().equals(count)) {
                return count1;
            }
        }
        return null;
    }

    public boolean isEnoughMoney(String line) {
        boolean res = false;
        String count = line.substring(5, 16);
        double money = Double.parseDouble(line.substring(34));
        for (BankCount count1 : bankCounts) {
            if (count1.getName().equals(count)) {
                if (count1.getBalance() - money >= 0) {
                    res = true;
                }
            }
        }
        return res;
    }

    //метод для берем строку достаем из нее счета и деньги
    //метод для проверок счетов
    //метод для перевода денег
    //метод для записи в файл отчета



}
