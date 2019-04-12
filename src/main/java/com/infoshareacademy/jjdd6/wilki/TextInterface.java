package com.infoshareacademy.jjdd6.wilki;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    public void drawMenu() {
        String menu = "------------------------------------------------------------------" +
                "\n| [1] Show wallet \t \t \t \t \t \t \t \t \t \t \t \t |" +
                "\n| [2] Add new share / Increase share amount \t \t \t \t \t |" +
                "\n| [3] Delete share / Reduce share amount \t \t \t \t \t \t |" +
                "\n| [4] Change parameters of your share (e.g. stop-loss price) \t |" +
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

    public void clearScreen() {
        System.out.flush();
    }

    public void buyShareInterface(Wallet wallet) {
        String ticker="";
        int amount;
        double price;
        do {
            System.out.print("Enter valid ticker (or press ENTER to cancel): ");
            Scanner scanner = new Scanner(System.in);
            ticker = scanner.nextLine();
            if (ticker == "") {
                break;
            }
            System.out.println();
        } while (!new LoadData().validateTicker(ticker));

        while(ticker !="") {
            do {
                System.out.print("Enter amount: ");
                Scanner scanner2 = new Scanner(System.in);
                amount = scanner2.nextInt();
                System.out.println();
                System.out.print("Enter buy price: ");
                Scanner scanner3 = new Scanner(System.in);
                price = scanner3.nextDouble();
                System.out.println();
            } while (!wallet.checkIfEnoughCash(amount, price));

            wallet.buyShare(ticker.toUpperCase(), amount, price);
        }
//        drawMainMenu();
    }

    public void sellShareInterface(Wallet wallet) {
        String ticker="";
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

        while(ticker !="") {
            do {
                amount = inputAmount();
                System.out.print("Enter buy price: ");
                Scanner scanner3 = new Scanner(System.in);
                price = scanner3.nextDouble();
                System.out.println();
            } while (wallet.scanWalletForShare(ticker).getSharesTotalAmount() < amount);

            wallet.sellShare(ticker.toUpperCase(), amount, price);
        }
//        drawMainMenu();
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
