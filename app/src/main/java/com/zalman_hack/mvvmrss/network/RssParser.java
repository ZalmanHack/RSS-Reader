package com.zalman_hack.mvvmrss.network;

import androidx.annotation.NonNull;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import static org.jsoup.parser.Parser.unescapeEntities;

public class RssParser {

    private static final String TAG = RssParser.class.getSimpleName();
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Channel channel = new Channel();
    private final List<Category> categories = new ArrayList<Category>();;
    private final List<ItemWithChannelAndCategories> items = new ArrayList<>();
    private boolean isDataLoaded = false;

    public RssParser(String Url) {
        channel.link = Url;
        init();
    }

    public void init() {
        try {
            setDataLoaded(false);
            XmlReader xmlReader;
            if(channel.link.contains("https://")) {
                xmlReader = new XmlReader((HttpsURLConnection) new URL(channel.link).openConnection());
            }
            else {
                xmlReader = new XmlReader((HttpURLConnection) new URL(channel.link).openConnection());
            }
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(xmlReader);
            channel.name = feed.getTitle();
            List<SyndEntry> entries = feed.getEntries();

            int i = 1;
            for (SyndEntry entry : entries) {
                ItemWithChannelAndCategories itemWithCategories = new ItemWithChannelAndCategories();
                itemWithCategories.item = new Item();
                itemWithCategories.categories = new ArrayList<>();

                itemWithCategories.item.title = entry.getTitle();
                itemWithCategories.item.item_link = entry.getLink();
                itemWithCategories.item.author = entry.getAuthor();
                itemWithCategories.item.date = entry.getPublishedDate();

                SyndContent description = entry.getDescription();
                itemWithCategories.item.image_link = getImage(description.getValue());
                itemWithCategories.item.description = getDescription(description.getValue());

                for(SyndCategory name : entry.getCategories()) {
                    Category category = new Category();
                    category.name = name.getName();
                    itemWithCategories.categories.add(category);
                }
                items.add(itemWithCategories);
                setDataLoaded(true);
            }
        } catch (Exception e) {
            setDataLoaded(false);
        }
    }

    private String getImage(@NonNull String str) {
        Document doc = Jsoup.parse(str);
        Elements imgs = doc.select("img");
        for (Element img : imgs) {
            if (img.hasAttr("src")) {
                return img.attr("src");
            }
        }
        return null;
    }

    @NonNull
    private String getDescription(@NonNull String str){
        // Чтение первого текста без тегов
        List<String> splitText = Arrays.asList(str.split("<"));
        String result = unescapeEntities(splitText.get(0).trim(), true);
        str = str.replace(splitText.get(0).trim(), "");
        if (result.length() > 0) {
            result += "\n\n";
        }
        // чтение текста с тегами
        Document doc = Jsoup.parse(str);
        Elements ps = doc.select("p");
        for (Element p : ps) {
            if (p.hasText() && p.ownText().length() > 0) {
                result += p.ownText().trim() + "\n\n";
            }
        }
        // Чтение оставшегося текста без тегов
        splitText = Arrays.asList((str + " ").split(">"));
        String endText = unescapeEntities(splitText.get(splitText.size() - 1), true);
        if(!endText.equals(" "))
            result += endText;
        return result.replaceAll("(\n)+", "\n\n").trim();
    }

    public Channel getChannel() {
        return channel;
    }

    public List<ItemWithChannelAndCategories> getItems() {
        return items;
    }

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        isDataLoaded = dataLoaded;
    }
}



