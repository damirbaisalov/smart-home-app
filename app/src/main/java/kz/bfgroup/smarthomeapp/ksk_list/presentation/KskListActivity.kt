package kz.bfgroup.smarthomeapp.ksk_list.presentation

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
import kz.bfgroup.smarthomeapp.data.ApiRetrofit2
import kz.bfgroup.smarthomeapp.ksk_detailed.KskDetailActivity
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskItemClickListener
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskListAdapter
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollDownButton: ImageButton
    private lateinit var scrollUpButton: ImageButton
    private var clicked = false
    private var count = 0
    private val kskListAdapter = KskListAdapter(getKskItemClickListener())
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var toolbarTitleTextView: TextView
    private var searchingKskList: List<KskApiData> = listOf()
    private lateinit var tempArrayList : ArrayList<KskApiData>
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksk_list)

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
            kskListAdapter.clearAll()
            loadApiData()
        }

        loadApiData()

        queryInSearchView()

        scrollDownButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(count-1)
            clicked=true
        }
        scrollUpButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            clicked=false
        }

        if (clicked) {
            scrollUpButton.visibility = View.VISIBLE
            scrollDownButton.visibility = View.GONE
        } else {
            scrollUpButton.visibility = View.GONE
            scrollDownButton.visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.activity_ksk_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = kskListAdapter
        backButton = findViewById(R.id.activity_ksk_list_back_button)
        progressBar = findViewById(R.id.activity_ksk_list_progress_bar)
        recyclerView.visibility = View.INVISIBLE
        scrollDownButton = findViewById(R.id.scroll_down_recyclerview)
        scrollUpButton = findViewById(R.id.scroll_up_recyclerview)

        toolbarTitleTextView = findViewById(R.id.activity_ksk_list_toolbar_text_view)
        searchView = findViewById(R.id.activity_ksk_list_toolbar_search_view)
        val magId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val magTextView = searchView.findViewById<TextView>(magId)
        magTextView.setTextColor(Color.WHITE)
        swipeRefreshLayout = findViewById(R.id.activity_ksk_list_swipe_refresh)
    }

    private fun loadApiData() {
        ApiRetrofit2.getApiClient().getKskList().enqueue(object: Callback<List<KskApiData>> {
            override fun onResponse(
                call: Call<List<KskApiData>>,
                response: Response<List<KskApiData>>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {

                    val kskApiDataResponseList: MutableList<KskApiData> = mutableListOf()
                    val list = response.body()!!
                    kskApiDataResponseList.addAll(list)
                    searchingKskList = list
                    kskListAdapter.setList(kskApiDataResponseList)
                    count = list.size
                }
            }

            override fun onFailure(call: Call<List<KskApiData>>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                progressBar.visibility = View.GONE
                Toast.makeText(this@KskListActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()

                val queryText = p0?.lowercase()


                kskListAdapter.filter(queryText!!)


                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

//                cafeAdapter?.filter(queryText!!)
//
//                if (queryText?.isEmpty()!!)
//                    cafeAdapter?.setList(searchingCafeList)

                val newKskList : MutableList<KskApiData> = mutableListOf()
                for (q in searchingKskList) {
                    if (q.kskName?.lowercase()?.contains(queryText!!)!!) {
                        newKskList.add(q)
                    }
                }
                kskListAdapter.setList(newKskList)

                return false
            }
        })
    }

    private fun getKskItemClickListener(): KskItemClickListener {
        return object: KskItemClickListener {
            override fun onKskClick(id: String?) {
                val intent = Intent(this@KskListActivity, KskDetailActivity::class.java)
                intent.putExtra("ksk_info_id", id)
                startActivity(intent)
            }
        }
    }
}