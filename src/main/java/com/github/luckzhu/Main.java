package com.github.luckzhu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 初始链接一个，存入数据库的链接池
        // 从链接池中取出一个未处理的链接
        // 判断这个链接是否符合要求，符合则将其内容存入数据库的新闻池
        // 然后将这个链接上的含有的链接（符合条件的）存入链接池
        // 重复循环 直到没链接 或者 达到目标条数
        String initialLink = "";
        setLinkIntoDB(initialLink);

        String link = getUnhandledLinkFromLinkPool();
        News news = getNewsByLink(link);
        insertNewsIntoDB(news);

        List<String> allLinksOnPage = getAllLinksOnPage(link);

        for (String newLink : allLinksOnPage) {
            if (conformsToCrawlingRules(newLink)) {
                setLinkIntoDB(newLink);
            }
        }
        MarkLinkHandled(link);
    }

    //DB
    private static void MarkLinkHandled(String link) {
    }

    private static List<String> getAllLinksOnPage(String link) {
        return new ArrayList<>();
    }

    //DB
    private static void insertNewsIntoDB(News news) {

    }

    private static News getNewsByLink(String link) {
        return new News();
    }

    private static boolean conformsToCrawlingRules(String link) {
        return true;
    }

    //DB
    private static String getUnhandledLinkFromLinkPool() {
        return "";
    }

    //DB
    private static void setLinkIntoDB(String link) {

    }
}
