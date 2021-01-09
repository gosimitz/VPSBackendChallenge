# VPSBackendChallenge
## About
This program queries the Star Wars API (https://swapi.dev) and fetches starship and pilot information. It prints out each ship and the pilots that have flown that ship. 

### Example:
Starship:

Pilot 

Pilot 2


Starship 2: 

No Pilots 


## Prerequisites
This program uses a JSON parsing library at https://github.com/stleary/JSON-java, other than that the program runs on purely built in libraries in Java 11. 

## To Run
1. Make sure you have downloaded the JSON parsing library .jar and StarshipPilotLister.java 

2. Open a terminal window and compile the program using:
javac -cp (relative path to the JSON library jar) (relative path to the StarshipPilotLister.java)

3. Move json's jar, StarshipPilotLister.class, and Ship.class to the same directory (optional, but I like to do this) using mv relative_path_curr target_dir

4. Run the program: java -cp json-20201115.jar:. StarshipPilotLister 

5. Enjoy!
