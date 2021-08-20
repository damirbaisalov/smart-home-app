package kz.bfgroup.smarthomeapp.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kz.bfgroup.smarthomeapp.MenuActivity
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.registration.*
import kz.bfgroup.smarthomeapp.registration.models.UserApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var userLoginEditText: EditText
    private lateinit var userPasswordEditText: EditText
    private lateinit var userSubmitDataButton: Button
    private lateinit var openRegistrationForm: LinearLayout
    private lateinit var loginFormLinearLayout: LinearLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var fields: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        openRegistrationForm.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        userSubmitDataButton.setOnClickListener {

            if (userLoginEditText.text.toString().isEmpty() || userPasswordEditText.text.toString().isEmpty()) {
                showToast("Заполните все поля")
            } else {

                progressBar.visibility = View.VISIBLE
                loginFormLinearLayout.visibility = View.INVISIBLE

                fields = mutableMapOf(
                    "tel_auth" to formatTelephoneNumber(userLoginEditText.text.toString()),
                    "password" to userPasswordEditText.text.toString()
                )
                sendUserData(fields)
            }
        }
    }

    private fun initViews() {
        userLoginEditText = findViewById(R.id.login_activity_user_telephone)
        userPasswordEditText = findViewById(R.id.login_activity_user_password)
        userSubmitDataButton = findViewById(R.id.login_activity_submit_user_data_button)
        openRegistrationForm = findViewById(R.id.open_registration_form)
        loginFormLinearLayout = findViewById(R.id.login_form_linear_layout)
        progressBar = findViewById(R.id.login_form_progress_bar)
    }

    private fun sendUserData(fields: Map<String, String>) {
        ApiRetrofit.getApiClient().userLogin(fields).enqueue(object: Callback<UserApiData> {
            override fun onResponse(call: Call<UserApiData>, response: Response<UserApiData>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val responseBody = response.body()!!

                    if (responseBody.mess_status == "0") {
                        showToast(responseBody.mess)
                        progressBar.visibility = View.GONE
                        loginFormLinearLayout.visibility = View.VISIBLE
                    } else {
                        saveUserDataNeeded(
                            response.body()!!.serial_number!!,
                            response.body()!!.id!!,
                            response.body()!!.home_id!!,
                            response.body()!!.ksk_id!!
                        )
                        val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<UserApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                loginFormLinearLayout.visibility = View.VISIBLE
                showToast(t.message)
            }

        })
    }

    private fun showToast(text: String?) {
        Toast.makeText(this@LoginActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun formatTelephoneNumber(tel: String): String {
        var telephoneNumber = tel.replace("(","")
        telephoneNumber = telephoneNumber.replace(")", "")
        telephoneNumber = telephoneNumber.replace("-","")
        telephoneNumber = telephoneNumber.replace("+","")

        return telephoneNumber
    }

    private fun saveUserDataNeeded(token: String, userId: String, homeId: String, kskId: String) {
        val sharedPref = this.getSharedPreferences(MY_APP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(GENERATED_ACCESS_TOKEN, token)
        editor.putString(GENERATED_USER_ID, userId)
        editor.putString(GENERATED_HOME_ID, homeId)
        editor.putString(GENERATED_KSK_ID, kskId)
        editor.apply()
    }
}