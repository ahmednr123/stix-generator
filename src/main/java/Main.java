import com.stixloggen.StixGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(new FileReader("C:\\Users\\inc-611\\Downloads\\Send-Archive\\logs.txt"));

        String line;
        ArrayList<String> Logs = new ArrayList<>();
        while((line = bufferedReader.readLine()) != null) {
            Logs.add(line);
        }

        StixGenerator stixGenerator = new StixGenerator(Logs);
        stixGenerator.saveBundleToFile(new File("C:\\Users\\inc-611\\Downloads\\Send-Archive\\bundle-test-2.json"));
    }
}
