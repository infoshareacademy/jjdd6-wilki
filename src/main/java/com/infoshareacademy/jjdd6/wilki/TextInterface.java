package com.infoshareacademy.jjdd6.wilki;

public class TextInterface {
    public void drawMenu(){

        String menu = "------------------------------------------------------------------" +
                "\n| [1] Show wallet \t \t \t \t \t \t \t \t \t \t \t \t |" +
                "\n| [2] Add new stocks / Increase stocks amount \t \t \t \t \t |" +
                "\n| [3] Delete stocks / Reduce stocks amount \t \t \t \t \t \t |" +
                "\n| [4] Change parameters of your stocks (e.g. stop-loss price \t |" +
                "\n| [5] Add / Remove money amount \t \t \t \t \t \t \t \t |" +
                "\n------------------------------------------------------------------";
        System.out.println(menu);

    }
}
