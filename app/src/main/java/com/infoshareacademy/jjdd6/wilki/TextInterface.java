package com.infoshareacademy.jjdd6.wilki;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextInterface {

    private Wallet wallet = new WalletInitializer().init();

    private void drawMenu() {
        String menu = "+------------------------------------------------------------------------+" +
                "\n| [1] Show wallet" +
                "\n| [2] Buy shares" +
                "\n| [3] Sell shares" +
                "\n| [4] Set stop-loss and take-profit price" +
                "\n| [5] Increase or reduce amount of your free cash" +
                "\n| [6] Save to XML and exit" +
                "\n+------------------------------------------------------------------------+";
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
                    clearScreen();
                    drawWallet();
                    chooseOptionWallet();
                } else if (choose == 2) {
                    clearScreen();
                    buyShareInterface();
                    drawMainMenu();
                } else if (choose == 3) {
                    clearScreen();
                    sellShareInterface();
                    drawMainMenu();
                } else if (choose == 4) {
                    clearScreen();
                    shareParameters();
                    drawMainMenu();
                } else if (choose == 5) {
                    clearScreen();
                    IncreaseReduceFreeCash();
                    drawMainMenu();
                } else if (choose == 6) {
                    clearScreen();
                    if (wallet.getShares().size() > 0) {
                    WalletToXML walletToXML = new WalletToXML();
                    walletToXML.saveToXml(wallet);
                    }
                    System.out.println("Goodbye.");
                    System.exit(0);
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
                    clearScreen();
                    drawMainMenu();
                } else {
                    System.out.println("Wrong number - try again");
                }
            } catch (InputMismatchException e) {
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
            clearScreen();
            System.out.println("\nYou don't have enough money!");
            System.out.println("Your current balance: " + wallet.getFreeCash() + " pln\n");
        } else {
            wallet.buyShare(ticker.toUpperCase(), amount, price);
            clearScreen();
        }
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
            if (price < 0.01) {
                System.out.println("Price must be greater than 0\n");
            }
        } while (price < 0.01 || isPriceInvalid);
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
            if (amount < 1) {
                System.out.println("Amount must be greater than 0\n");
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
            clearScreen();
        } else {
            clearScreen();
            System.out.println("You trying to sell " + amount + " shares, but you have " + wallet.scanWalletForShare(ticker).getSharesTotalAmount());
        }
        System.out.println();
    }

    private void shareParameters() {

        String ticker;
        System.out.println("Change parameters");
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

        System.out.println("Stop-loss");
        double stopLossPrice = validatePrice();
        share.setStopLossPrice(BigDecimal.valueOf(stopLossPrice));
        System.out.println("Take-profit");
        double takeProfitPrice = validatePrice();
        share.setTakeProfitPrice(BigDecimal.valueOf(takeProfitPrice));
        clearScreen();
    }

    private void IncreaseReduceFreeCash() {
        System.out.println("Increase or reduce amount\n");
        System.out.println("Your current balance: " + wallet.getFreeCash() + " pln\n");

        int choose = 0;
        do {
            Scanner input = new Scanner(System.in);
            try {
                if (Double.parseDouble(String.valueOf(wallet.getFreeCash())) == 0.00) {
                    choose = 1;
                } else {
                    System.out.println("To increase amount of your balance press -> 1 \n" +
                            "To reduce amount of your balance press -> 2 \n \n" +
                            "Type 0 to go to main menu: ");
                    choose = input.nextInt();
                    if (choose == 0) {
                        drawMainMenu();
                        break;
                    }
                }
                if (choose == 1) {
                    double amount = validateAmount();
                    wallet.increaseBaseCash(BigDecimal.valueOf(amount));
                    clearScreen();
                    drawMainMenu();
                } else if (choose == 2) {
                    double amount = validateAmount();
                    if (Double.parseDouble(String.valueOf(wallet.getFreeCash())) < amount) {
                        clearScreen();
                        System.out.println("\nYou don't have enough money!");
                        System.out.println("Your current balance: " + wallet.getFreeCash() + " pln\n");
                        break;
                    }
                    wallet.reduceBaseCash(BigDecimal.valueOf(amount));
                    clearScreen();
                    drawMainMenu();
                } else {
                    System.out.println("Wrong number - try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input valid number");
            }
        } while (choose < 1 || choose > 2);
    }

    void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}