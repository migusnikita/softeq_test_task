package ru.mail.migus_nikita.crawler.util;

import java.util.List;
import java.util.Map;

public class ConsoleWriterUtil {

    public static String prepareUrlToConsoleOutput(Map.Entry<String, List<Integer>> visitedUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitedUrl.getKey()).append(" ");

        for (Integer i : visitedUrl.getValue()) {
            sb.append(i).append(" ");
        }

        return sb.toString();
    }

}

