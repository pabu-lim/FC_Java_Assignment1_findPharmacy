package org.example;

import java.util.Scanner;
public class InputManager {



    public String getUserKeyword(){
        System.out.print("위치 키워드를 입력하세요:");
        Scanner scanner = new Scanner(System.in);
        String userResponse = scanner.nextLine();
        return userResponse;
    }

    public int getRadius(){
        System.out.print("검색 반경을 입력하세요(1000:1km):");
        Scanner scanner = new Scanner(System.in);
        int radius = scanner.nextInt();
        scanner.nextLine();

        System.out.println();

        return radius;
    }
}
