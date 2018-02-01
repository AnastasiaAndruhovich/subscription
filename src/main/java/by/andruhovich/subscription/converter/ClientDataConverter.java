package by.andruhovich.subscription.converter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientDataConverter {

    private static final Logger LOGGER = LogManager.getLogger(ClientDataConverter.class);

    public static Date convertStringToDate(String date) {
        LOGGER.log(Level.INFO, "Request for convert string to date");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date resultDate = null;
        try {
            resultDate = dateFormat.parse(date);
        } catch (ParseException e) {
            LOGGER.log(Level.ERROR, "Error convert string to date");
        }
        return resultDate;
    }

    public static byte[] convertImageToByteArray(String pathName) {
        File file = new File(pathName);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Error convert image to byte array");
        }
        return null;
    }
}
