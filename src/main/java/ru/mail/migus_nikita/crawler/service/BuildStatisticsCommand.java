package ru.mail.migus_nikita.crawler.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.mail.migus_nikita.crawler.controller.Controller;

public class BuildStatisticsCommand implements Runnable {

    private final String url;
    private final String[] attrs;

    public BuildStatisticsCommand(String url, String[] attrs) {
        this.url = url;
        this.attrs = attrs;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();
            StringBuilder builder = new StringBuilder();
            builder.append(url).append(" ");
            int totalHits = 0;
            int currentHits;
            for (String attr : attrs) {
                currentHits = StringUtils.countMatches(text, attr);
                builder.append(currentHits).append(" ");
                totalHits += currentHits;
            }
            builder.append(totalHits);
            Controller.mapWithLinksAndTotalHits.put(totalHits, url);
            Controller.queueWithLinksAndStatistics.add(builder.toString());
        } catch (IOException | IllegalArgumentException e) {
            Controller.queueWithLinksAndStatistics.add("Invalid link");
        }
    }

}
