package ru.mail.migus_nikita.crawler.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvWriterUtil {

    public static void write(String[] headers, Map<String, List<Integer>> scannedPages, String filePath) throws IOException {

        try (FileWriter out = new FileWriter(filePath);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {

            List<Object[]> preparedRecords = prepareRecords(scannedPages);

            for (Object[] preparedRecord : preparedRecords) {
                printer.printRecord(preparedRecord);
            }
        }
    }

    private static List<Object[]> prepareRecords(Map<String, List<Integer>> scannedPages) {
        List<Object[]> preparedRecords = new ArrayList<>();

        Set<String> urls = scannedPages.keySet();

        for (String url : urls) {
            List<Integer> currentSearchResult = scannedPages.get(url);
            Object[] record = new Object[currentSearchResult.size() + 1];
            record[0] = url;

            for (int i = 1; i < record.length; i++) {
                record[i] = currentSearchResult.get(i - 1);
            }

            preparedRecords.add(record);
        }

        return preparedRecords;
    }

}
