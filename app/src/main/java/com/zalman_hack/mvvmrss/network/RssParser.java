package com.zalman_hack.mvvmrss.network;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.zalman_hack.mvvmrss.databases.ItemWithChannelAndCategories;
import com.zalman_hack.mvvmrss.databases.entities.Category;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databases.entities.Item;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RssParser {

    private static final String TAG = RssParser.class.getSimpleName();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Channel channel;
    //private final List<Item> items;
    private final List<Category> categories;

    private List<ItemWithChannelAndCategories> items;

    public RssParser(String Url) {
        channel = new Channel();
        channel.link = Url;
        items = new ArrayList<ItemWithChannelAndCategories>();
        categories = new ArrayList<Category>();;
        init();

    }

    public void init() {
        try {
            HttpURLConnection httpsURLConnection = (HttpURLConnection) new URL(channel.link).openConnection();
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(httpsURLConnection));
            channel.name = feed.getTitle();
            List<SyndEntry> entries = feed.getEntries();

            int i = 1;
            for (SyndEntry entry : entries) {
                ItemWithChannelAndCategories itemWithCategories = new ItemWithChannelAndCategories();
                itemWithCategories.item = new Item();
                itemWithCategories.categories = new ArrayList<Category>();

                itemWithCategories.item.title = entry.getTitle();
                itemWithCategories.item.item_link = entry.getLink();
                itemWithCategories.item.author = entry.getAuthor();
                itemWithCategories.item.date = entry.getPublishedDate();

                SyndContent description = entry.getDescription();
                Document doc = Jsoup.parse(description.toString());

                Elements imgs = doc.select("img");
                for (Element img : imgs) {
                    if (img.hasAttr("src")) {
                        itemWithCategories.item.image_link = img.attr("src");
                        break;
                    }
                }

                Elements ps = doc.select("p");
                for (Element p : ps) {
                    if (p.hasText()) {
                        itemWithCategories.item.description = MessageFormat.format("{0}{1}", itemWithCategories.item.description, p.text() + "<br/>");
                    }
                }

                for(SyndCategory name : entry.getCategories()) {
                    Category category = new Category();
                    category.name = name.getName();
                    itemWithCategories.categories.add(category);
                }
                items.add(itemWithCategories);
            }
        } catch (Exception ignore) { }
    }

    public Channel getChannel() {
        return channel;
    }

    public List<ItemWithChannelAndCategories> getItems() {
        return items;
    }
}



