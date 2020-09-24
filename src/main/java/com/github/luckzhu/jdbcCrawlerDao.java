package com.github.luckzhu;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class jdbcCrawlerDao implements CrawlerDao {

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    @Override
    public void setLinkIntoDB(String link) throws SQLException {
        String sql = "INSERT INTO LINKPOOL (LINK) VALUES (?)";
        setConnection(sql);
        preparedStatement.setString(1, link);
        preparedStatement.executeUpdate();
        closeConnection();
    }


    private static void setConnection(String sql) throws SQLException {
        File projectDir = new File(System.getProperty("basedir", System.getProperty("user.dir")));
        String jdbcUrl = "jdbc:h2:file:" + new File(projectDir, "target/crawler").getAbsolutePath();
        connection = DriverManager.getConnection(jdbcUrl, "root", "root");
        preparedStatement = connection.prepareStatement(sql);
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public String getUnhandledLinkFromLinkPool() throws SQLException {
        String sql = "SELECT * FROM LINKPOOL WHERE STATUS = 1 LIMIT 1";
        setConnection(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String link = null;
        while (resultSet.next()) {
            link = resultSet.getString("link");
        }
        closeConnection();
        return link;
    }

    @Override
    public void insertNewsIntoDB(News news) throws SQLException {
        String sql = "INSERT INTO NEWS (TITLE,AUTHOR,CONTENT,CREATED_AT) VALUES(?,?,?,?);";
        setConnection(sql);
        preparedStatement.setString(1, news.getTitle());
        preparedStatement.setString(2, news.getAuthor());
        preparedStatement.setString(3, news.getContent());
        preparedStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
        preparedStatement.executeUpdate();
        closeConnection();
    }

    @Override
    public void MarkLinkHandled(String link) throws SQLException {
        File projectDir = new File(System.getProperty("basedir", System.getProperty("user.dir")));
        String jdbcUrl = "jdbc:h2:file:" + new File(projectDir, "target/crawler").getAbsolutePath();
        String sql = "UPDATE LINKPOOL SET STATUS = -1 WHERE LINK = ? ";
        setConnection(sql);
        preparedStatement.setString(1, link);
        preparedStatement.executeUpdate();
        closeConnection();
    }
}
