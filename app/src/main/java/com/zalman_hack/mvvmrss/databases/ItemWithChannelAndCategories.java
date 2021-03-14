package com.zalman_hack.mvvmrss.databases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.zalman_hack.mvvmrss.databases.entities.Category;
import com.zalman_hack.mvvmrss.databases.entities.Item;
import com.zalman_hack.mvvmrss.databases.entities.ItemWithCategory;

import java.util.List;
import java.util.Objects;

public class ItemWithChannelAndCategories implements Parcelable {
    @Embedded
    public Item item;

    //@Embedded
    //public Channel channel;

    @Relation(parentColumn = "item_id",
            entity = Category.class,
            entityColumn = "category_id",
            associateBy = @Junction(
                    value = ItemWithCategory.class,
                    parentColumn = "itemId",
                    entityColumn = "categoryId"))
    public List<Category> categories;

    public ItemWithChannelAndCategories() {
        super();
    }

    protected ItemWithChannelAndCategories(Parcel in) {
        item = in.readParcelable(Item.class.getClassLoader());
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<ItemWithChannelAndCategories> CREATOR = new Creator<ItemWithChannelAndCategories>() {
        @Override
        public ItemWithChannelAndCategories createFromParcel(Parcel in) {
            return new ItemWithChannelAndCategories(in);
        }

        @Override
        public ItemWithChannelAndCategories[] newArray(int size) {
            return new ItemWithChannelAndCategories[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(item, flags);
        dest.writeTypedList(categories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemWithChannelAndCategories that = (ItemWithChannelAndCategories) o;
        return Objects.equals(item, that.item) &&
                Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, categories);
    }
}