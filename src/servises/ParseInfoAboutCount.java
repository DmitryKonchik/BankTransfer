package servises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParseInfoAboutCount {

    private Map<String, Double> counts = new HashMap<>();
    private final String pathToFile = "src/files/CountsInfo.txt";

    public Map<String, Double> getCounts() {
        return counts;
    }

    public ParseInfoAboutCount() {
        parseInfoAboutCountsFromFile();
    }

    public void parseInfoAboutCountsFromFile() {
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
