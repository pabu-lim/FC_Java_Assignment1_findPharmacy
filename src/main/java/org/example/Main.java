package org.example;

import org.apache.http.HttpEntity;
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
        String baseUrl = "https://dapi.kakao.com/v2/local/search/"; // Url
        String restAPI = "keyword.json"; // 요청할 REST API 종류 - 키워드검색
        String restAPI2 = "category.json"; // 요청할 REST API 종류 - 카테고리검색
        String categoryGroupCode = "PM9"; // 약국
        String query = "서울언주초등학교"; // 쿼리
        String x = "126.8527773663502"; // x좌표
        String y = "35.12999610435208"; // y좌표
        int radius = 20000; // 거리m 반경
        int size = 10; // 페이지에 보여질 개수(default:10)


        Scanner scanner = new Scanner(System.in);

        System.out.print("위치 키워드를 입력하세요:");
        query = scanner.nextLine();
        System.out.print("검색 반경을 입력하세요(1000:1km):");
        radius = scanner.nextInt();
        scanner.nextLine(); // nextInt사용 후 엔터 제거
        System.out.println();

        System.out.println("입력한 위치 키워드: " + query);
        System.out.println("검색 반경: " + String.format("%.1f", radius / 1000.0) + "km");
        System.out.println();
        System.out.println("**약국 검색 결과**");

        // 키워드 검색으로 좌표 먼저 가져오기
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedQuery = URLEncoder.encode(query, "UTF-8"); // URL 인코딩

            HttpGet httpGet = new HttpGet(baseUrl + restAPI + "?query=" + encodedQuery);
            httpGet.addHeader("Authorization", "KakaoAK 482726a9a7309e5c086e5f32b9899d52");

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String result = EntityUtils.toString(entity);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray documentsArray = jsonObject.getJSONArray("documents");
                JSONObject documentObject = documentsArray.getJSONObject(0);
                x = documentObject.getString("x");
                y = documentObject.getString("y");

            } else {
                System.out.println("Error: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 가져온 좌표와 입력받았던 반경으로 약국 카테고리 검색(결과는 상위 10개만)
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedQuery = URLEncoder.encode(query, "UTF-8"); // URL 인코딩
            HttpGet httpGet = new HttpGet(baseUrl + restAPI2 + "?y=" + y + "&x=" + x + "&category_group_code=" + categoryGroupCode + "&radius=" + radius +"&size=" + size);
            httpGet.addHeader("Authorization", "KakaoAK 482726a9a7309e5c086e5f32b9899d52");

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 200) {
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
