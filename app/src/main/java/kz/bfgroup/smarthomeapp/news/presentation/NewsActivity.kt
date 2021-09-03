package kz.bfgroup.smarthomeapp.news.presentation

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsAdapter
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsClickListener
import kz.bfgroup.smarthomeapp.news_detailed.NewsDetailedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val newsAdapter = NewsAdapter(getNewsClickListener())

    private lateinit var toolbarTitleTextView: TextView
    private var searchingNewsList: List<NewsApiData> = listOf()
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initViews()

        searchView.setOnSearchClickListener {
            toolbarTitleTextView.visibility = View.GONE
        }
        searchView.setOnCloseListener {
            toolbarTitleTextView.visibility = View.VISIBLE
            false
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        swipeRefreshLayout.setOnRefreshListener {
            newsAdapter.clearAll()
            loadApiData()
        }

        loadApiData()
        queryInSearchView()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.activity_news_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = newsAdapter
        backButton = findViewById(R.id.activity_news_back_button)
        progressBar = findViewById(R.id.activity_news_list_progress_bar)
        recyclerView.visibility = View.INVISIBLE

        toolbarTitleTextView = findViewById(R.id.activity_news_list_toolbar_text_view)
        searchView = findViewById(R.id.activity_news_list_toolbar_search_view)
        swipeRefreshLayout = findViewById(R.id.activity_news_swipe_refresh)

        val magId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val magTextView = searchView.findViewById<TextView>(magId)
        magTextView.setTextColor(Color.WHITE)
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getNewsList("0").enqueue(object: Callback<List<NewsApiData>> {
            override fun onResponse(
                call: Call<List<NewsApiData>>,
                response: Response<List<NewsApiData>>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {

                    val newsApiDataResponseList: MutableList<NewsApiData> = mutableListOf()
                    val list = response.body()!!
                    newsApiDataResponseList.addAll(list)
                    searchingNewsList = list
                    newsAdapter.setList(newsApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<NewsApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@NewsActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()

                val queryText = p0?.lowercase()


                newsAdapter.filter(queryText!!)


                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

                val newNewsList : MutableList<NewsApiData> = mutableListOf()
                for (q in searchingNewsList) {
                    if (q.title?.lowercase()?.contains(queryText!!)!!) {
                        newNewsList.add(q)
                    }
                }
                newsAdapter.setList(newNewsList)

                return false
            }
        })
    }


    private fun getNewsClickListener(): NewsClickListener {
        return object: NewsClickListener {
            override fun onNewsClick(id: String?) {
                val intent = Intent(this@NewsActivity, NewsDetailedActivity::class.java)
                intent.putExtra("one_news_id", id)
                startActivity(intent)
            }
        }
    }
}