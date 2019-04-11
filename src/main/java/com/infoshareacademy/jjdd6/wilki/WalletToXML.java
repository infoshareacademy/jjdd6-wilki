package com.infoshareacademy.jjdd6.wilki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class WalletToXML {

    private static Logger logger = LoggerFactory.getLogger(LoadData.class);
    private static final String SERIALIZED_FILE_NAME = "wallet.xml";

    public void saveToXml(Wallet wallet) {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
        } catch (FileNotFoundException fileNotFound) {
            logger.error("Can't create or open file wallet.xml");
        }
        if (encoder != null) {
            encoder.writeObject(wallet);
        } else {
            logger.info("Nothing to save");
        }
        if (encoder != null) {
            encoder.close();
        }
    }

    public Wallet loadFromXml() {
        XMLDecoder xmlDecoder = null;
        try {
            xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
        } catch (FileNotFoundException e) {
            logger.error("File wallet.xml not found");
        }
        if (xmlDecoder != null) {
            Wallet wallet = (Wallet) xmlDecoder.readObject();
            return wallet;
        }
        return new Wallet();
    }
}
