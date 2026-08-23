import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args){
        // Fertig, wenn: Request an echte API wird gesendet,
        //               JSON-Antwort wird korrekt geparst,
        //               Wetterdaten werden lesbar angezeigt,
        //               Fehler werden abgefangen.

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");



        String url = "https://api.openweathermap.org/data/2.5/weather?q=London&appid=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());
        }
        catch(InterruptedException e){
            System.out.println("Unterbrochen");
        }
        catch(IOException e){
            System.out.println("Fehler");
        }

    }
}