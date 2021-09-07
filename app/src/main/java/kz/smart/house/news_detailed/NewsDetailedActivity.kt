package kz.smart.house.news_detailed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import kz.smart.house.R
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.news.models.NewsApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsDetailedActivity : AppCompatActivity() {

    private lateinit var toolbarSearchView: SearchView
    private lateinit var backButton: ImageButton
    private lateinit var newsImage: ImageView
    private lateinit var newsTitle: TextView
    private lateinit var newsDescription: TextView
    private lateinit var newsDate: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detailed)
        initView()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()
    }

    private fun initView() {
        toolbarSearchView = findViewById(R.id.activity_news_list_toolbar_search_view)
        toolbarSearchView.visibility = View.INVISIBLE
        newsImage = findViewById(R.id.news_detailed_image)
        newsTitle = findViewById(R.id.news_detailed_title)
        newsDescription = findViewById(R.id.news_detailed_description)
        newsDate = findViewById(R.id.news_detailed_date)
        progressBar = findViewById(R.id.activity_news_detailed_progressbar)
        backButton = findViewById(R.id.activity_news_back_button)
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getNewsById(intent.getStringExtra("one_news_id")!!).enqueue(object: Callback<NewsApiData>{
            override fun onResponse(call: Call<NewsApiData>, response: Response<NewsApiData>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val responseBody = response.body()!!

                    Glide
                        .with(this@NewsDetailedActivity)
                        .load(responseBody.img)
                        .centerCrop()
                        .into(newsImage)

                    newsTitle.text = responseBody.title
                    newsDescription.text = responseBody.description
                    newsDate.text = responseBody.data?.substring(0,10)
                }
            }

            override fun onFailure(call: Call<NewsApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@NewsDetailedActivity,t.message,Toast.LENGTH_LONG).show()
            }

        })
    }
}