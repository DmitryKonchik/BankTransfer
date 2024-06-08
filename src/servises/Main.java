package servises;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        CountTransfer transfer = new CountTransfer();
        ReadReportFile readReportFile = new ReadReportFile();
        Scanner scanner = new Scanner(System.in);

        System.out.println("What do you want to do? \n" +
                "1: make a transaction \n" +
                "2: output report file\n" +
                "3: output report file by date\n" +
                "4: finish program");
        boolean isTrue = true;

        while (isTrue) {
            String variant = scanner.nextLine();
            switch (variant) {
                case "1":
                    transfer.parseTransactionFiles();
                    break;
                case "2":
                    readReportFile.printReportFile();
                    break;
                case "3":
                    readReportFile.printReportFileByDate();
                    break;
                case "4":
                    isTrue = false;
                    break;
                default:
                    System.out.println("Enter another option");
            }
        }
    }
}