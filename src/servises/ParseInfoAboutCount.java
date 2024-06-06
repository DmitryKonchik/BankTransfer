package servises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ParseInfoAboutCount {

    private Map<String, Double> counts;
    private final String pathToFile = "src/files/CountsInfo.txt";

    public Map<String, Double> getCounts() {
        return counts;
    }

    public void setCounts(Map<String, Double> counts) {
        this.counts = counts;
    }

    public void parseInfoAboutCountsFromFile () {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String s;
            while ((s = br.readLine()) != null) {
                counts.put(s.substring(0, 11), Double.parseDouble(s.substring(14)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseInfoAboutCountsToFile() {
        try (FileWriter fw = new FileWriter(pathToFile)){
            for (Map.Entry e : counts.entrySet()) {
                fw.write(e.getKey() + " | " + e.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
