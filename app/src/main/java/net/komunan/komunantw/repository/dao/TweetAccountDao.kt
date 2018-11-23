package net.komunan.komunantw.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.komunan.komunantw.repository.entity.TweetAccount

@Dao
abstract class TweetAccountDao {

    /* ==================== SQL Definitions. ==================== */

    @Query("SELECT * FROM tweet_account WHERE tweet_id = :tweetId")
    abstract fun find(tweetId: Long): List<TweetAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(tweetAccount: TweetAccount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun save(tweetAccounts: List<TweetAccount>)
}
