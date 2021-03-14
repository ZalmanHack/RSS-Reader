package com.zalman_hack.mvvmrss.databases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databases.entities.Item;

import java.util.List;

public class ChannelWithItems implements Parcelable {
    @Embedded
    public Channel channel;

    @Relation(parentColumn = "channel_id",
            entity = Item.class,
            entityColumn = "channelId")
    public List<ItemWithChannelAndCategories> items;

    public ChannelWithItems() {
        super();
    }

    protected ChannelWithItems(Parcel in) {
        channel = in.readParcelable(Channel.class.getClassLoader());
        items = in.createTypedArrayList(ItemWithChannelAndCategories.CREATOR);
    }

    public static final Creator<ChannelWithItems> CREATOR = new Creator<ChannelWithItems>() {
        @Override
        public ChannelWithItems createFromParcel(Parcel in) {
            return new ChannelWithItems(in);
        }

        @Override
        public ChannelWithItems[] newArray(int size) {
            return new ChannelWithItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(channel, flags);
        dest.writeTypedList(items);
    }
}
