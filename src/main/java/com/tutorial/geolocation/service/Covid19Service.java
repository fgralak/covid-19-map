package com.tutorial.geolocation.service;

import com.tutorial.geolocation.model.Point;
import com.tutorial.geolocation.parser.CovidDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Covid19Service
{
    private final CovidDataParser covidDataParser;
    private final RestTemplate restTemplate;

    private static final String COVID_CONFIRMED_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                    "csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private static final String COVID_DEAD_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                    "csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private static final String COVID_RECOVERED_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                    "csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    public List<List<Point>> getCovidData(String date) throws IOException
    {
        String confirmed = restTemplate.getForObject(COVID_CONFIRMED_URL, String.class);
        String dead = restTemplate.getForObject(COVID_DEAD_URL, String.class);
        String recovered = restTemplate.getForObject(COVID_RECOVERED_URL, String.class);

        HashMap<List<Double>, String> covidData = new HashMap<>();

        for (Point point : covidDataParser.parseData(confirmed, date))
        {
            covidData.put(Arrays.asList(point.getX(),point.getY()), "Confirmed: " + point.getText() + "<br>");
        }

        for (Point point : covidDataParser.parseData(dead, date))
        {
            String data = covidData.getOrDefault(Arrays.asList(point.getX(),point.getY()), "");
            covidData.put(Arrays.asList(point.getX(),point.getY()), data + "Dead: " + point.getText() + "<br>");
        }

        for (Point point : covidDataParser.parseData(recovered, date))
        {
            String data = covidData.getOrDefault(Arrays.asList(point.getX(),point.getY()), "");
            covidData.put(Arrays.asList(point.getX(),point.getY()), data + "Recovered: " + point.getText());
        }

        List<Point> covidPoints = new ArrayList<>();

        for (Map.Entry<List<Double>, String> entry : covidData.entrySet())
        {
            covidPoints.add(new Point(entry.getKey().get(0), entry.getKey().get(1), entry.getValue()));
        }

        List<List<Point>> result = new ArrayList<>();
        result.add(covidPoints);
        result.add(new ArrayList<>(covidDataParser.parseData(confirmed, date)));

        return result;
    }
}
