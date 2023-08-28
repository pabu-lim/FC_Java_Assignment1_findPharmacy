package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static org.example.KakaoAPIConfig.MAX_SIZE;

public class Client {
    public static void main(String[] args) {
        InputManager inputManager = new InputManager();
        KakaoRequester kakaoRequester = new KakaoRequesterImpl();
        OutputManager outputManager = new OutputManager();

        HashMap<String, String> coordinate = new HashMap<>();

        String keyword = inputManager.getUserKeyword();
        int radius = inputManager.getRadius();

        JSONArray resultJsonArray = kakaoRequester.KeywordSearch(keyword);
        JSONObject firstJSONResult = resultJsonArray.getJSONObject(0);
        coordinate.put("x", firstJSONResult.getString("x"));
        coordinate.put("y", firstJSONResult.getString("y"));

        resultJsonArray = kakaoRequester.categorySearch(coordinate, radius, MAX_SIZE);

        outputManager.printResult(resultJsonArray, keyword, radius);

        outputManager.openBrowser();

    }
}