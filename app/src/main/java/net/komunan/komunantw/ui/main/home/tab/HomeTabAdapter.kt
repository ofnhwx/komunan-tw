package net.komunan.komunantw.ui.main.home.tab

import android.arch.paging.PagedListAdapter
import android.net.Uri
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_tweet.view.*
import kotlinx.coroutines.*
import net.komunan.komunantw.R
import net.komunan.komunantw.repository.entity.TweetDetail
import net.komunan.komunantw.repository.entity.User
import net.komunan.komunantw.string

class HomeTabAdapter: PagedListAdapter<TweetDetail, HomeTabAdapter.TweetViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<TweetDetail> = object: DiffUtil.ItemCallback<TweetDetail>() {
            override fun areItemsTheSame(oldItem: TweetDetail, newItem: TweetDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TweetDetail, newItem: TweetDetail): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TweetViewHolder(inflater.inflate(R.layout.item_tweet, parent, false))
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TweetViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(tweet: TweetDetail?) {
            if (tweet == null) {
                return
            }
            GlobalScope.launch(Dispatchers.Main) {
                val user = withContext(Dispatchers.Default) { User.find(tweet.userId) }
                if (user == null) {
                    // TODO: 取得を試みてダメならダミー画像とかを設定
                } else {
                    itemView.tweet_user_icon.setImageURI(Uri.parse(user.imageUrl))
                    itemView.tweet_user_name.text = user.name
                    itemView.tweet_user_screen_name.text = R.string.format_screen_name.string(user.screenName)
                }
                itemView.tweet_date_time.text = "{{時間}}" // TODO: 時間をフォーマットして設定
                itemView.tweet_text.text = tweet.text
            }
        }
    }
}
