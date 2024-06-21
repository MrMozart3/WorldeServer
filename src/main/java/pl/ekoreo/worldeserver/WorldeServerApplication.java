package pl.ekoreo.worldeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.ekoreo.worldeserver.games.wordle.WordleBoard;
import pl.ekoreo.worldeserver.games.wordle.WordleUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@SpringBootApplication
public class WorldeServerApplication {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static void main(String[] args) {
        System.out.println(countLines(new File("C:\\Users\\MrMozartOfficial\\IdeaProjects\\WorldeServer\\src\\main\\java\\pl\\ekoreo\\worldeserver"))  + " Lines Currently");
        SpringApplication.run(WorldeServerApplication.class, args);
    }
    public static int countLines(File file) {
        int lines = 0;

        // Check if the file is a directory
        if (file.isDirectory()) {
            // Iterate over all files and directories inside the directory
            for (File f : Objects.requireNonNull(file.listFiles())) {
                // Recursively call countLines method for each file/directory
                lines += countLines(f);
            }
        } else {
            // If the file is a Java file, count its lines
            if (file.getName().endsWith(".java")) {
                try {
                    lines += Files.readAllLines(file.toPath()).size();
                } catch (IOException e) {
                    // Handle IOException if file cannot be read
                }
            }
        }
        return lines;
    }
}
