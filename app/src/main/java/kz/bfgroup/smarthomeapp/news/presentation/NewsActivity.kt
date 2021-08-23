package kz.bfgroup.smarthomeapp.news.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskItemClickListener
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskListAdapter
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsAdapter
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar
//    private lateinit var scrollDownButton: ImageButton
//    private lateinit var scrollUpButton: ImageButton
//    private var clicked = false
//    private var count = 0
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
//        scrollDownButton = findViewById(R.id.scroll_down_recyclerview)
//        scrollUpButton = findViewById(R.id.scroll_up_recyclerview)

        toolbarTitleTextView = findViewById(R.id.activity_news_list_toolbar_text_view)
        searchView = findViewById(R.id.activity_news_list_toolbar_search_view)
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getNewsList("0").enqueue(object: Callback<List<NewsApiData>> {
            override fun onResponse(
                call: Call<List<NewsApiData>>,
                response: Response<List<NewsApiData>>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val newsApiDataResponseList: MutableList<NewsApiData> = mutableListOf()
                    val list = response.body()!!
                    newsApiDataResponseList.addAll(list)
                    newsAdapter.setList(newsApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<NewsApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
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
                    if (q.title?.contains(queryText!!)!!) {
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
                Toast.makeText(this@NewsActivity, id, Toast.LENGTH_LONG).show()
            }
        }
    }
}