package org.example;

import org.json.JSONArray;

import java.util.HashMap;

public interface KakaoRequester {
    JSONArray KeywordSearch(String keyword);
    JSONArray categorySearch(HashMap<String, String> query, int radius, int maxSize);
}
