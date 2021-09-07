package kz.smart.house.my_ksk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kz.smart.house.R
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.my_ksk.models.MyKskApiData
import kz.smart.house.registration.GENERATED_KSK_ID
import kz.smart.house.registration.MY_APP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyKskActivity : AppCompatActivity() {

    private lateinit var myKskTitle: TextView
    private lateinit var myKskEmployeeNum: TextView
    private lateinit var myKskPhones: TextView
    private lateinit var myKskDirectorName: TextView
    private lateinit var myKskHomeCount: TextView
    private lateinit var backButton: ImageButton
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private lateinit var progressBar1TextView: TextView
    private lateinit var progressBar2TextView: TextView
    private lateinit var openVoteListButton: TextView
    private lateinit var kskTitle: String

    private lateinit var progressBar: ProgressBar
    private lateinit var myKskLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ksk)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        openVoteListButton.setOnClickListener {
            val intent = Intent(this, CandidatesActivity::class.java)
            intent.putExtra("ksk_name", kskTitle)
            startActivity(intent)
        }


        loadApiData()
        loadApiData2()

    }

    private fun initViews() {
        myKskTitle = findViewById(R.id.my_ksk_title)
        myKskEmployeeNum = findViewById(R.id.my_ksk_employee_num)
        myKskPhones = findViewById(R.id.my_ksk_contacts)
        myKskDirectorName = findViewById(R.id.my_ksk_predsedatel)
        myKskHomeCount = findViewById(R.id.my_ksk_home_count)
        backButton = findViewById(R.id.activity_my_ksk_back_button)
        progressBar1 = findViewById(R.id.progressBar1)
        progressBar1.max = 100
        progressBar2 = findViewById(R.id.progressBar2)
        progressBar2.max = 100
        progressBar1TextView = findViewById(R.id.progress_bar1_text_inside)
        progressBar2TextView = findViewById(R.id.progress_bar2_text_inside)
        openVoteListButton = findViewById(R.id.open_vote_list_button)
        progressBar = findViewById(R.id.activity_my_ksk_progress_bar)
        myKskLayout = findViewById(R.id.my_ksk_layout)
        myKskLayout.visibility = View.INVISIBLE
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getMyKsk(getSavedKskId()).enqueue(object: Callback<MyKskApiData> {
            override fun onResponse(
                call: Call<MyKskApiData>,
                response: Response<MyKskApiData>
            ) {
                progressBar.visibility = View.GONE
                myKskLayout.visibility = View.VISIBLE
                if (response.isSuccessful) {

                    val responseBody = response.body()!!

                    myKskTitle.text = responseBody.kskName
                    kskTitle = responseBody.kskName.toString()

                    myKskEmployeeNum.text = ("Численность сотдруников"+"\n"+"12")
                    myKskPhones.text = ("Контакты:" + "\n" + "тел."+responseBody.phones)
                    myKskDirectorName.text = ("Председатель:"+"\n"+responseBody.director_full_name)


                    val kskRatingLiked = responseBody.reiting?.toDouble()?.div(0.05)
                    val kskRatingNotLiked = 100 - kskRatingLiked!!
                    progressBar1.progress = kskRatingLiked.toInt()
                    progressBar2.progress = kskRatingNotLiked.toInt()

                    progressBar1TextView.text = ("${progressBar1.progress}%")
                    progressBar2TextView.text = ("${progressBar2.progress}%")

                }
            }

            override fun onFailure(call: Call<MyKskApiData>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@MyKskActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadApiData2() {
        ApiRetrofit.getApiClient().getKskHomeNum(getSavedKskId()).enqueue(object: Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
//                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val responseBody = response.body()!!

                    myKskHomeCount.text = ("Количество домов КСК:\n$responseBody")

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MyKskActivity, t.message, Toast.LENGTH_LONG).show()
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
}