package kz.bfgroup.smarthomeapp.ksk_list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskItemClickListener
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskListAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksk_list)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()

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
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getKskList().enqueue(object: Callback<List<KskApiData>> {
            override fun onResponse(
                call: Call<List<KskApiData>>,
                response: Response<List<KskApiData>>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val kskApiDataResponseList: MutableList<KskApiData> = mutableListOf()
                    val list = response.body()!!
                    kskApiDataResponseList.addAll(list)
                    kskListAdapter.setList(kskApiDataResponseList)
                    count = list.size
                }
            }

            override fun onFailure(call: Call<List<KskApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@KskListActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getKskItemClickListener(): KskItemClickListener {
        return object: KskItemClickListener {
            override fun onKskClick(id: String?) {
                Toast.makeText(this@KskListActivity, id, Toast.LENGTH_LONG).show()
            }
        }
    }
}