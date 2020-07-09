package ru.mail.migus_nikita.crawler.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mail.migus_nikita.crawler.service.Crawler;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

@Service
public class CrawlerImpl implements Crawler {

    private static final Logger logger = LogManager.getLogger(CrawlerImpl.class);

    @Value("${crawler.jsoup-href-pattern:a[href]}")
    private String jsoupHrefPattern;

    @Value("${crawler.jsoup-abs-href-pattern:abs:href}")
    private String jsoupAbsHrefPattern;

    @Value("${crawler.exclude-link-pattern:[http].+[^(pdf|rar|zip)]}")
    private String excludeLinksPattern;

    @Override
    public Map<String, List<Integer>> crawl(String url, int maxDepth, int maxVisitedPages, String[] searchWords) {

        final Queue<SimpleEntry<String, Integer>> urlsToProcess = new LinkedList<>();

        final Map<String, List<Integer>> linksWithSearchResults = new HashMap<>();

        urlsToProcess.add(new SimpleEntry<>(url, 0));

        while (!urlsToProcess.isEmpty()) {
            SimpleEntry<String, Integer> currentUrlDepthPair = urlsToProcess.poll();
            List<SimpleEntry<String, Integer>> newUrls = processUrl(currentUrlDepthPair, searchWords,
                    linksWithSearchResults, maxDepth, maxVisitedPages);
            urlsToProcess.addAll(newUrls);
        }

        logger.info("Total: " + linksWithSearchResults.keySet().size() + "\n");

        return linksWithSearchResults;
    }

    private List<SimpleEntry<String, Integer>> processUrl(
            SimpleEntry<String, Integer> currentUrlDepthPair, String[] searchWords,
            Map<String, List<Integer>> linksWithSearchResults, int maxDepth, int maxVisitedPages) {

        String currentUrl = currentUrlDepthPair.getKey();
        int currentUrlDepth = currentUrlDepthPair.getValue();

        List<SimpleEntry<String, Integer>> foundUrls = new LinkedList<>();

        try {
            logger.debug("==> Depth: " + currentUrlDepth + " [" + currentUrl + "]");
            Document document = Jsoup.connect(currentUrl).get();

            if (currentUrlDepth < maxDepth) {
                if (currentUrlDepth == 0) {
                    linksWithSearchResults.put(currentUrl, null);
                }
                Elements linksOnPage = document.select(jsoupHrefPattern);
                currentUrlDepth++;
                for (Element link : linksOnPage) {
                    String urlString = link.attr(jsoupAbsHrefPattern);
                    if (!urlString.isEmpty() && !linksWithSearchResults.containsKey(urlString)
                            && linksWithSearchResults.keySet().size() < maxVisitedPages && isLinkToHtmlPage(urlString)) {
                        foundUrls.add(new SimpleEntry<>(urlString, currentUrlDepth));
                        linksWithSearchResults.put(urlString, new ArrayList<>());
                    }
                }
            }

            List<Integer> wordSearchResult = checkWordsMatchingOnPage(document, searchWords);
            linksWithSearchResults.put(currentUrl, wordSearchResult);

        } catch (IOException | IllegalArgumentException e) {
            logger.error("For '" + currentUrl + "': " + e.getMessage());
        }

        return foundUrls;
    }

    private List<Integer> checkWordsMatchingOnPage(Document document, String[] searchWords) {
        List<Integer> wordSearchResult = new ArrayList<>();

        for (String wordToFind : searchWords) {
            Elements elements = document.getElementsMatchingText(wordToFind);
            wordSearchResult.add(elements.size());
        }

        return wordSearchResult;
    }

    private boolean isLinkToHtmlPage(String url) {
        return url.matches(excludeLinksPattern);
    }

}
