FROM openjdk:11
WORKDIR /application
RUN git clone https://github.com/gosimitz/VPSBackendChallenge.git
RUN git clone https://github.com/stleary/JSON-java.git
CMD javac org\json\*.java
CMD jar cf json-java.jar org/json/*.class

CMD javac -cp json-java.jar /src/StarshipPilotLister.java
CMD mv /out/production/VPSBackendChallenge/Ship.class ./
CMD mv /out/production/VPSBackendChallenge/StarshipPilotLister.class ./

CMD java -cp json-java.jar:. StarshipPilotLister
