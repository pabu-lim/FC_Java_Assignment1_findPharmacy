package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import static org.example.KakaoAPIConfig.*;

public class KakaoRequesterImpl implements KakaoRequester {
    JSONArray documentsArray;

    public JSONArray KeywordSearch(String keyword){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedQuery = URLEncoder.encode(keyword, "UTF-8"); // URL 인코딩

            HttpGet httpGet = new HttpGet(BASE_URL + KEYWORD_TYPE + "?query=" + encodedQuery);
            httpGet.addHeader("Authorization", REST_API_KEY);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);
                documentsArray = jsonObject.getJSONArray("documents");
            } else {
                System.out.println("Error: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentsArray;
    }

    public JSONArray categorySearch(HashMap<String, String> query, int radius, int maxSize) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet httpGet = new HttpGet(BASE_URL + CATEGORY_TYPE + "?y=" + query.get("y") + "&x=" + query.get("x") + "&category_group_code=" + PHARMACY_CODE + "&radius=" + radius + "&size=" + maxSize);
            httpGet.addHeader("Authorization", REST_API_KEY);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);
                documentsArray = jsonObject.getJSONArray("documents");
            } else {
                System.out.println("Error: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  documentsArray;
    }

}
