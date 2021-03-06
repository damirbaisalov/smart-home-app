package kz.smart.house.my_requests

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kz.smart.house.R
import kz.smart.house.common.LoadingDialog
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.my_home.models.HomeApiData
import kz.smart.house.registration.GENERATED_HOME_ID
import kz.smart.house.registration.GENERATED_KSK_ID
import kz.smart.house.registration.GENERATED_USER_ID
import kz.smart.house.registration.MY_APP
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewRequestActivity : AppCompatActivity() {

    private lateinit var newRequestHeading: EditText
    private lateinit var newRequestText: EditText
    private lateinit var sendNewRequestButton: TextView
    private lateinit var backButton: ImageButton
    private lateinit var newRequestAddress: TextView
//    private val builder = AlertDialog.Builder(this)
    private lateinit var fields: Map<String, String>
    private lateinit var searchView: SearchView

    private val loadingDialog: LoadingDialog = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()

        sendNewRequestButton.setOnClickListener {

            val requestTitleTrim = newRequestHeading.text.toString().trim()
            val requestMessageTrim = newRequestText.text.toString().trim()

            if (requestTitleTrim.isEmpty() || requestMessageTrim.isEmpty()) {
                Toast.makeText(this@NewRequestActivity, "Заполните необходимые поля", Toast.LENGTH_LONG).show()
            } else {
                fields = mutableMapOf(
                    "heading" to requestTitleTrim,
                    "message_text" to requestMessageTrim,
                    "apply_home_id" to getSavedHomeId(),
                    "apply_ksk_id" to getSavedKskId(),
                    "apply_tenants_id" to getSavedUserId()
                )

                loadingDialog.startLoadingDialog()
                sendRequest()
            }
        }
    }

    private fun initViews() {
        newRequestHeading = findViewById(R.id.activity_new_request_heading)
        newRequestText = findViewById(R.id.activity_new_request_text)
        sendNewRequestButton = findViewById(R.id.activity_new_request_send_button)
        backButton = findViewById(R.id.activity_my_request_back_button)
        newRequestAddress = findViewById(R.id.activity_new_request_address)
        searchView = findViewById(R.id.activity_my_request_toolbar_search_view)
        searchView.visibility = View.GONE
    }

    private fun sendRequest() {
        ApiRetrofit.getApiClient().sendNewRequest(fields).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                loadingDialog.dialogDismiss()
                if (response.isSuccessful) {
                    if (response.body()?.string()=="Введите все поля!"){
                        Toast.makeText(this@NewRequestActivity, response.body()?.string(), Toast.LENGTH_LONG).show()
                    } else {
                        val dialogFragment = SuccessNewRequestDialogFragment()
                        val fragmentManager = supportFragmentManager

                        val transaction = fragmentManager.beginTransaction()
                        dialogFragment.show(transaction, "dialog")

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dialogDismiss()
                Toast.makeText(this@NewRequestActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun loadApiData() {
        ApiRetrofit.getApiClient().getMyHomeAddress(getSavedHomeId()).enqueue(object: Callback<HomeApiData> {
            override fun onResponse(
                call: Call<HomeApiData>,
                response: Response<HomeApiData>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    newRequestAddress.text = (responseBody.street + ", " + responseBody.nomer)
                }
            }

            override fun onFailure(call: Call<HomeApiData>, t: Throwable) {
                Toast.makeText(this@NewRequestActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
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

    private fun getSavedKskId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_KSK_ID, "default") ?: "default"
    }
}