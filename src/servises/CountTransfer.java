package servises;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        // метод смотрит есть ли в папке транзакций файлы подходящие для проведения транзакций и через цикл обрабатывает все файлы
        File inputFolder = new File(pathToInputFolder);
        File[] files = inputFolder.listFiles();
        if (files != null) { // проверяем есть ли подходящие файлы
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    readAndTransaction(file);
                    moveFileToArchive(file); // перемещаем файл транзакиии в архив
                } else {
                    moveFileToArchive(file);
                }
            }
            System.out.println("All transaction successful");
        }
        parseInfoAboutCount.parseInfoAboutCountsToFile();
    }

    private void readAndTransaction(File file) {
        //Производим транзакцию считывая с отдельного файла данные
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transaction(line, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with transaction");
        }
    }


    public void transaction(String line, File file) {
        // метод для транзакции

        BankCount countToTransaction; // Счет С которого переводим
        BankCount countFromTransaction; // Счет НА который переводим
        String resultOfTransaction; // результат транзакции

        Pattern pattern = Pattern.compile(patternToCheckTransactionLine);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) { // проверка на правильность введенной строки в файле транзакции
            line = matcher.group();
            if (isCountExist(line.substring(5, 16)) && isCountExist(line.substring(20, 31))) {
                //Проверка существуют ли такие счета
                countFromTransaction = findCount(line.substring(5, 16));// создаем счет с которого переводят деньги
                countToTransaction = findCount(line.substring(20, 31));// создаем счет на который переводят деньги
                double money = Double.parseDouble(line.substring(34)); // из фала транзакции присваиваем переменной значение переводимых денег
                if (money > 0) { // проверка положительная ли сумма для перевода
                    if (countFromTransaction.isEnoughMoneyOnBalance(money)) {// проверяем достаточно ли денег на счете С которого переводим деньги
                        countToTransaction.addMoney(money); // добавление денег на счет
                        countFromTransaction.withdrawMoney(money); // отнимаем деньги со счета
                        resultOfTransaction = "Successful transaction";
                        // записываем данные в МАПу после транзакции
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
            resultOfTransaction = "Invalid transaction string";
        }
        // запись в файл отчет после транзакции

        parseReport(file, resultOfTransaction, line);
    }

    public boolean isCountExist(String count) {
        //Проверяем существет ли такой счет в файле который определяет название и остаток счетов
        boolean res = false;
        for (Map.Entry e : mapOfCounts.entrySet()) {
            if (e.getKey().equals(count)) {
                res = true;
            }
        }
        return res;
    }

    public BankCount findCount(String count) {
        //Опоеделяет класс счета из файла счетов для дальнейшей работы со счетом
        for (Map.Entry e : mapOfCounts.entrySet()) {
            if (e.getKey().equals(count)) {
                return new BankCount((String) e.getKey(), (Double) e.getValue());
            }
        }
        return null;
    }

    public void parseCountToMap(BankCount count) {
        //Преобразуем классс аккаунта банка в мап для передачи в файл
        mapOfCounts.put(count.getName(), count.getBalance());
    }

    public void moveFileToArchive(File file) {
        //метод для перемещения файла в архив
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSS");
        try {
            Files.move(Paths.get(file.getPath()), Paths.get(pathToArchiveFolder + LocalDateTime.now().format(formatter) + file.getName()));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
    }

    public void parseReport(File file, String resultOfTransaction, String line) {
        //запись в файл отчет об транзакции

        List<String> listCountName = findAllCounts(line);

        //Если нет двух совпадений, для того чтобы не выпадала ошибка добавляются два пустых значения в конец
        listCountName.add(null);
        listCountName.add(null);

        try (FileWriter report = new FileWriter(pathToReportFile, true)) {
            report.write("Date: " + LocalDateTime.now() + " | File name: " + file.getName() + " | Transfer from " +
                    listCountName.get(0) + " to " + listCountName.get(1) + " | Result: " + resultOfTransaction + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> findAllCounts(String line) {
        Pattern pattern = Pattern.compile("\\d+-\\d+");
        Matcher matcher = pattern.matcher(line);
        List<String> listCountName = new ArrayList<>();
        while (matcher.find()) {
            listCountName.add(matcher.group());
        }
        return listCountName;
    }
}
