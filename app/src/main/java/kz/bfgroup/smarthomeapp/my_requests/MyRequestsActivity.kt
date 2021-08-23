package kz.bfgroup.smarthomeapp.my_requests

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.my_ksk.models.CandidatesApiData
import kz.bfgroup.smarthomeapp.my_ksk.view.CandidateAdapter
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import kz.bfgroup.smarthomeapp.my_requests.view.RequestAdapter
import kz.bfgroup.smarthomeapp.registration.GENERATED_HOME_ID
import kz.bfgroup.smarthomeapp.registration.GENERATED_USER_ID
import kz.bfgroup.smarthomeapp.registration.MY_APP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var openNewRequestActivity: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fields: Map<String,String>
    private val myRequestAdapter = RequestAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        openNewRequestActivity.setOnClickListener {
            val intent = Intent(this, NewRequestActivity::class.java)
            startActivity(intent)
        }

        loadApiData(fields)
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

        fields = mutableMapOf(
            "zayavki_tenant_id" to getSavedUserId(),
            "zayavki_home_id" to getSavedHomeId()
        )
    }

    private fun loadApiData(fields: Map<String,String>) {
        ApiRetrofit.getApiClient().getMyRequests(fields).enqueue(object:
            Callback<List<MyRequestApiData>> {
            override fun onResponse(
                call: Call<List<MyRequestApiData>>,
                response: Response<List<MyRequestApiData>>
            ) {
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val requestsApiDataResponseList: MutableList<MyRequestApiData> = mutableListOf()
                    val list = response.body()!!
                    requestsApiDataResponseList.addAll(list)
                    myRequestAdapter.setList(requestsApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<MyRequestApiData>>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@MyRequestsActivity, t.message, Toast.LENGTH_LONG).show()
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