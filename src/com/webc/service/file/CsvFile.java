package com.webc.service.file;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CsvFile {

    public static CSVWriter create(){

        try {
            File file = new File("statusCodes.csv");
            FileWriter statusCodeData = new FileWriter(file);

            CSVWriter writer = new CSVWriter(statusCodeData);

            String[] header = { "url", "status code"};
            writer.writeNext(header);

            return  writer;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void close(CSVWriter writer) throws IOException {

        writer.close();
    }
}
