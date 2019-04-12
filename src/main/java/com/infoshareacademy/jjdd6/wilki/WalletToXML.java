package com.infoshareacademy.jjdd6.wilki;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class WalletToXML {

    private static Logger logger = LoggerFactory.getLogger(AppRunner.class);
    private static final String SERIALIZED_FILE_NAME = "wallet.xml";

    public void saveToXml(Wallet wallet) {
        XmlMapper xmlMapper = new XmlMapper();

        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.registerModule(new JSR310Module());
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            xmlMapper.writeValue(new File(SERIALIZED_FILE_NAME), wallet);
        } catch (IOException e) {
            logger.error("Wallet export to XML failed");
        }
    }


    public Wallet loadFromXml() {
        try {
            File file = new File(SERIALIZED_FILE_NAME);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.registerModule(new JavaTimeModule());
            xmlMapper.registerModule(new JSR310Module());
            xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            String xml = inputStreamToString(new FileInputStream(file));
            return xmlMapper.readValue(xml, Wallet.class);
        } catch (Exception e) {
            logger.error("Wallet import from XML failed");
        }
        return new Wallet();
    }

    public String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
