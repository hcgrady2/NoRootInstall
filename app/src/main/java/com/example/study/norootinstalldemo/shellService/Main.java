package com.example.study.norootinstalldemo.shellService;


public class Main {
    public static void main(String[] args){
        new ServiceThread().start();
        while (true);
    }
}