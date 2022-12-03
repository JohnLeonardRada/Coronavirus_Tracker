package com.project.coronavirustracker.services;

import com.project.coronavirustracker.model.LocationStats;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronavirusDataService {

    private static final String CONFIRMED_CASES_GLOBALLY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String RECOVERED_CASES_GLOBALLY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";
    private static final String DEATH_CASES_GLOBALLY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private List<LocationStats> confirmedAllStats = new ArrayList<>();
    private List<LocationStats> recoveredAllStats = new ArrayList<>();

    public List<LocationStats> getConfirmedAllStats() {
        return confirmedAllStats;
    }

    public List<LocationStats> getRecoveredAllStats() {
        return recoveredAllStats;
    }

    //After Done Creating The Instance Of The Class, Execute The Method With @PostConstruct
    //Spring Supports Multiple @PostConstruct
    @PostConstruct
    //AUTO RUN BASED ON SET SCHEDULE
    //@Scheduled(cron = "* * 1 * * *")
    public void fetchConfirmedCases() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CONFIRMED_CASES_GLOBALLY_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPreviousDay(latestCases - previousDayCases);
            System.out.println(locationStat);

            newStats.add(locationStat);
        }
        this.confirmedAllStats = newStats;
    }

    @PostConstruct
    public void fetchRecoveredCases() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(RECOVERED_CASES_GLOBALLY_URL))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for(CSVRecord record: records){
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            locationStat.setLatestTotalCases(latestCases);
            System.out.println(locationStat);

            newStats.add(locationStat);
        }
        this.recoveredAllStats = newStats;
    }
}
