package com.github.luckzhu;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// 初始链接一个，存入数据库的链接池
// 从链接池中取出一个未处理的链接
// 判断这个链接是否符合要求，符合则将其内容存入数据库的新闻池
// 然后将这个链接上的含有的链接（符合条件的）存入链接池
// 重复循环 直到没链接 或者 达到目标条数
public class Crawler {
    private CrawlerDao dao = new jdbcCrawlerDao();

    public static void main(String[] args) throws SQLException, IOException {
//        String initialLink = "";
//        dao.setLinkIntoDB(initialLink);
        new Crawler().run();
    }

    private void run() throws SQLException, IOException {
        String link = dao.getUnhandledLinkFromLinkPool();

        while (link != null) {
            System.out.println(link);
            if (conformsToCrawlingRules(link)) {
                News news = getNewsByLink(link);
                if (news != null) {
                    dao.insertNewsIntoDB(news);
                    System.out.println("保存成功");
                }
            }
            List<String> allLinksOnPage = getAllLinksOnPage(link);
            for (String newLink : allLinksOnPage) {
                dao.setLinkIntoDB(newLink);
            }
            dao.MarkLinkHandled(link);
            link = dao.getUnhandledLinkFromLinkPool();
        }
    }

    private static List<String> getAllLinksOnPage(String link) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        Document document = getHtmlDocument(link);
        Elements linkElements = document.select("a");
        for (Element linkEle : linkElements) {
            String href = linkEle.attr("href");
            if (conformsToCrawlingRules(href)) {
                list.add(href);
            }
        }
        return list;
    }

    private static Document getHtmlDocument(String link) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(link)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("服务器端错误: " + response);
        }
        String responseBodyString = Objects.requireNonNull(response.body()).string();
        return Jsoup.parse(responseBodyString);
    }

    private static News getNewsByLink(String link) throws IOException {
        Document document = getHtmlDocument(link);
        Elements articles = document.select("article");
        if (articles.size() > 0) {
            Element article = articles.get(0);
            String text = article.select(".art_tit_h1").text();
            String author = article.select(".art_cite").text();
            if (author.equals("")) {
                author = article.select(".weibo_user").text();
            }
            String content = article.select(".art_content").toString();
            return new News(text, author, content);
        }
        return null;
    }

    private static boolean conformsToCrawlingRules(String link) {
        return link.contains("news.sina.cn");
    }

}
