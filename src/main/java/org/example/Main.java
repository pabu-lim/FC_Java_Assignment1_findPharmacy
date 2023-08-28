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

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String REST_API_KEY = "KakaoAK 482726a9a7309e5c086e5f32b9899d52";
        String baseUrl = "https://dapi.kakao.com/v2/local/search/";
        String keywordType = "keyword.json";
        String categoryType = "category.json";
        String pharmacyCode = "PM9";
        String query = "서울언주초등학교";
        String latitude = "35.12999610435208";
        String longitude = "126.8527773663502";
        int radius = 20000;
        int maxSize = 10;


        Scanner scanner = new Scanner(System.in);

        System.out.print("위치 키워드를 입력하세요:");
        query = scanner.nextLine();
        System.out.print("검색 반경을 입력하세요(1000:1km):");
        radius = scanner.nextInt();
        scanner.nextLine();
        System.out.println();

        System.out.println("입력한 위치 키워드: " + query);
        System.out.println("검색 반경: " + String.format("%.1f", radius / 1000.0) + "km");
        System.out.println();
        System.out.println("**약국 검색 결과**");

        // 키워드 검색으로 좌표 먼저 가져오기
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedQuery = URLEncoder.encode(query, "UTF-8"); // URL 인코딩

            HttpGet httpGet = new HttpGet(baseUrl + keywordType + "?query=" + encodedQuery);
            httpGet.addHeader("Authorization", REST_API_KEY);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(entity);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray documentsArray = jsonObject.getJSONArray("documents");
                JSONObject documentObject = documentsArray.getJSONObject(0);

                latitude = documentObject.getString("y");
                longitude = documentObject.getString("x");


            } else {
                System.out.println("Error: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 가져온 좌표와 입력받았던 반경으로 약국 카테고리 검색(결과는 상위 10개만)
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedQuery = URLEncoder.encode(query, "UTF-8"); // URL 인코딩
            HttpGet httpGet = new HttpGet(baseUrl + categoryType + "?y=" + latitude + "&x=" + longitude + "&category_group_code=" + pharmacyCode + "&radius=" + radius +"&size=" + maxSize);
            httpGet.addHeader("Authorization", REST_API_KEY);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(entity);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray documentsArray = jsonObject.getJSONArray("documents");

                // 받아온 10개 모두 출력
                for (int i = 0; i < documentsArray.length(); i++) {
                    JSONObject documentObject = documentsArray.getJSONObject(i);
                    System.out.println("- 장소 URL(지도 위치): " + documentObject.getString("place_url"));
                    System.out.println("- 상호명: " + documentObject.getString("place_name"));
                    System.out.println("- 주소: " + documentObject.getString("address_name"));
                    System.out.println("- 전화번호: " + documentObject.getString("phone"));
                    System.out.println("- 거리(km): " + Double.parseDouble((documentObject.getString("distance"))) / 1000 + "km");
                    System.out.println();
                }

                System.out.println("----------------------------------");
                System.out.println();

                while (true) {
                    System.out.print("kakaomap URL(장소 URL):");
                    String resultUrl = scanner.nextLine();
                    if(resultUrl.equals("exit")){
                        scanner.close();
                        System.out.println("프로그램 종료");
                        break;
                    }else{
                        try {
                            Desktop.getDesktop().browse(URI.create(resultUrl));
                        } catch (Exception e) {
                            System.out.println("올바르지 않은 url");
                        }
                    }
                }



            } else {
                System.out.println("Error: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
