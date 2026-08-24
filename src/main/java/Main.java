import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args){

        // Fertig, wenn: Request an echte API wird gesendet(check),
        //               JSON-Antwort wird korrekt geparst(check),
        //               Wetterdaten werden lesbar angezeigt(check),
        //               Fehler werden abgefangen(check).

        Scanner scanner = new Scanner(System.in);

        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");

        System.out.print("Von welcher Stadt möchtest du das Wetter wissen?: ");
        String stadt = scanner.nextLine();
        String encoded = URLEncoder.encode(stadt, StandardCharsets.UTF_8);


        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + encoded + "&units=metric&appid=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                JSONObject obj = new JSONObject(response.body());

                String name = obj.getString("name");

                String beschreibung = "";
                JSONArray weather = obj.getJSONArray("weather");
                for(int i = 0; i < weather.length(); i++){
                    JSONObject details = weather.getJSONObject(i);
                    beschreibung = details.getString("description");
                }

                JSONObject main = obj.getJSONObject("main");
                double temp = main.getDouble("temp");
                int pressure = main.getInt("pressure");
                int humidity = main.getInt("humidity");
                int sea_level = main.getInt("sea_level");
                int grnd_level = main.getInt("grnd_level");
                String meeresspiegel = "";
                int hoehe;
                if(sea_level < grnd_level){
                    hoehe = grnd_level - sea_level;
                    meeresspiegel = name + " liegt " + hoehe + "m über dem Meerespiegel.";
                }
                else if(sea_level > grnd_level){
                    hoehe = sea_level - grnd_level;
                    meeresspiegel = name + " liegt " + hoehe + "m unter dem Meerespiegel. \n";
                }
                else{
                    meeresspiegel = name + " liegt auf der Höhe des Meeresspiegel. \n";
                }

                int visibility = obj.getInt("visibility");

                JSONObject wind = obj.getJSONObject("wind");
                double speed = wind.getDouble("speed");
                int deg = wind.getInt("deg");
                String windRichtung = "";
                if(deg == 0 || deg == 360){
                    windRichtung = "Nord";
                }
                else if(deg == 90){
                    windRichtung = "Ost";
                }
                else if(deg == 180){
                    windRichtung = "Süd";
                }
                else if(deg == 270){
                    windRichtung = "West";
                }
                else if(deg > 0 && deg < 90){
                    windRichtung = "NordOst";
                }
                else if(deg > 90 && deg < 180){
                    windRichtung = "SüdOst";
                }
                else if(deg > 180 && deg < 270){
                    windRichtung = "SüdWest";
                }
                else if(deg > 270 && deg < 360){
                    windRichtung = "NordWest";
                }



                System.out.println("In " + name + " gibt es " + beschreibung + ".\n" +
                        "Die Temperatur beträgt " + temp + " Grad Celcius, der Luftdruck ist " + pressure + " Pascal,\n" +
                        "die Luftfeuchtigkeit beträgt " + humidity + "%. " + meeresspiegel +
                        "Man kann " + visibility + "m weit sehen und der Wind hat eine Geschwindigkeit von " + speed + "kmh und der Wind kommt aus " + windRichtung + " Richtung.");
            }
            else{
                System.out.println("Fehler-Statuscode: " + response.statusCode());
            }
            
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