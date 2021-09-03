package kz.bfgroup.smarthomeapp.ksk_detailed

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
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
import kz.bfgroup.smarthomeapp.my_requests.SuccessNewRequestDialogFragment
import kz.bfgroup.smarthomeapp.registration.*
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

const val MY_APP_WITH_KSK_ID = "MY_APP_WITH_KSK_ID"
const val SELECTED_KSK_ID = "MY_APP_SELECTED_KSK_ID"
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

    private lateinit var saveKskButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksk_detail)
        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        saveKskButton.setOnClickListener {
            if (!saveKskButton.isSelected) {
                saveKskButton.text = "КСК сохранен"
                saveKskButton.setTextColor(Color.parseColor("#FFFFFFFF"))
                saveKskButton.isSelected = true
                saveSelectedKsk(kskIdFromKskList!!)

                val dialogFragment = SuccessSavedKskStateDialogFragment()
                val fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                dialogFragment.show(transaction, "dialog")

            } else {
                saveKskButton.text = "Сохранить КСК"
                saveKskButton.isSelected = false
                saveKskButton.setTextColor(Color.parseColor("#0C90FF"))
                deleteSelectedKsk()
            }
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

        saveKskButton = findViewById(R.id.activity_ksk_detail_save_button)


        if (intent.getBooleanExtra("save_button_invisible", false)) {
            saveKskButton.visibility = View.GONE
        }

        if (getSavedKskId() == kskIdFromKskList) {
            saveKskButton.isSelected = true
            saveKskButton.text = "КСК сохранен"
            saveKskButton.setTextColor(Color.parseColor("#FFFFFFFF"))
        } else {
            saveKskButton.text = "Сохранить Кск"
            saveKskButton.setTextColor(Color.parseColor("#0C90FF"))
            saveKskButton.isSelected = false
        }
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

    private fun getSavedKskId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(SELECTED_KSK_ID, "default") ?: "default"
    }

    private fun saveSelectedKsk(kskId: String) {
        val sharedPref = this.getSharedPreferences(MY_APP_WITH_KSK_ID, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(SELECTED_KSK_ID, kskId)
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

    private fun makeToast(dataToShow: String) {
        val toast = Toast.makeText(this, dataToShow, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}