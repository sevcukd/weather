package com.example.ivasik.asinctask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;


public class Parsing {


    public String parseString(String way, String wth, String parsingData) {

        JSONObject weatherJsonObject = null;
        try {
            weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray weatherArray = (JSONArray) weatherJsonObject.get(way);
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        return (String) weatherData.get(wth);
    }


    public String parseString( String parsingData) {
        JSONObject weatherJsonObject = null;
        try {
            weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (String) weatherJsonObject.get("name");

    }

    public double parseLong(String way, String wth, String parsingData) {
        JSONObject weatherJsonObject = null;
        try {
            weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject temp = (JSONObject) weatherJsonObject.get(way);
        JSONArray array = new JSONArray();
        array.add(temp);
        JSONObject data = (JSONObject) array.get(0);
        return Double.parseDouble(data.get(wth).toString());
    }
}
