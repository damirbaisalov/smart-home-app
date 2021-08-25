package kz.bfgroup.smarthomeapp.ksk_detailed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.data.ApiRetrofit2
import kz.bfgroup.smarthomeapp.ksk_detailed.models.KskDetailedApiData
import kz.bfgroup.smarthomeapp.ksk_detailed.view.KskAddressesAdapter
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class KskDetailActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var kskToolbarTitle: TextView
    private lateinit var kskTitle: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var kskDetailLineaLayout: LinearLayout
    private var kskAddressAdapter = KskAddressesAdapter()

    private lateinit var kskDirectorName: TextView
    private lateinit var kskDirectorTelephone: TextView
    private lateinit var kskDirectorPhone: TextView
    private var kskIdFromKskList: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksk_detail)
        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()
    }

    private fun initViews() {
        backButton = findViewById(R.id.activity_ksk_detail_back_button)
        kskToolbarTitle = findViewById(R.id.ksk_detail_toolbar_ksk_title)
        kskTitle = findViewById(R.id.activity_ksk_detail_ksk_name_text_view)
        recyclerView = findViewById(R.id.activity_ksk_detail_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = kskAddressAdapter
        progressBar = findViewById(R.id.activity_ksk_detail_progressbar)
        kskDirectorName = findViewById(R.id.activity_ksk_detail_director_name)
        kskDirectorTelephone = findViewById(R.id.activity_ksk_detail_telephone)
        kskDirectorPhone = findViewById(R.id.activity_ksk_detail_phone)
        kskDetailLineaLayout = findViewById(R.id.activity_ksk_detail_linear_layout)
        kskIdFromKskList = intent.getStringExtra("ksk_info_id")
        kskDetailLineaLayout.visibility = View.INVISIBLE
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getKskById(kskIdFromKskList!!).enqueue(object: Callback<KskDetailedApiData> {
            override fun onResponse(
                call: Call<KskDetailedApiData>,
                response: Response<KskDetailedApiData>
            ) {
                progressBar.visibility = View.GONE
                kskDetailLineaLayout.visibility = View.VISIBLE

                if (response.isSuccessful) {

                    val responseBody = response.body()!!
                    kskToolbarTitle.text = responseBody.kskName
                    kskTitle.text = responseBody.kskName
                    kskDirectorName.text = responseBody.director_full_name

                    val splitPhone = responseBody.phones?.split(",")
                    kskDirectorTelephone.text = splitPhone?.first() ?: "Нет данных"
                    kskDirectorPhone.text = ("р. тел. " + splitPhone?.last())

                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!.addrs
                    streetApiDataResponseList.addAll(list)
                    kskAddressAdapter.setList(streetApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<KskDetailedApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                makeToast(t.message!!)
            }

        })
    }

    private fun makeToast(dataToShow: String) {
        val toast = Toast.makeText(this, dataToShow, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}