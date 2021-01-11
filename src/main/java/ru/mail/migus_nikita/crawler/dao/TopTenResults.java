package ru.mail.migus_nikita.crawler.dao;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import ru.mail.migus_nikita.crawler.dao.exception.DaoException;

public class TopTenResults {

    public void writeResults(File file, Map<Integer, String> map) throws DaoException {
        try (PrintWriter writer = new PrintWriter(file)) {
            int count = 0;
            Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
            while (count < 10) {
                if (iterator.hasNext()) {
                    Map.Entry<Integer, String> pair = iterator.next();
                    writer.println("Total hits: " + pair.getKey() + ", link: " + pair.getValue());
                    System.out.println("Total hits: " + pair.getKey() + ", link: " + pair.getValue());
                }
                ++count;
            }
        } catch (FileNotFoundException e) {
            throw new DaoException(e);
        }
    }
}
