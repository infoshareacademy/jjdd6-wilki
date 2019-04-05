package com.infoshareacademy.jjdd6.wilki;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {
    public void run() {

        TextInterface textInterface = new TextInterface();
        textInterface.drawMenu();

        chooseOption();

    }
    private void chooseOption() throws InputMismatchException {
        int choose = 0;
        do{
            Scanner input = new Scanner(System.in);
            try{
                choose = input.nextInt();
                if(choose == 1){
                    // show wallet
                }
                else if(choose == 2){
                    // add/
                }
                else if(choose == 3){

                }
                else if(choose == 4){

                }
                else if(choose == 5){

                }
                else{
                    System.out.println("Try again");
                }

            }catch (InputMismatchException e){
                System.out.println("Input valid number");
            }
        }while(choose < 1 || choose > 5);


    }
}
