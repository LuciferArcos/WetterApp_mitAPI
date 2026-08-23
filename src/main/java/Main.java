import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args){
        // Fertig, wenn: Request an echte API wird gesendet,
        //               JSON-Antwort wird korrekt geparst,
        //               Wetterdaten werden lesbar angezeigt,
        //               Fehler werden abgefangen.

        Scanner scanner = new Scanner(System.in);

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");

        System.out.print("Von welcher Stadt möchtest du das Wetter wissen?: ");
        String stadt = scanner.nextLine();
        String encoded = URLEncoder.encode(stadt, StandardCharsets.UTF_8);


        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + stadt + "&appid=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject obj = new JSONObject(response);

            //String base = obj.getString("weather");
            //int temp = obj.getInt();


            System.out.println(response.statusCode());
            System.out.println(response.body());
        }
        catch(InterruptedException e){
            System.out.println("Unterbrochen");
        }
        catch(IOException e){
            System.out.println("Fehler");
        }

        scanner.close();

    }
}