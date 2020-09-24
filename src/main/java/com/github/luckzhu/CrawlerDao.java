package com.github.luckzhu;

import java.sql.SQLException;

public interface CrawlerDao {
    //DB
    void setLinkIntoDB(String link) throws SQLException;

    //DB
    String getUnhandledLinkFromLinkPool(
            
    ) throws SQLException;

    //DB
    void insertNewsIntoDB(News news) throws SQLException;

    //DB
    void MarkLinkHandled(String link) throws SQLException;
}
