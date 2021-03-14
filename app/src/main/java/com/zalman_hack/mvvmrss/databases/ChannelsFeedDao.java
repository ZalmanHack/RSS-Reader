package com.zalman_hack.mvvmrss.databases;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.zalman_hack.mvvmrss.databases.entities.Category;
import com.zalman_hack.mvvmrss.databases.entities.Channel;
import com.zalman_hack.mvvmrss.databases.entities.Item;
import com.zalman_hack.mvvmrss.databases.entities.ItemWithCategory;

import java.util.List;

@Dao
public abstract class ChannelsFeedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertRelation(ItemWithCategory record);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertItem(Item record);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertCategory(Category record);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertChannel(Channel record);



    @Query("SELECT category_id FROM category WHERE name = :name LIMIT 1")
    abstract long getCategoryId(String name);

    @Query("SELECT channel_id FROM channel WHERE name = :name LIMIT 1")
    abstract long getChannelId(String name);



    @Query("DELETE FROM item")
    public abstract void deleteItemAll();

    @Query("DELETE FROM item WHERE channelId = :channel_id")
    public abstract void deleteFeeds(long channel_id);

    @Query("DELETE FROM category")
    public abstract void deleteCategoryAll();

    @Query("DELETE FROM channel")
    public abstract void deleteChannelAll();

    /** @deprecated */
    @Query("DELETE FROM itemwithcategory")
    public abstract void deleteItemWithCategory();


    // Публичные методы ----------------------------------------------------------------------------

    // FIXME: Во время каскадного удаления items могцт остаться более не используемые категории,
    //  что приводит к утечке памяти
    @Query("DELETE FROM channel WHERE name == :name ")
    public abstract void deleteChannelOf(String name);


    @Transaction
    @Query("SELECT * FROM channel")
    public abstract LiveData<List<ChannelWithItems>> getItemsAllLive();


    @Transaction
    @Query("SELECT * FROM item, channel " +
            "WHERE item.channelId == :channelId AND channel.channel_id == item.channelId")
    public abstract LiveData<List<ItemWithChannelAndCategories>> getItemsOfChannelLive(long channelId);

/*
    @Transaction
    @Query("SELECT * FROM item, channel " +
            "WHERE channel.channel_id == item.channelId")
    public abstract LiveData<List<ItemWithChannelAndCategories>> getItemsAllLive();
*/

    @Transaction
    @Query("SELECT * FROM channel")
    public abstract LiveData<List<Channel>> getChannelsAllLive();

    @Transaction
    @Query("SELECT * FROM channel")
    public abstract List<Channel> getChannelsAll();

    @Transaction
    public void deleteFeedsAll() {
        deleteCategoryAll();
        deleteItemAll();
    }

    @Transaction
    public void updateChannelItems(Channel channel, List<ItemWithChannelAndCategories> items) {
        long channel_id = getChannelId(channel.name);
        if (channel_id < 1) {
            channel_id = insertChannel(channel);
        }
        else {
            deleteFeeds(channel_id);
        }
        for (ItemWithChannelAndCategories item : items) {
            item.item.channelId = channel_id;
            long item_id = insertItem(item.item);
            for (Category category : item.categories) {
                long category_id = getCategoryId(category.name);
                if (category_id < 1) {
                    category_id = insertCategory(category);
                }
                ItemWithCategory itemWithCategory = new ItemWithCategory();
                itemWithCategory.itemId = item_id;
                itemWithCategory.categoryId = category_id;
                insertRelation(itemWithCategory);
            }
        }
    }

    /** @deprecated */
    @Transaction
    public void insertItem(Channel channel, ItemWithChannelAndCategories item) {
        long channel_id = getChannelId(channel.name);
        if(channel_id < 1)
            channel_id = insertChannel(channel);
        item.item.channelId = channel_id;
        long item_id = insertItem(item.item);
        if(item.categories != null) {
            for (Category category : item.categories) {
                long category_id = getCategoryId(category.name);
                if (category_id < 1)
                    category_id = insertCategory(category);
                ItemWithCategory itemWithCategory = new ItemWithCategory();
                itemWithCategory.itemId = item_id;
                itemWithCategory.categoryId = category_id;
                insertRelation(itemWithCategory);
            }
        }
    }

}

