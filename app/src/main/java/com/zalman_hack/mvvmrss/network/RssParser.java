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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;
import static org.jsoup.parser.Parser.unescapeEntities;

public class RssParser {

    private Channel channel;
    private final RetrofitService service;
    private List<ItemWithChannelAndCategories> items;
    private boolean isDataLoaded = false;

    @Inject
    public RssParser(RetrofitService service) {
        this.service = service;
    }

    public void load(String url) {
        this.channel = new Channel();
        this.channel.link = url;
        init();
    }

    public void init() {
        try {
            setDataLoaded(false);
            items = new ArrayList<>();
            InputStream responseBody = requireNonNull(service.serviceNews.getRss(channel.link).execute().body()).byteStream();
            XmlReader xmlReader = new XmlReader(responseBody);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(xmlReader);
            channel.name = feed.getTitle();
            List<SyndEntry> entries = feed.getEntries();

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
        StringBuilder resultBuilder = new StringBuilder(result);
        for (Element p : ps) {
            if (p.hasText() && p.ownText().length() > 0) {
                resultBuilder.append(p.ownText().trim()).append("\n\n");
            }
        }
        result = resultBuilder.toString();
        // Чтение оставшегося текста без тегов
        splitText = Arrays.asList((str + " ").split(">"));
        String endText = unescapeEntities(splitText.get(splitText.size() - 1), true);
        if(!endText.equals(" "))
            result += endText;
        result = result.replaceAll("(\n)+", "\n\n").trim();
        if(result.isEmpty())
            result += doc.wholeText();
        return result;
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



