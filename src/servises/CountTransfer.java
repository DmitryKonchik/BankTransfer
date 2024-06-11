package servises;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
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
    private static final String PATH_TO_INPUT_FOLDER = "src/files/input";
    private static final String PATH_TO_REPORT_FILE = "src/files/report.txt";
    private static final String PATH_TO_ARCHIVE_FOLDER = "src/files/archive/";
    private static final String PATTERN_TO_CHECK_TRANSACTION_LINE = "from:[0-9]{5}-[0-9]{5} to:\\d{5}-\\d{5} \\| -?\\d+\\.?\\d*";

    private ParseInfoAboutCount parseInfoAboutCount = new ParseInfoAboutCount();
    private Map<String, Double> mapOfCounts = parseInfoAboutCount.getCounts();

    public void parseTransactionFiles() {
        // the method looks to see if there are files in the transactions folder suitable for conducting transactions and processes all files through a loop
        File inputFolder = new File(PATH_TO_INPUT_FOLDER);
        File[] files = inputFolder.listFiles();
        if (files != null) { // check if there are suitable files
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    readAndTransaction(file);
                    moveFileToArchive(file); // move the transaction file to the archive
                } else {
                    moveFileToArchive(file);
                }
            }
            System.out.println("All transaction successful");
        }
        parseInfoAboutCount.parseInfoAboutCountsToFile();
    }

    private void readAndTransaction(File file) {
        // We perform a transaction by reading data from a separate file
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
        // method for transaction

        BankCount countToTransaction; // The account to which we transfer
        BankCount countFromTransaction; // Account from which we transfer
        String resultOfTransaction; // transaction result

        Pattern pattern = Pattern.compile(PATTERN_TO_CHECK_TRANSACTION_LINE);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) { // checking the correctness of the entered string in the transaction file
            line = matcher.group();
            if (isCountExist(line.substring(5, 16)) && isCountExist(line.substring(20, 31))) {
                // Checking whether such accounts exist
                countFromTransaction = findCount(line.substring(5, 16));// create an account from which money is transferred
                countToTransaction = findCount(line.substring(20, 31));// create an account to which money is transferred
                double money = Double.parseDouble(line.substring(34)); // from the transaction file we assign the value of the transferred money to a variable
                if (money > 0.0) { // checking whether the amount to be transferred is positive
                    if (countFromTransaction.isEnoughMoneyOnBalance(money)) {// we check whether there is enough money in the account from which we are transferring money
                        countToTransaction.addMoney(money); // adding money to your account
                        countFromTransaction.withdrawMoney(money); // take money from the account
                        resultOfTransaction = "Successful transaction";
                        // write data to MAP after the transaction
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
        // writing a report to a file after a transaction
        parseReport(file, resultOfTransaction, line);
    }

    public boolean isCountExist(String count) {
        // Check whether such an account exists in the file that defines the name and balance of the accounts
        return mapOfCounts.containsKey(count);
    }

    public BankCount findCount(String count) {
        // Defines an invoice class from an invoice file for further work with the invoice
        BankCount newBankCount = null;
        for (Map.Entry e : mapOfCounts.entrySet()) {
            if (e.getKey().equals(count)) {
                newBankCount = new BankCount((String) e.getKey(), (Double) e.getValue());
            }
        }
        return newBankCount;
    }

    public void parseCountToMap(BankCount count) {
        // We convert the bank account class into MAP for transfer to a file
        mapOfCounts.put(count.getName(), count.getBalance());
    }

    public void moveFileToArchive(File file) {
        // method for moving a file to an archive
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSS");
        try {
            Files.move(Paths.get(file.getPath()), Paths.get(PATH_TO_ARCHIVE_FOLDER + LocalDateTime.now().format(formatter) + file.getName()));
        } catch (IOException e) {
            System.out.println("Exception while moving file: " + e.getMessage());
        }
    }

    public void parseReport(File file, String resultOfTransaction, String line) {
        // recording a transaction report in a file

        List<String> listCountName = findAllCounts(line);

        // If there are no two matches, two empty values are added to the end to prevent an error.
        listCountName.add(null);
        listCountName.add(null);

        try (FileWriter report = new FileWriter(PATH_TO_REPORT_FILE, true)) {
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
