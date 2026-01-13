import java.util.List;

public class Main {

    public static void main(String[] args) {

        CsvFlux handler = new CsvFlux();

        // using traditional
        List<String> lines = handler.readFile("employees.csv");
        System.out.println(lines);

        // using reactive (Flux)
        handler.readFileRx("employees.csv").subscribe(
                line -> System.out.println("Line: " + line),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed"));

    }
}