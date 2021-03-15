package com.zalman_hack.mvvmrss.databases.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item",
        indices = {@Index(value = {"date", "author", "title", "description",
                "image_link", "item_link", "channelId"}, unique = true)},
        foreignKeys = {@ForeignKey(onDelete = CASCADE,
                entity = Channel.class,
                parentColumns = "channel_id",
                childColumns = "channelId")})
public class Item implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long item_id;
    public Date date;
    public String author;
    public String title;
    public String description;
    public String image_link;
    public String item_link;
    @ColumnInfo(index = true)
    public long channelId;

    public Item() {
        super();
    }

    protected Item(Parcel in) {
        item_id = in.readLong();
        author = in.readString();
        title = in.readString();
        description = in.readString();
        image_link = in.readString();
        item_link = in.readString();
        channelId = in.readLong();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(item_id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image_link);
        dest.writeString(item_link);
        dest.writeLong(channelId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return channelId == item.channelId &&
                Objects.equals(date, item.date) &&
                Objects.equals(author, item.author) &&
                Objects.equals(title, item.title) &&
                Objects.equals(description, item.description) &&
                Objects.equals(image_link, item.image_link) &&
                Objects.equals(item_link, item.item_link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, author, title, description, image_link, item_link, channelId);
    }
}

