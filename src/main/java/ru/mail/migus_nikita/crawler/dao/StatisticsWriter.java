package ru.mail.migus_nikita.crawler.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Queue;

import ru.mail.migus_nikita.crawler.dao.exception.DaoException;

public class StatisticsWriter {

    public void writeStatistics(File file, Queue<String> queue) throws DaoException {
        try (PrintWriter writer = new PrintWriter(file)) {
            while (!queue.isEmpty()) {
                writer.println(queue.poll());
            }
        } catch (FileNotFoundException e) {
            throw new DaoException(e);
        }
    }

}
