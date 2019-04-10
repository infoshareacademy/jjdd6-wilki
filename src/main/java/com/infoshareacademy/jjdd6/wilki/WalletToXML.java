package com.infoshareacademy.jjdd6.wilki;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class WalletToXML {


    private static final String SERIALIZED_FILE_NAME = "wallet.xml";

    public void saveToXml(Wallet wallet) {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("ERROR: While creating or opening file wallet.xml");
        }
        if (encoder != null) {
            encoder.writeObject(wallet);
        } else System.out.println("Nothing to save!");
        if (encoder != null) {
            encoder.close();
        }
    }

    public Wallet loadFromXml() {
        XMLDecoder xmlDecoder = null;
        try {
            xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File wallet.xml not found");
        }
        if (xmlDecoder != null) {
            Wallet wallet = (Wallet) xmlDecoder.readObject();
            return wallet;
        }
        return new Wallet();
    }
}
