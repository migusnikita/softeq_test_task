package ru.mail.migus_nikita.crawler;

import ru.mail.migus_nikita.crawler.service.Crawler;
import ru.mail.migus_nikita.crawler.util.ConsoleWriterUtil;
import ru.mail.migus_nikita.crawler.util.CsvWriterUtil;
import ru.mail.migus_nikita.crawler.util.FileWriterUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

@Component
public class CrawlerRunner implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(CrawlerRunner.class);

    private static final String URL_HEADER = "Url";
    private static final String TOTAL_HEADER = "Total";
    private static final String CANNOT_WRITE_RESULTS_TO_CSV_FILES = "Cannot write results to .csv files";

    private final Crawler crawler;

    @Value("${crawler.top-results-limit:10}")
    private int topResultsLimit;

    @Value("${crawler.words-to-find:Tesla}")
    private String[] words;

    @Value("${crawler.max-depth:1}")
    private int maxDepth;

    @Value("${crawler.max-visited-pages:100}")
    private int maxVisitedPages;

    @Value("${crawler.root-url:https://www.tesla.com/}")
    private String rootUrl;

    @Value("${crawler.result-directory:D:\\out}")
    private String resultDirectory;

    @Value("${crawler.output-file:out_urls.csv}")
    private String outputFile;

    @Value("${crawler.output-top-file:out_urls_top.csv}")
    private String outputTopFile;

    @Autowired
    public CrawlerRunner(Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void run(String... args) {
        Map<String, List<Integer>> scannedUrls = crawler.crawl(rootUrl, maxDepth, maxVisitedPages, words);


        String[] headers = prepareHeaders(words);
        addTotalToResult(scannedUrls);
        Map<String, List<Integer>> top10Pages = getTopByTotalPages(scannedUrls, topResultsLimit);

        try {
            FileWriterUtil.createFolder(resultDirectory);
            CsvWriterUtil.write(headers, scannedUrls, resultDirectory + File.separator + outputFile);
            CsvWriterUtil.write(headers, top10Pages, resultDirectory + File.separator + outputTopFile);
        } catch (IOException e) {
            logger.error(CANNOT_WRITE_RESULTS_TO_CSV_FILES, e);
        }

        top10Pages.entrySet().forEach(page -> logger.info(ConsoleWriterUtil.prepareUrlToConsoleOutput(page)));
    }

    private String[] prepareHeaders(String[] searchWords) {
        String[] headers = new String[searchWords.length + 2];
        headers[0] = URL_HEADER;
        System.arraycopy(searchWords, 0, headers, 1, searchWords.length);
        headers[headers.length - 1] = TOTAL_HEADER;
        return headers;
    }

    private void addTotalToResult(Map<String, List<Integer>> urlsWithResults) {
        urlsWithResults.values()
                .forEach((results) -> results.add(
                        results.
                                stream().
                                mapToInt(Integer::intValue).
                                sum()));
    }

    private Map<String, List<Integer>> getTopByTotalPages(Map<String, List<Integer>> visitedUrls, int resultsLimit) {
        return visitedUrls.entrySet()
                .stream()
                .sorted(comparingByValue((url1, url2) -> url2.get(url2.size() - 1) - url1.get(url1.size() - 1)))
                .limit(resultsLimit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

}
