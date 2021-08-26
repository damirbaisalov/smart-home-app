package kz.bfgroup.smarthomeapp.my_requests

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import kz.bfgroup.smarthomeapp.my_requests.view.RequestAdapter
import kz.bfgroup.smarthomeapp.registration.GENERATED_HOME_ID
import kz.bfgroup.smarthomeapp.registration.GENERATED_USER_ID
import kz.bfgroup.smarthomeapp.registration.MY_APP
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var openNewRequestActivity: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var fields: Map<String,String>
    private val myRequestAdapter = RequestAdapter()
    private lateinit var toolbarTitleTextView: TextView
    private var searchingRequestList: List<MyRequestApiData> = listOf()
    private lateinit var searchView: SearchView
    private lateinit var emptyRequestListTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

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

        openNewRequestActivity.setOnClickListener {
            val intent = Intent(this, NewRequestActivity::class.java)
            startActivity(intent)
        }

        swipeRefreshLayout.setOnRefreshListener {
            myRequestAdapter.clearAll()
            loadApiData(fields)
        }

        loadApiData(fields)
        queryInSearchView()
    }

    private fun initViews(){
        backButton = findViewById(R.id.activity_my_request_back_button)
        openNewRequestActivity = findViewById(R.id.activity_my_request_new_request)
        recyclerView = findViewById(R.id.activity_my_request_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = myRequestAdapter

        progressBar = findViewById(R.id.activity_my_request_progress_bar)
        fields = mutableMapOf(
            "zayavki_tenant_id" to getSavedUserId(),
            "zayavki_home_id" to getSavedHomeId()
        )
        swipeRefreshLayout = findViewById(R.id.activity_my_request_refresh)
        toolbarTitleTextView = findViewById(R.id.activity_my_request_toolbar_text_view)
        searchView = findViewById(R.id.activity_my_request_toolbar_search_view)
        emptyRequestListTextView = findViewById(R.id.my_request_list_empty_text_view)
    }

    private fun loadApiData(fields: Map<String,String>) {
        ApiRetrofit.getApiClient().getMyRequests(fields).enqueue(object:
            Callback<List<MyRequestApiData>> {
            override fun onResponse(
                call: Call<List<MyRequestApiData>>,
                response: Response<List<MyRequestApiData>>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {

                    val requestsApiDataResponseList: MutableList<MyRequestApiData> = mutableListOf()
                    val list = response.body()!!
                    requestsApiDataResponseList.addAll(list)
                    searchingRequestList = list
                    myRequestAdapter.setList(requestsApiDataResponseList)
                    if (list.isEmpty()) {
                        emptyRequestListTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    } else {
                        emptyRequestListTextView.visibility = View.INVISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<List<MyRequestApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@MyRequestsActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()

                val queryText = p0?.lowercase()


                myRequestAdapter.filter(queryText!!)


                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

//                cafeAdapter?.filter(queryText!!)
//
//                if (queryText?.isEmpty()!!)
//                    cafeAdapter?.setList(searchingCafeList)

                val newMyRequestList : MutableList<MyRequestApiData> = mutableListOf()
                for (q in searchingRequestList) {
                    if (q.heading?.lowercase()?.contains(queryText!!)!!) {
                        newMyRequestList.add(q)
                    }
                }
                myRequestAdapter.setList(newMyRequestList)

                return false
            }
        })
    }

    override fun onStart() {
        super.onStart()
        loadApiData(fields)
    }

    private fun getSavedUserId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_USER_ID, "default") ?: "default"
    }

    private fun getSavedHomeId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_HOME_ID, "default") ?: "default"
    }
}