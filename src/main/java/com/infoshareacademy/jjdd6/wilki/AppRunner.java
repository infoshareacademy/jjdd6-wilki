package com.infoshareacademy.jjdd6.wilki;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {
    public void run() {

        TextInterface textInterface = new TextInterface();
        textInterface.drawMenu();

        chooseOption();

    }
    public void chooseOption() throws InputMismatchException {
        Scanner input = new Scanner(System.in);
         do{
            try {
                int choice = input.nextInt();
                switch (choice) {
                    case 1: {
                        System.out.println("1");
                        break;
                    }
                    case 2: {
                        System.out.println("2");
                        break;
                    }
                    case 3: {
                        System.out.println("3");
                        break;
                    }
                    case 4: {
                        System.out.println("4");
                        break;
                    }
                    case 5: {
                        System.out.println("5");
                        break;
                    }
                    default: {
                        System.out.println("Try again");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Input valid number");
            }
        }while(choice > 0 && choice < 6)

    }
}
