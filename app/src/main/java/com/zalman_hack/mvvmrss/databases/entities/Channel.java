package com.zalman_hack.mvvmrss.databases.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "channel",
        indices = {@Index(value = {"name", "link"}, unique = true)})
public class Channel implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long channel_id;
    public String name;
    public String link;

    public Channel() {
        super();
    }

    protected Channel(Parcel in) {
        channel_id = in.readLong();
        name = in.readString();
        link = in.readString();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(channel_id);
        dest.writeString(name);
        dest.writeString(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return channel_id == channel.channel_id &&
                Objects.equals(name, channel.name) &&
                Objects.equals(link, channel.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel_id, name, link);
    }
}