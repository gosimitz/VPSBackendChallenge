import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import org.json.JSONArray; //https://github.com/stleary/JSON-java
import org.json.JSONObject;

/**
 * This is a program that queries the swapi.dev to get the Starships used in the Star Wars universe and the pilots of the
 * respective ships. https://github.com/stleary/JSON-java is a JSON parsing library required to run this
 * This was written using Java 11.
 * Author: Gus Osimitz
 */
public class StarshipPilotLister {

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
        int shipCount =  ships.getInt("count"); //get count of total starships according to the api
        //Iterate through the ships
        for(int currShip = 0; currShip <= shipCount; currShip++) {
            //Get the ship info
            JSONObject currShipJSON = querySwapi(swapiURL + shipEndPoint + currShip + "/");
            //Due to an unsuccessful API call, likely a nonexistent ship
            if(currShipJSON.isEmpty()){
                continue;
            }
            String shipName = currShipJSON.getString("name");

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
            //Due to an unsuccessful API call, likely a nonexistent pilot
            if(pilotJSON.isEmpty()){
                continue;
            }
            pilots.add(pilotJSON.getString("name"));
        }
        return pilots;
    }
    /**
     * A simple method to run a GET query on swapi.dev
     * @param url - desired endpoint to query
     * @return - A JSONObject representing the body of the response, or an empty JSONObject if a 200 response was not recieved
     * @throws IOException
     * @throws InterruptedException
     */
    private static JSONObject querySwapi(String url) throws IOException, InterruptedException {
        //API requires https, but some urls may just be http, add the 's' if it is missing.
        if(url.charAt(4) != 's') {
            StringBuilder tempUrl = new StringBuilder(url);
            tempUrl.insert(4, 's');
            url = tempUrl.toString();
        }
        //Create client
        HttpClient client = HttpClient.newHttpClient();

        //Create request
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();

        //Send request
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //protect against any unsuccessful API calls
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
            if(ship.pilots.size() == 0) {
                System.out.println("No pilots");
            }
            for (String pilot : ship.pilots) {
                System.out.println(pilot);
            }
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
