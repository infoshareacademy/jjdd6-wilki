package com.infoshareacademy.jjdd6.wilki;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SaveData {


    private static final String SERIALIZED_FILE_NAME = "wallet.xml";

    public void serializeToXml() {
        Wallet wallet = new Wallet();
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("ERROR: While creating or opening file wallet.xml");
        }
        if (encoder != null) {
            encoder.writeObject(wallet);
        }
        else System.out.println("Nothing to save!");
        if (encoder != null) {
            encoder.close();
        }
    }
}
