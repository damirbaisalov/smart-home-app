package kz.bfgroup.smarthomeapp.my_requests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewRequestActivity : AppCompatActivity() {

    private lateinit var newRequestHeading: EditText
    private lateinit var newRequestText: EditText
    private lateinit var sendNewRequestButton: TextView
    private lateinit var backButton: ImageButton
    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)

        initViews()

        backButton.setOnClickListener {
            onBackPressed()
        }

        sendNewRequestButton.setOnClickListener {
            if (newRequestHeading.text.toString().trim().isEmpty() || newRequestText.text.toString().trim().isEmpty()) {
                Toast.makeText(this@NewRequestActivity, "Заполните необходимые поля", Toast.LENGTH_LONG).show()
            } else {
                fields = mutableMapOf(
                    "heading" to newRequestHeading.text.toString(),
                    "message_text" to newRequestText.text.toString(),
                    "apply_home_id" to "1020",
                    "apply_ksk_id" to "121",
                    "apply_tenants_id" to "59"
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
                        Toast.makeText(this@NewRequestActivity, response.body()?.string(), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@NewRequestActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}