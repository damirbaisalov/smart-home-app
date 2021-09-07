package kz.smart.house.news.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.data.GlideApp
import kz.smart.house.news.models.NewsApiData

class NewsViewHolder(
    itemView: View,
    private val onNewsClickListener: NewsClickListener
): RecyclerView.ViewHolder(itemView) {

    private val newsClickItemLayout : LinearLayout = itemView.findViewById(R.id.news_click_item)
    private val newsImage: ImageView = itemView.findViewById(R.id.news_image)
    private val newsTitle: TextView = itemView.findViewById(R.id.news_title)
    private val newsDescription: TextView = itemView.findViewById(R.id.news_description)

    fun onBind(newsApiData: NewsApiData) {

        GlideApp
            .with(itemView.context)
            .load(newsApiData.img)
            .centerCrop()
            .into(newsImage)

        newsTitle.text = newsApiData.title
        newsDescription.text = newsApiData.description

        newsClickItemLayout.setOnClickListener {
            onNewsClickListener.onNewsClick(newsApiData.id)
        }
    }
}