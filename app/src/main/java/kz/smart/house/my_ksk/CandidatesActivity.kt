package kz.smart.house.my_ksk

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.my_ksk.models.CandidatesApiData
import kz.smart.house.my_ksk.models.MyKskApiData
import kz.smart.house.my_ksk.view.CandidateAdapter
import kz.smart.house.registration.GENERATED_HOME_ID
import kz.smart.house.registration.GENERATED_KSK_ID
import kz.smart.house.registration.MY_APP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidatesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private lateinit var progressBar1TextView: TextView
    private lateinit var progressBar2TextView: TextView

    private lateinit var kskTitle: TextView
    private val candidatesAdapter = CandidateAdapter()

    private lateinit var progressBar: ProgressBar
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidates)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadMyKskData()
        loadApiData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.activity_candidates_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = candidatesAdapter
        kskTitle = findViewById(R.id.activity_candidates_my_ksk_title)
        kskTitle.text = intent.getStringExtra("ksk_name")
        backButton = findViewById(R.id.activity_my_ksk_back_button)
//        progressBar = findViewById(R.id.activity_news_list_progress_bar)
//        recyclerView.visibility = View.INVISIBLE
        progressBar1 = findViewById(R.id.candidates_activity_progressBar1)
        progressBar2 = findViewById(R.id.candidates_activity_progressBar2)
        progressBar1TextView = findViewById(R.id.candidates_activity_progress_bar1_text_inside)
        progressBar2TextView = findViewById(R.id.candidates_activity_progress_bar2_text_inside)
        progressBar = findViewById(R.id.activity_candidates_progress_bar)
        nestedScrollView = findViewById(R.id.activity_candidates_nested_scroll)
        nestedScrollView.visibility = View.INVISIBLE
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getAllCandidates(getSavedHomeId()).enqueue(object: Callback<List<CandidatesApiData>> {
            override fun onResponse(
                call: Call<List<CandidatesApiData>>,
                response: Response<List<CandidatesApiData>>
            ) {
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val candidatesApiDataResponseList: MutableList<CandidatesApiData> = mutableListOf()
                    val list = response.body()!!
                    candidatesApiDataResponseList.addAll(list)
                    candidatesAdapter.setList(candidatesApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<CandidatesApiData>>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@CandidatesActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadMyKskData() {
        ApiRetrofit.getApiClient().getMyKsk(getSavedKskId()).enqueue(object: Callback<MyKskApiData> {
            override fun onResponse(
                call: Call<MyKskApiData>,
                response: Response<MyKskApiData>
            ) {
                progressBar.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val responseBody = response.body()!!

                    val kskRatingLiked = responseBody.reiting?.toDouble()?.div(0.05)
                    val kskRatingNotLiked = 100 - kskRatingLiked!!
                    progressBar1.progress = kskRatingLiked.toInt()
                    progressBar2.progress = kskRatingNotLiked.toInt()

                    progressBar1TextView.text = ("${progressBar1.progress}%")
                    progressBar2TextView.text = ("${progressBar2.progress}%")

                }
            }

            override fun onFailure(call: Call<MyKskApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CandidatesActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getSavedKskId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_KSK_ID, "default") ?: "default"
    }

    private fun getSavedHomeId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_HOME_ID, "default") ?: "default"
    }
}