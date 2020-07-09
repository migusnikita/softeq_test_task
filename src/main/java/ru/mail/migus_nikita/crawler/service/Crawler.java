package ru.mail.migus_nikita.crawler.service;

import java.util.List;
import java.util.Map;

public interface Crawler {
    Map<String, List<Integer>> crawl(String url, int maxDepth, int maxVisitedPages, String[] searchWords);
}
