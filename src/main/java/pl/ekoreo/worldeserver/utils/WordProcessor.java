package pl.ekoreo.worldeserver.utils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordProcessor {

    public static void main(String[] args) {
        // Replace with your input and output file paths
        String inputFilePath = "C:\\Users\\MrMozartOfficial\\Desktop\\marek_noun_freq.txt";
        String outputFilePath = "words.txt";

        List<String> fiveLetterWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2 && parts[0].length() == 5) {
                    fiveLetterWords.add(parts[0].toUpperCase());
                }
                if (fiveLetterWords.size() == 1000) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String word : fiveLetterWords) {
                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

