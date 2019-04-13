package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Optional;
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
                    buyShareInterface();
                } else if (choose == 3) {
                    clearScreen();
                    sellShareInterface();
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
        String leftAlignTotalFormat = "| %31s | %13s | %13s | %13s | %10s |%n";

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

        BigDecimal totalBaseValue = wallet.getBaseWorth();
        BigDecimal totalCurrentValue = wallet.getSharesCurrentWorth();
        Double totalReturn = (totalCurrentValue.doubleValue() / totalBaseValue.doubleValue() - 1) * 100;
        DecimalFormat df = new DecimalFormat("0.00");


        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");
        System.out.format(leftAlignTotalFormat, "TOTAL", totalBaseValue + " pln", "", totalCurrentValue + " pln", df.format(totalReturn) + " %");
        System.out.format("+----------------------------------------------------------------------------------------------+%n");
    }

    public void showWallet() {
        drawWallet();
        chooseOptionWallet();
    }


    public void buyShareInterface() {

        String ticker;
        int amount = 0;
        double price = 0;
        boolean isAmountInvalid;
        boolean isPriceInvalid;
        System.out.println("Add shares");

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter valid ticker (or press ENTER to cancel): ");
            ticker = scanner.nextLine();
            if (ticker.equals("")) {
                drawMainMenu();
                break;
            }
            System.out.println();
        } while (!new LoadData().validateTicker(ticker));

        System.out.println("Found! " + new LoadData().loadAndScanTickers(ticker.toUpperCase()));
        System.out.println();
        do {
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter amount: ");

                try {
                    isAmountInvalid = false;
                    amount = scanner.nextInt();

                } catch (InputMismatchException e) {
                    System.out.println("Input valid amount");
                    isAmountInvalid = true;
                }
            } while (amount < 1 || isAmountInvalid);

            System.out.println();

            do {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter buy price: ");
                try {
                    isPriceInvalid = false;
                    price = scanner.nextDouble();
                } catch (InputMismatchException e) {
                    System.out.println("Input valid price");
                    isPriceInvalid = true;
                }
            } while (price < 1 || isPriceInvalid);
        } while (!wallet.checkIfEnoughCash(amount, price));

        wallet.buyShare(ticker.toUpperCase(), amount, price);

        drawMainMenu();
    }

    public void sellShareInterface() {
        String ticker = "";
        int amount;
        double price;
        do {
            System.out.print("Enter valid ticker (or press ENTER to cancel): ");
            Scanner scanner = new Scanner(System.in);
            ticker = scanner.nextLine();
            System.out.println();
            if (ticker.equals("") || wallet.checkIfShareIsPresent(ticker)) {
                break;
            }
            System.out.println("Ticker is not in your wallet");
        } while (!wallet.checkIfShareIsPresent(ticker));

        do {
            amount = inputAmount();
            System.out.print("Enter buy price: ");
            Scanner scanner3 = new Scanner(System.in);
            price = scanner3.nextDouble();
            System.out.println();
        } while (wallet.scanWalletForShare(ticker).getSharesTotalAmount() < amount);

        wallet.sellShare(ticker.toUpperCase(), amount, price);
        drawMainMenu();
    }

    private int inputAmount() {
        int amount;
        System.out.print("Enter amount: ");
        Scanner scanner2 = new Scanner(System.in);
        amount = scanner2.nextInt();
        System.out.println();
        return amount;
    }
}
