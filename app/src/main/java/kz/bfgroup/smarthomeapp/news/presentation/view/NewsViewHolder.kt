package kz.bfgroup.smarthomeapp.news.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.news.models.NewsApiData

class NewsViewHolder(
    itemView: View,
    private val onNewsClickListener: NewsClickListener
): RecyclerView.ViewHolder(itemView) {

    private val newsClickItemLayout : LinearLayout = itemView.findViewById(R.id.news_click_item)
    private val newsImage: ImageView = itemView.findViewById(R.id.news_image)
    private val newsTitle: TextView = itemView.findViewById(R.id.news_title)

    fun onBind(newsApiData: NewsApiData) {

        Glide
            .with(itemView.context)
            .load(newsApiData.img)
            .centerCrop()
            .into(newsImage)

        newsTitle.text = newsApiData.title

        newsClickItemLayout.setOnClickListener {
            onNewsClickListener.onNewsClick(newsApiData.id)
        }
    }
}