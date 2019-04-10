package com.infoshareacademy.jjdd6.wilki;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    public void drawMenu() {
        String menu = "+----------------------------------------------------------------+" +
                "\n| [1] Show wallet \t \t \t \t \t \t \t \t \t \t \t \t |" +
                "\n| [2] Add new share / Increase share amount \t \t \t \t \t |" +
                "\n| [3] Delete share / Reduce share amount \t \t \t \t \t \t |" +
                "\n| [4] Change parameters of your share (e.g. stop-loss price) \t |" +
                "\n| [5] Add / Remove money amount \t \t \t \t \t \t \t \t |" +
                "\n+----------------------------------------------------------------+";
        System.out.println(menu);
    }

    public void chooseOptionMainMenu() throws InputMismatchException {
        int choose = 0;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("Choose option: ");
                choose = input.nextInt();
                if (choose == 1) {
                    clearScreen();
                    showWallet();
                } else if (choose == 2) {

                    clearScreen();
//                  Add new share / Increase share amount
                } else if (choose == 3) {

                    clearScreen();
//                  Delete share / Reduce share amount
                } else if (choose == 4) {

                    clearScreen();
//                  Change parameters of your share (e.g. stop-loss price)
                } else if (choose == 5) {
                    clearScreen();
//                  Add / Remove money amount
                } else {
                    clearScreen();
                    drawMenu();
                    System.out.println("Try again");
                }
            } catch (InputMismatchException e) {
                clearScreen();
                drawMenu();
                System.out.println("Input valid number");
            }
        } while (choose < 1 || choose > 5);

    }

    public void chooseOptionWallet() {
        int choose = 1;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("Type 0 to return to main menu: ");
                choose = input.nextInt();
                if (choose == 0) {
                    clearScreen();
                    drawMainMenu();
                } else {
                    clearScreen();
                    drawWallet();
                    System.out.println("Try again");
                }
            } catch (InputMismatchException e) {
                clearScreen();
                drawWallet();
                System.out.println("Input valid number");
            }
        } while (choose != 0);
    }

    public void clearScreen() {
        System.out.flush();
    }

    public void drawMainMenu() {
        drawMenu();
        chooseOptionMainMenu();
    }

    public void drawWallet() {
        String leftAlignFormat = "| %-7s | %-4d | %-10s | %-1s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |%-10s | %-10s | %-10s |%n";

        System.out.format("+------------+---------------------------------------------------------------------------------------------------+%n");
        System.out.format("|   Ticker   |  ID  |%n");
        System.out.format("+------------+------+%n");
        for (int i = 0; i < 10; i++) {
            System.out.format(leftAlignFormat, "ticker" + i, i * i,i,i,i,i,i,i,i,i,i,i,i,i);
        }
        System.out.format("------------+----------------------------------------------------------------------------------------------------+%n");
    }

    public void showWallet() {
        drawWallet();
        chooseOptionWallet();
    }
}
