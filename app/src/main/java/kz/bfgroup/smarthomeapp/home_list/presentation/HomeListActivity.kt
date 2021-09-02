package kz.bfgroup.smarthomeapp.home_list.presentation

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
import kz.bfgroup.smarthomeapp.home_list.presentation.view.HomeListAdapter
import kz.bfgroup.smarthomeapp.registration.NomerListDialogFragment
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.StreetClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeListActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private var homeListAdapter = HomeListAdapter(getStreetClickListener())
    private lateinit var searchView: SearchView
    private var searchingStreetList: List<StreetApiData> = listOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbarTitleTextView: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_list)
        bindViews()

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
            homeListAdapter.clearAll()
            loadApiData()
        }

        loadApiData()

        queryInSearchView()
    }

    private fun bindViews() {
        backButton = findViewById(R.id.activity_my_home_back_button)

        recyclerView = findViewById(R.id.activity_home_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = homeListAdapter
        swipeRefreshLayout = findViewById(R.id.activity_home_list_swipe_refresh)
        searchView = findViewById(R.id.activity_my_home_toolbar_search_view)
        progressBar = findViewById(R.id.activity_home_list_progress_bar)
        toolbarTitleTextView = findViewById(R.id.activity_my_home_toolbar_text_view)

        val magId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val magTextView = searchView.findViewById<TextView>(magId)
        magTextView.setTextColor(Color.WHITE)
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getUniqueStreetList("1").enqueue(object:
            Callback<List<StreetApiData>> {
            override fun onResponse(
                call: Call<List<StreetApiData>>,
                response: Response<List<StreetApiData>>
            ) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!
                    streetApiDataResponseList.addAll(list)
                    searchingStreetList = list
                    homeListAdapter.setList(streetApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@HomeListActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                val queryText = p0?.lowercase()

                homeListAdapter.filter(queryText!!)

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

                val newStreetList : MutableList<StreetApiData> = mutableListOf()
                for (q in searchingStreetList) {
                    val streetWithNomer = q.street?.lowercase()
                    if (streetWithNomer?.contains(queryText!!)!!) {
                        newStreetList.add(q)
                    }
                }
                homeListAdapter.setList(newStreetList)

                return false
            }
        })
    }

    private fun getStreetClickListener(): StreetClickListener {
        return object: StreetClickListener {
            override fun onClick(street: String?) {
                val bundle = Bundle()
                bundle.putString("selected_street", street)
                val homeNumberListDialogFragment = HomeNumberListDialogFragment()
                homeNumberListDialogFragment.arguments = bundle
                homeNumberListDialogFragment.show(supportFragmentManager, "homeNumberListDialogFragment")
            }
        }
    }
}