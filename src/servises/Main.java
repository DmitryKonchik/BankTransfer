package servises;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String line = "from:11111-11111 to:55555-55555 | 100.12 ";

//        Pattern patern = Pattern.compile("from:[0-9]{5}-[0-9]{5} to:\\d{5}-\\d{5} \\| \\d+\\.?\\d*");
//
//        Matcher matcher = patern.matcher(line);

//        if (matcher.find()){

            System.out.println(line.substring(5, 16));
            System.out.println(line.substring(20, 31));
            System.out.println(line.substring(34));
//        }



    }
}