package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    private Wallet wallet = new WalletInitializer().init();

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

        String leftAlignFormat = "| %6s | %6s | %13s | %13s | %13s | %13s | %10s | %15s | %17s |%n";

        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");
        System.out.format("| TICKER | AMOUNT | AVG BUY PRICE |     VALUE     | CURRENT PRICE | CURRENT VALUE |   RETURN   | STOP-LOSS PRICE | TAKE-PROFIT PRICE |%n");
        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");

        for (int i = 0; i < wallet.getShares().size(); i++) {
            System.out.format(leftAlignFormat,
                    wallet.getShares().get(i).getTicker(),
                    wallet.getShares().get(i).getSharesTotalAmount(),
                    wallet.getShares().get(i).getAvgBuyPrice() + " pln",
                    wallet.getShares().get(i).getBaseValue() + " pln",
                    wallet.getShares().get(i).getCurrentPrice() + " pln",
                    wallet.getShares().get(i).getCurrentValue() + " pln",
                    wallet.getShares().get(i).getCurrentValue().divide(wallet.getShares().get(i).getBaseValue()).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).doubleValue() + " %",
                    wallet.getShares().get(i).getStopLossPrice() + " pln",
                    wallet.getShares().get(i).getTakeProfitPrice() + " pln");
        }

        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");
        System.out.format("|               TOTAL                                                                          |%n");
        System.out.format("+----------------------------------------------------------------------------------------------+%n");
    }

    public void showWallet() {
        drawWallet();
        chooseOptionWallet();
    }


}
