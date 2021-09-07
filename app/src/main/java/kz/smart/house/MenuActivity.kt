package kz.smart.house

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.data.ApiRetrofit2
import kz.smart.house.home_list.presentation.HomeListActivity
import kz.smart.house.ksk_detailed.KskDetailActivity
import kz.smart.house.ksk_detailed.MY_APP_WITH_KSK_ID
import kz.smart.house.ksk_detailed.SELECTED_KSK_ID
import kz.smart.house.ksk_list.presentation.KskListActivity
import kz.smart.house.login.LoginActivity
import kz.smart.house.my_home.MyHomeActivity
import kz.smart.house.my_home.SELECTED_HOME_ID
import kz.smart.house.my_home.SELECTED_HOME_NUMBER
import kz.smart.house.my_home.SELECTED_HOME_STREET
import kz.smart.house.my_ksk.MyKskActivity
import kz.smart.house.my_requests.MyRequestsActivity
import kz.smart.house.news.models.NewsApiData
import kz.smart.house.news.presentation.NewsActivity
import kz.smart.house.news.presentation.view.NewsAdapter
import kz.smart.house.news.presentation.view.NewsClickListener
import kz.smart.house.news_detailed.NewsDetailedActivity
import kz.smart.house.registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    private lateinit var logOutImageButton: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val newsAdapter = NewsAdapter(getNewsClickListener())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val theme = AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(theme)
        initViews()

        if (getSavedSerialNumber()=="default") {
            logOutImageButton.visibility = View.INVISIBLE
        } else {
            logOutImageButton.visibility = View.VISIBLE
        }

        val openNewsTextView = findViewById<LinearLayout>(R.id.open_news_activity)
        openNewsTextView.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        val openMyKskActivity = findViewById<LinearLayout>(R.id.open_my_ksk_activity)
        openMyKskActivity.setOnClickListener {

            if (getSavedSerialNumber()=="default" && getSavedKskId() == "default"){
                val intent = Intent(this, KskListActivity::class.java)
                startActivity(intent)
            } else if (getSavedSerialNumber() == "default" && getSavedKskId() != "default" ) {
                val intent = Intent(this, KskDetailActivity::class.java)
                intent.putExtra("ksk_info_id", getSavedKskId())
                startActivity(intent)
            } else if (getSavedSerialNumber() != "default") {
                val intent = Intent(this, MyKskActivity::class.java)
                startActivity(intent)
            }
        }

        val openMyHomeActivity = findViewById<LinearLayout>(R.id.open_my_home_activity)
        openMyHomeActivity.setOnClickListener {
            if (getSavedSerialNumber()=="default" && getSelectedHomeId()=="default"){
                val intent = Intent(this, HomeListActivity::class.java)
                startActivity(intent)
            } else if (getSavedSerialNumber() == "default" && getSelectedHomeId() != "default"){
                val intent = Intent(this, MyHomeActivity::class.java)
                startActivity(intent)
            } else if (getSavedSerialNumber() != "default") {
                val intent = Intent(this, MyHomeActivity::class.java)
                startActivity(intent)
            }
        }

        val openMyRequestActivity = findViewById<LinearLayout>(R.id.open_my_request_activity)
        openMyRequestActivity.setOnClickListener {
            if (getSavedSerialNumber()=="default"){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MyRequestsActivity::class.java)
                startActivity(intent)
            }
        }

        val openKskListTextView = findViewById<LinearLayout>(R.id.open_ksk_list)
        openKskListTextView.setOnClickListener {
            val intent = Intent(this, KskListActivity::class.java)
            intent.putExtra("clicked_from_open_ksk_list", true)
            startActivity(intent)
        }

        logOutImageButton = findViewById(R.id.activity_menu_toolbar_logout)
        logOutImageButton.setOnClickListener {
            deleteSavedSerialNumber()
            deleteSavedHomeId()
            deleteSavedKskId()
            deleteSavedUserId()
            deleteSelectedHome()
            deleteSelectedKsk()
            deleteSelectedHomeStreet()
            deleteSelectedHomeNumber()
            finish()
            startActivity(intent)
        }

        swipeRefreshLayout.setOnRefreshListener {
            newsAdapter.clearAll()
            loadApiData()
        }

        loadApiData()
    }

    private fun initViews() {
        logOutImageButton = findViewById(R.id.activity_menu_toolbar_logout)

        recyclerView = findViewById(R.id.activity_menu_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = newsAdapter
        progressBar = findViewById(R.id.activity_menu_list_progress_bar)
        recyclerView.visibility = View.INVISIBLE

        swipeRefreshLayout = findViewById(R.id.activity_menu_swipe_refresh)

    }

    private fun loadApiData() {
        ApiRetrofit2.getApiClient().getNews().enqueue(object: Callback<List<NewsApiData>> {
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
                    newsAdapter.setList(newsApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<NewsApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@MenuActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getNewsClickListener(): NewsClickListener {
        return object: NewsClickListener {
            override fun onNewsClick(id: String?) {
                val intent = Intent(this@MenuActivity, NewsDetailedActivity::class.java)
                intent.putExtra("one_news_id", id)
                startActivity(intent)
            }
        }
    }

    private fun getSavedKskId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(SELECTED_KSK_ID, "default") ?: "default"
    }

    private fun getSelectedHomeId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(SELECTED_HOME_ID, "default") ?: "default"
    }

    private fun getSavedSerialNumber(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_ACCESS_TOKEN, "default") ?: "default"
    }

    private fun deleteSavedSerialNumber() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(GENERATED_ACCESS_TOKEN)
        editor.apply()
    }

    private fun deleteSavedHomeId() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(GENERATED_HOME_ID)
        editor.apply()
    }

    private fun deleteSavedKskId() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(GENERATED_KSK_ID)
        editor.apply()
    }

    private fun deleteSavedUserId() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(GENERATED_USER_ID)
        editor.apply()
    }

    private fun deleteSelectedHome() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(SELECTED_HOME_ID)
        editor.apply()
    }

    private fun deleteSelectedKsk() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(SELECTED_KSK_ID)
        editor.apply()
    }

    private fun deleteSelectedHomeStreet() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(SELECTED_HOME_STREET)
        editor.apply()
    }

    private fun deleteSelectedHomeNumber() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(SELECTED_HOME_NUMBER)
        editor.apply()
    }
}