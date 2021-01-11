package ru.mail.migus_nikita.crawler.service;

import java.io.File;
import java.util.Map;

import ru.mail.migus_nikita.crawler.dao.DaoFactory;
import ru.mail.migus_nikita.crawler.dao.TopTenResults;
import ru.mail.migus_nikita.crawler.dao.exception.DaoException;
import ru.mail.migus_nikita.crawler.service.exception.ServiceException;

public class WriteTopTenResultsCommand {

    public void writeResults(File file, Map<Integer, String> map) throws ServiceException {
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            TopTenResults top10ResultsWriter = daoFactory.getTop10ResultsWriter();
            top10ResultsWriter.writeResults(file, map);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
