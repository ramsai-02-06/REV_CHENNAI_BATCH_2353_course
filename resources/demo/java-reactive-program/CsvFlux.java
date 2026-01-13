import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

public class CsvFlux {

    public List<String> readFile(String filename){

        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(filename)
             ) {

            if (inputStream == null) {
                throw new FileNotFoundException("File not found in resources: " + filename);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    public Flux<String> readFileRx(String filename){
        return Flux.defer(() -> {
            try {
                InputStream inputStream = getClass().getClassLoader()
                        .getResourceAsStream(filename);

                if (inputStream == null) {
                    return Flux.error(
                            new FileNotFoundException("File not found in resources: " + filename)
                    );
                }

                // Convert InputStream to Stream<String>
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream)
                );

                Stream<String> lines = reader.lines();
                //success
                return Flux.fromStream(lines)
                        .doFinally(signal -> {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                System.err.println("Error closing reader: " + e.getMessage());
                            }
                        });

            } catch (Exception e) { //error
                return Flux.error(e);
            }
        });
    }

}