import java.io.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class CsvPublisherSubscriber {

    // Method that reads CSV file and emits each row (Publisher)
    public Flow.Publisher<String> readCsvFile(String filename) {
        return subscriber -> {
            new Thread(() -> {
                SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
                publisher.subscribe(subscriber);

                try {
                    InputStream inputStream = getClass().getClassLoader()
                            .getResourceAsStream(filename);

                    if (inputStream == null) {
                        publisher.closeExceptionally(
                                new FileNotFoundException("CSV file not found: " + filename)
                        );
                        return;
                    }

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream)
                    );

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Publishing: " + line);
                        publisher.submit(line);
                        Thread.sleep(300);  // Simulate processing delay
                    }

                    reader.close();
                    publisher.close();

                } catch (IOException | InterruptedException e) {
                    publisher.closeExceptionally(e);
                }
            }).start();
        };
    }

    public static void main(String[] args) throws InterruptedException {
        CsvPublisherSubscriber demo = new CsvPublisherSubscriber();

        // Create and subscribe to CSV file reader
        demo.readCsvFile("employees.csv").subscribe(new Flow.Subscriber<String>() {
            private Flow.Subscription subscription;
            private int rowCount = 0;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                System.out.println("Subscribed to CSV stream!\n");
                subscription.request(Long.MAX_VALUE);  // Request all items
            }

            @Override
            public void onNext(String row) {
                rowCount++;
                System.out.println("Row " + rowCount + " received: " + row);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("\nCSV stream completed! Total rows: " + rowCount);
            }
        });

        // Wait for async processing to complete
        Thread.sleep(10000);
    }
}