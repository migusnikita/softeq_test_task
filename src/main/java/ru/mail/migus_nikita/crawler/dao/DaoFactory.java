package ru.mail.migus_nikita.crawler.dao;

public final class DaoFactory {

    private static final DaoFactory INSTANCE = new DaoFactory();
    private final StatisticsWriter statisticsWriter = new StatisticsWriter();
    private final TopTenResults top10ResultsWriter = new TopTenResults();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public StatisticsWriter getStatisticsWriter() {
        return statisticsWriter;
    }

    public TopTenResults getTop10ResultsWriter() {
        return top10ResultsWriter;
    }

}
