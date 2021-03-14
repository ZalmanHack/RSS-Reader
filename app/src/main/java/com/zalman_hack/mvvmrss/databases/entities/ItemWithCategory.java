package com.zalman_hack.mvvmrss.databases.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "itemWithCategory",
        primaryKeys = {"itemId", "categoryId"},
        foreignKeys = {@ForeignKey(onDelete = CASCADE,
                entity = Item.class,
                parentColumns = "item_id",
                childColumns = "itemId")})
public class ItemWithCategory implements Parcelable {
    @ColumnInfo(index = true)
    public long itemId;
    @ColumnInfo(index = true)
    public long categoryId;

    public ItemWithCategory() {
        super();
    }

    protected ItemWithCategory(Parcel in) {
        itemId = in.readLong();
        categoryId = in.readLong();
    }

    public static final Creator<ItemWithCategory> CREATOR = new Creator<ItemWithCategory>() {
        @Override
        public ItemWithCategory createFromParcel(Parcel in) {
            return new ItemWithCategory(in);
        }

        @Override
        public ItemWithCategory[] newArray(int size) {
            return new ItemWithCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemId);
        dest.writeLong(categoryId);
    }
}