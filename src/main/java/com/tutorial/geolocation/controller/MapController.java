package com.tutorial.geolocation.controller;

import com.tutorial.geolocation.model.Point;
import com.tutorial.geolocation.service.Covid19Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MapController
{
    private final Covid19Service covid19Service;

    @GetMapping
    public String getMap(Model model, @RequestParam String date) throws IOException
    {
        List<List<Point>> covidData = covid19Service.getCovidData(date);
        model.addAttribute("covidPoints", covidData.get(0));
        model.addAttribute("covidRadius", covidData.get(1));
        return "map";
    }
}
