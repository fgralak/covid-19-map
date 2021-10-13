package com.tutorial.geolocation.parser;

import com.tutorial.geolocation.model.Point;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class CovidDataParser
{
    public List<Point> parseData(String dataToParse, String date) throws IOException
    {
        StringReader stringReader = new StringReader(dataToParse);
        CSVParser parsedData = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);

        List<Point> listOfPoints = new ArrayList<>();

        for (CSVRecord row : parsedData)
        {
            if(row.get("Lat").isEmpty() || row.get("Long").isEmpty()) continue;
            double latitude = Double.parseDouble(row.get("Lat"));
            double longitude = Double.parseDouble(row.get("Long"));
            String text = row.get(date);
            listOfPoints.add(new Point(latitude, longitude, text));
        }
        return listOfPoints;
    }
}
