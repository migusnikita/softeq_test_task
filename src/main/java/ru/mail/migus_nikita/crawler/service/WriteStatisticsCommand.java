package ru.mail.migus_nikita.crawler.service;

import java.io.File;
import java.util.Queue;

import ru.mail.migus_nikita.crawler.dao.DaoFactory;
import ru.mail.migus_nikita.crawler.dao.StatisticsWriter;
import ru.mail.migus_nikita.crawler.dao.exception.DaoException;
import ru.mail.migus_nikita.crawler.service.exception.ServiceException;

public class WriteStatisticsCommand {

    public void writeStatistics(File file, Queue<String> queue) throws ServiceException {
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            StatisticsWriter statisticsWriter = daoFactory.getStatisticsWriter();
            statisticsWriter.writeStatistics(file, queue);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
