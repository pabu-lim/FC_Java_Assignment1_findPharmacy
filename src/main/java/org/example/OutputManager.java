package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URI;
import java.util.Scanner;

public class OutputManager {
    public void printResult(JSONArray resultJsonArray, String keyword, int radius) {
        System.out.println("입력한 위치 키워드: " + keyword);
        System.out.println("검색 반경: " + String.format("%.1f", radius / 1000.0) + "km");
        System.out.println();
        System.out.println("**약국 검색 결과**");

        for (int i = 0; i < resultJsonArray.length(); i++) {
            JSONObject resultObject = resultJsonArray.getJSONObject(i);
            System.out.println("- 장소 URL(지도 위치): " + resultObject.getString("place_url"));
            System.out.println("- 상호명: " + resultObject.getString("place_name"));
            System.out.println("- 주소: " + resultObject.getString("address_name"));
            System.out.println("- 전화번호: " + resultObject.getString("phone"));
            System.out.println("- 거리(km): " + Double.parseDouble((resultObject.getString("distance"))) / 1000 + "km");
            System.out.println();
        }
        System.out.println("----------------------------------");
        System.out.println();
    }

    public void openBrowser() {
        Scanner scanner = new Scanner(System.in);

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
                    System.out.println(resultUrl + "은 올바르지 않은 URL입니다.");
                }
            }
        }

    }
}
