package edu.unipr.valdrin.javafinalassignment2021.services;

import edu.unipr.valdrin.javafinalassignment2021.models.COVID19LocationsStatistics;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service            //Spring @Service annotation is a specialization of @Component annotation. Spring Service annotation can be applied only to classes. It is used to mark the class as a service provider
public class COVID19DataService {

    public static String virusDataLink = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<COVID19LocationsStatistics> allStatistics = new ArrayList<>();

    public List<COVID19LocationsStatistics> getAllStatistics() {            //Getter for allStatistics
        return allStatistics;
    }

    @PostConstruct      //The @PostConstruct annotation is used on a method that needs to be executed after dependency injection is done to perform any initialization.
    @Scheduled(cron = "* * 1 * * *")    //The @Scheduled annotation schedules the run of a method on a regular bases (here it is the first hour of every day)
    public void getVirusData(){
        List<COVID19LocationsStatistics> newStatistics = new ArrayList<>();   //New list filled with colons that we need

        HttpClient client = HttpClient.newHttpClient();  //creating a new client ,It can be used to request HTTP resources over the network
        HttpRequest request = HttpRequest.newBuilder()   // 1.
                .uri(URI.create(virusDataLink))         //access the request link,creates the string
                .build();
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());  //Getting a response for sending the client.Requests can be sent either synchronously or asynchronously. The synchronous API, as expected, blocks until the HttpResponse is available
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(httpResponse.body());  //reading the text from the file inserted to the link in line Nr. 22
        StringReader csvBodyReader = new StringReader(httpResponse.body()); //Reader instance is a way to get to read text (parses string)
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader); //Parsing files in the reader (in this case with First Record as Header)
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CSVRecord record : records) {      //Loop through records objects
            COVID19LocationsStatistics locationStatistics = new COVID19LocationsStatistics();
            /*locationStatistics.setState(record.get("Province/State"));*/
            locationStatistics.setCountry(record.get("Country/Region"));
            //locationStatistics.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            int latestCases = Integer.parseInt(record.get(record.size()-1));    //Getting the last colum of the csv in the link (the last updated date with covid cases)
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));
            locationStatistics.setLatestTotalCases(latestCases);
            locationStatistics.setDiffFromPrevDay(latestCases - prevDayCases);      //Getting the number of new cases
            newStatistics.add(locationStatistics);
            /*String country = record.get("Country/Region");
            System.out.println(country);*/
        }
        this.allStatistics = newStatistics;
    }
}

        /* 1.HttpRequests are built from HttpRequest builders.
        HttpRequest builders are obtained by calling HttpRequest.newBuilder.
        A request's URI, headers and body can be set. Request bodies are provided through a HttpRequest.
        BodyProcessor object supplied to the DELETE, POST or PUT methods. GET does not take a body.
        Once all required parameters have been set in the builder, HttpRequest.Builder.build()
        is called to return the HttpRequest. Builders can also be copied and modified multiple times
        in order to build multiple related requests that differ in some parameters*/

//Recourses
//https://developer.android.com/guide/background/threading#java
//https://start.spring.io/
//https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv
//https://commons.apache.org/proper/commons-csv/user-guide.html
//https://getbootstrap.com/docs/5.0/components/carousel/
//https://getbootstrap.com/docs/5.0/components/card/
//https://getbootstrap.com/docs/4.0/components/jumbotron/
//https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#using-theach
//https://docs.oracle.com/
//https://www.youtube.com
//https://www.google.com
//Earliers projects with professor Efthimios Alepis