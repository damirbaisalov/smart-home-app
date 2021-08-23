package kz.bfgroup.smarthomeapp.my_requests

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import kz.bfgroup.smarthomeapp.registration.GENERATED_HOME_ID
import kz.bfgroup.smarthomeapp.registration.GENERATED_KSK_ID
import kz.bfgroup.smarthomeapp.registration.GENERATED_USER_ID
import kz.bfgroup.smarthomeapp.registration.MY_APP
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewRequestActivity : AppCompatActivity() {

    private lateinit var newRequestHeading: EditText
    private lateinit var newRequestText: EditText
    private lateinit var sendNewRequestButton: TextView
    private lateinit var backButton: ImageButton
//    private val builder = AlertDialog.Builder(this)
    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

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
                sendRequest()
            }
        }
    }

    private fun initViews() {
        newRequestHeading = findViewById(R.id.activity_new_request_heading)
        newRequestText = findViewById(R.id.activity_new_request_text)
        sendNewRequestButton = findViewById(R.id.activity_new_request_send_button)
        backButton = findViewById(R.id.activity_my_request_back_button)
    }

    private fun sendRequest() {
        ApiRetrofit.getApiClient().sendNewRequest(fields).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
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
//                progressBar.visibility = View.GONE
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