package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    private Wallet wallet = new WalletInitializer().init();

    private void drawMenu() {
        String menu = "+----------------------------------------------------------------+" +
                "\n| [1] Show wallet \t \t \t \t \t \t \t \t \t \t \t \t |" +
                "\n| [2] Add new share / Increase share amount \t \t \t \t \t |" +
                "\n| [3] Delete share / Reduce share amount \t \t \t \t \t \t |" +
                "\n| [4] Change parameters of your share (e.g. stop-loss price) \t |" +
                "\n| [5] Credit or debit your free cash \t \t \t \t \t \t \t |" +
                "\n| [6] Save to XML and exit \t \t \t \t \t \t \t \t \t \t |" +
                "\n+----------------------------------------------------------------+";
        System.out.println(menu);
    }

    private void chooseOptionMainMenu() throws InputMismatchException {
        int choose = 0;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("Choose option: ");
                choose = input.nextInt();
                if (choose == 1) {
                    showWallet();
                } else if (choose == 2) {
                    buyShareInterface();
                } else if (choose == 3) {
                    sellShareInterface();
                } else if (choose == 4) {
//                  Change parameters of your share (e.g. stop-loss price)
                } else if (choose == 5) {
                    creditDebitFreeCash();
                } else if (choose == 6) {
                    WalletToXML walletToXML = new WalletToXML();
                    walletToXML.saveToXml(wallet);
                    break;
                } else {
                    drawMenu();
                    System.out.println("Try again");
                }
            } catch (InputMismatchException e) {
                drawMenu();
                System.out.println("Input valid number");
            }
        } while (choose < 1 || choose > 6);
    }

    private void chooseOptionWallet() {
        int choose = 1;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.print("Type 0 to go to main menu: ");
                choose = input.nextInt();
                if (choose == 0) {
                    drawMainMenu();
                } else {
                    drawWallet();
                    System.out.println("Try again");
                }
            } catch (InputMismatchException e) {
                drawWallet();
                System.out.println("Input valid number");
            }
        } while (choose != 0);
    }

    void drawMainMenu() {
        drawMenu();
        chooseOptionMainMenu();
    }

    private void drawWallet() {

        String leftAlignFormat = "| %6s | %6s | %13s | %13s | %13s | %13s | %10s | %15s | %17s |%n";
        String leftAlignTotalFormat = "| %31s | %13s | %13s | %13s | %10s |%n";
        String leftAlignFreeCashFormat = "| %15s | %13s |%n";

        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");
        System.out.format("| TICKER | AMOUNT | AVG BUY PRICE |     VALUE     | CURRENT PRICE | CURRENT VALUE |   RETURN   | STOP-LOSS PRICE | TAKE-PROFIT PRICE |%n");
        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");

        for (int i = 0; i < wallet.getShares().size(); i++) {
            DecimalFormat df = new DecimalFormat("0.00");
            System.out.format(leftAlignFormat,
                    wallet.getShares().get(i).getTicker(),
                    wallet.getShares().get(i).getSharesTotalAmount(),
                    wallet.getShares().get(i).getAvgBuyPrice() + " pln",
                    wallet.getShares().get(i).getBaseValue() + " pln",
                    wallet.getShares().get(i).getCurrentPrice() + " pln",
                    wallet.getShares().get(i).getCurrentValue() + " pln",
                    df.format(((wallet.getShares().get(i).getCurrentValue().doubleValue() / (wallet.getShares().get(i).getBaseValue()).doubleValue()) - 1) * 100) + " %",
                    wallet.getShares().get(i).getStopLossPrice() + " pln",
                    wallet.getShares().get(i).getTakeProfitPrice() + " pln");
        }
        BigDecimal totalBaseValue = wallet.getBaseWorth();
        BigDecimal totalCurrentValue = wallet.getSharesCurrentWorth();
        double totalReturn = totalBaseValue.doubleValue();
        if (totalReturn != 0.0) {
            totalReturn = (totalCurrentValue.doubleValue() / totalBaseValue.doubleValue() - 1) * 100;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.format("+--------+--------+---------------+---------------+---------------+---------------+------------+-----------------+-------------------+%n");
        System.out.format(leftAlignTotalFormat, "TOTAL", totalBaseValue + " pln", "", totalCurrentValue + " pln", df.format(totalReturn) + " %");
        System.out.format("+-----------------+---------------+------------------------------------------------------------+%n");
        System.out.format(leftAlignFreeCashFormat, "FREE CASH", wallet.getFreeCash() + " pln");
        System.out.format("+---------------------------------+%n");
    }

    private void showWallet() {
        drawWallet();
        chooseOptionWallet();
    }

    private void buyShareInterface() {

        String ticker;
        int amount;
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
        amount = validateAmount();
        System.out.println();
        double price = validatePrice();

        if (!wallet.checkIfEnoughCash(amount, price)) {
            System.out.println("You don't have enough money!");
        } else {
            wallet.buyShare(ticker.toUpperCase(), amount, price);
        }
        drawMainMenu();
    }

    private double validatePrice() {
        double price = 0;
        boolean isPriceInvalid;
        do {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter price: ");

            try {
                isPriceInvalid = false;
                price = scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Input valid price");
                isPriceInvalid = true;
            }

        } while (price < 1 || isPriceInvalid);
        return price;
    }

    private int validateAmount() {
        int amount = 0;
        boolean isAmountInvalid;
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
        return amount;
    }

    private void sellShareInterface() {
        String ticker;
        int amount;
        System.out.println("Sell shares");
        System.out.println();

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter valid ticker (or press ENTER to cancel): ");
            ticker = scanner.nextLine();
            System.out.println();

            if (ticker.equals("")) {
                drawMainMenu();
                break;
            }
            if (!(wallet.checkIfShareIsPresent(ticker))) {
                System.out.println("There are no shares with such ticker in your wallet");
                System.out.println();
            }
        } while (!wallet.checkIfShareIsPresent(ticker));
        Share share = wallet.scanWalletForShare(ticker);
        System.out.println(share.getFullCompanyName() + " [" + share.getTicker() + "] available amount: " + share.getSharesTotalAmount() + "   avg buy price: " + share.getAvgBuyPrice());
        System.out.println();
        amount = validateAmount();
        System.out.println();
        double price = validatePrice();
        System.out.println();

        if (!(wallet.scanWalletForShare(ticker).getSharesTotalAmount() < amount)) {
            wallet.sellShare(ticker.toUpperCase(), amount, price);
        } else {
            System.out.println("You trying to sell " + amount + " shares, but you have " + wallet.scanWalletForShare(ticker).getSharesTotalAmount());
        }
        System.out.println();
        drawMainMenu();
    }

    private void creditDebitFreeCash() {
        System.out.println("Your current balance is: " + wallet.getFreeCash() + " pln");

        int choose = 0;
        do {
            Scanner input = new Scanner(System.in);
            try {
                System.out.println("To credit your balance press 1 \n" +
                        "To debit your balance press 2");
                choose = input.nextInt();
                if (choose == 1) {
                    double amount = validateAmount();
                    wallet.creditBaseCash(BigDecimal.valueOf(amount));
                    drawMainMenu();
                } else if (choose == 2) {
                    double amount = validateAmount();
                    wallet.debitBaseCash(BigDecimal.valueOf(amount));
                    drawMainMenu();
                } else {
                    System.out.println("Wrong number - try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input valid number");
            }
        } while (choose < 1 || choose > 2);
    }
}
