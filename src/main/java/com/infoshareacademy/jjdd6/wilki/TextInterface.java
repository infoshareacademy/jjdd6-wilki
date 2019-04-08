package com.infoshareacademy.jjdd6.wilki;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    public void drawMenu() {
        String menu = "------------------------------------------------------------------" +
                "\n| [1] Show wallet \t \t \t \t \t \t \t \t \t \t \t \t |" +
                "\n| [2] Add new stocks / Increase stocks amount \t \t \t \t \t |" +
                "\n| [3] Delete stocks / Reduce stocks amount \t \t \t \t \t \t |" +
                "\n| [4] Change parameters of your stocks (e.g. stop-loss price) \t |" +
                "\n| [5] Add / Remove money amount \t \t \t \t \t \t \t \t |" +
                "\n------------------------------------------------------------------";
        System.out.println(menu);
    }

    public void chooseOption() throws InputMismatchException {
        int choose = 0;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("Choose option: ");
                choose = input.nextInt();
                if (choose == 1) {
                    clearScreen();
//                  Show wallet
                } else if (choose == 2) {
                    clearScreen();
//                  Add new stocks / Increase stocks amount
                } else if (choose == 3) {
                    clearScreen();
//                  Delete stocks / Reduce stocks amount
                } else if (choose == 4) {
                    clearScreen();
//                  Change parameters of your stocks (e.g. stop-loss price)
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

    public void clearScreen() {
        System.out.flush();
    }
}
