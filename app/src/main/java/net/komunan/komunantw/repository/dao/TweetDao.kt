@file:Suppress("FunctionName")

package net.komunan.komunantw.repository.dao

import androidx.room.*
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.w
import net.komunan.komunantw.repository.entity.Credential
import net.komunan.komunantw.repository.entity.Tweet
import net.komunan.komunantw.repository.entity.TweetAccount
import net.komunan.komunantw.common.service.TwitterService
import twitter4j.TwitterException

@Dao
abstract class TweetDao {

    /* ==================== Functions. ==================== */

    fun find(id: Long, credential: Credential, forceFetch: Boolean = false): Tweet? {
        return find(id, listOf(credential), forceFetch)
    }

    fun find(id: Long, credentials: List<Credential>, forceFetch: Boolean = false): Tweet? {
        return if (forceFetch) {
            fetchTweet(id, credentials) ?: find(id)
        } else {
            find(id) ?: fetchTweet(id, credentials)
        }
    }

    fun save(tweet: Tweet): Tweet {
        return saveWithoutLog(tweet).apply {
            d { "save: $tweet" }
        }
    }

    fun saveWithoutLog(tweet: Tweet): Tweet {
        val current = find(tweet.id)
        if (current == null) {
            pInsert(tweet)
        } else {
            pUpdate(tweet)
        }
        return tweet
    }

    private fun fetchTweet(id: Long, credentials: List<Credential>): Tweet? {
        return try {
            var result: Tweet? = null
            for (credential in credentials) {
                val status = TwitterService.twitter(credential).showStatus(id)
                if (result == null) {
                    result = Tweet(status).save()
                }
                TweetAccount(credential.accountId, status).save()
            }
            return result
        } catch (e: TwitterException) {
            w(e); null
        }
    }

    /* ==================== SQL Definitions. ==================== */

    @Query("SELECT * FROM tweet WHERE id = :id")
    abstract fun find(id: Long): Tweet?

    @Query("DELETE FROM tweet WHERE NOT EXISTS (SELECT * FROM tweet_source WHERE tweet.id = tweet_source.tweet_id)")
    abstract fun deleteUnnecessary(): Int

    /* ==================== SQL Definitions. ==================== */

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract fun pInsert(tweet: Tweet)

    @Update
    protected abstract fun pUpdate(tweet: Tweet)
}
