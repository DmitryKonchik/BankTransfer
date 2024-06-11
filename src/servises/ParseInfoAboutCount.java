package servises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ParseInfoAboutCount {

    private Map<String, Double> counts = new TreeMap<>();
    private final String pathToFile = "src/files/CountsInfo.txt";

    public Map<String, Double> getCounts() {
        return counts;
    }

    public ParseInfoAboutCount() {
        // The constructor calls a method to create a MAP
        parseInfoAboutCountsFromFile();
    }

    public void parseInfoAboutCountsFromFile() {
        // Method for creating MARA from an invoice file
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String s;
            while ((s = br.readLine()) != null) {
                counts.put(s.substring(0, 11), Double.parseDouble(s.substring(14)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with counts info");
        }
    }

    public void parseInfoAboutCountsToFile() {
        // Recording information in the invoice file after transactions
        try (FileWriter fw = new FileWriter(pathToFile)) {
            for (Map.Entry e : counts.entrySet()) {
                fw.write(e.getKey() + " | " + e.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NOT FILE with counts info");
        }
    }
}
