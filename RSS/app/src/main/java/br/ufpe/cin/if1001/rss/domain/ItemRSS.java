package br.ufpe.cin.if1001.rss.domain;

public class ItemRSS {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;

    public ItemRSS(String title, String link, String pubDate, String description) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title;
    }
}
