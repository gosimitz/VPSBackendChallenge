import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import org.json.JSONArray; //https://github.com/stleary/JSON-java
import org.json.JSONObject;
public class Main {

    /**
     * Main method for managing the logic flow of the Ship/Pilot getter. Uses swapi.dev to query all the Star Wars
     * ships and all of the pilots on each ship.
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String swapiURL = "https://swapi.dev/api/";
        String shipEndPoint = "starships/";
        JSONObject ships = querySwapi(swapiURL + shipEndPoint);

        LinkedList<Ship> allShipInfo = new LinkedList<>();
        int shipCount =  ships.getInt("count");
        //Iterate through the ships
        for(int currShip = 0; currShip <= shipCount; currShip++) {
            //Get the ship info
            JSONObject currShipJSON = querySwapi(swapiURL + shipEndPoint + currShip);
            //TODO Remove when bug solved
            //JSONObject currShipJSON = querySwapi("https://swapi.dev/api/starships/15");
            System.out.println(swapiURL + shipEndPoint + currShip);

            if(currShipJSON.isEmpty()){
                continue;
            }
            String shipName = currShipJSON.getString("name");
            System.out.println("SHIP COUNT "  + shipName);

            //Store the pilots from that ship.
            JSONArray pilotURLArray = currShipJSON.getJSONArray("pilots");

            allShipInfo.add(new Ship(shipName, getPilots(pilotURLArray)));
        }
        printInformation(allShipInfo);
    }

    /**
     * A helper method to retrieve the pilots of the selected ship.
     * @param pilotURLArray - A JSONArray of the URLs of the pilots.
     * @return A LinkedList consisting of the pilots names.
     * @throws IOException
     * @throws InterruptedException
     */
    private static LinkedList<String> getPilots(JSONArray pilotURLArray) throws IOException, InterruptedException {
        LinkedList<String> pilots = new LinkedList<>();
        for (int currPilot = 0; currPilot < pilotURLArray.length(); currPilot++) {
            JSONObject pilotJSON = querySwapi(pilotURLArray.getString(currPilot));
            if(pilotJSON.isEmpty()){
                continue;
            }
            pilots.add(pilotJSON.getString("name"));
        }
        return pilots;
    }
    /**
     * A simple method to run a GET query on swapi.dev
     * HTTP Request code adapted from: https://www.twilio.com/blog/5-ways-to-make-http-requests-in-java
     * @param url - desired endpoint to query
     * @return - A JSONObject representing the body of the response, or an empty JSONObject if a 200 response was not recieved
     * @throws IOException
     * @throws InterruptedException
     */
    private static JSONObject querySwapi(String url) throws IOException, InterruptedException {
        //Create client
        HttpClient client = HttpClient.newHttpClient();

        //Create request
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();

        //Send request
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode() + " " + response);
        if(response.statusCode() != 200) {
            return new JSONObject();
        }
        return new JSONObject(response.body().toString());
    }

    /**
     * A method to print out the information from each ship.
     * @param shipInfo - a LinkedList containing Ship Objects.
     */
    private static void printInformation(LinkedList<Ship> shipInfo) {
        for (Ship ship : shipInfo) {
            System.out.println(ship.name + ": ");
            for (String pilot : ship.pilots) {
                System.out.println(pilot);
            }
            System.out.println();
            System.out.println();
        }
    }
}

/**
 * A class to represent a Ship. Each ship has a name and a LinkedList of pilot names.
 */
class Ship {
    String name;
    LinkedList<String> pilots;
    public Ship(String _name, LinkedList<String> _pilots) {
        name = _name;
        pilots = _pilots;
    }

}
