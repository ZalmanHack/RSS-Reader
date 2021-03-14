package com.zalman_hack.mvvmrss.databases.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item",
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
}

