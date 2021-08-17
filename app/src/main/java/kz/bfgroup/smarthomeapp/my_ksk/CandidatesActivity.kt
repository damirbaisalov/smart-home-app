package kz.bfgroup.smarthomeapp.my_ksk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidatesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var kskTitle: TextView
    private val candidatesAdapter = CandidateAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidates)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

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
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getAllCandidates("1020").enqueue(object: Callback<List<CandidatesApiData>> {
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
}