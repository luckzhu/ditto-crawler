package com.github.luckzhu;

public class News {
    public String title;
    public String author;
    public String content;

    public News(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "News{" +
               "title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", content='" + content + '\'' +
               '}';
    }
}
