package kz.bfgroup.smarthomeapp.registration

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegistrationActivity : AppCompatActivity(), StreetListDialogFragment.OnInputNewListener {

    private lateinit var userFIOEditText: EditText
    private lateinit var userIINEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var userBirthdayEditText: EditText
    private lateinit var userAddressEditText: EditText
    private lateinit var userApartmentEditText: EditText
    private lateinit var userEntranceEditText: EditText
    private lateinit var userTelephoneEditText: EditText
    private lateinit var userPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var fields: Map<String, String>
    private val bundle = Bundle()
    private var okRequest = false
    private lateinit var userPhoneFormatted: String
    private lateinit var userNameSurnameFormatted: String
    private lateinit var userDadNameFormatted: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initViews()

        userAddressEditText.isFocusable = false
        userAddressEditText.setOnClickListener {
            val dialog = StreetListDialogFragment()
            dialog.show(supportFragmentManager, "streetListDialogFragment")
        }

        userBirthdayEditText.isFocusable = false
        userBirthdayEditText.setOnClickListener {
            val calendarData = Calendar.getInstance()
            val day = calendarData.get(Calendar.DAY_OF_MONTH)
            val month = calendarData.get(Calendar.MONTH)
            val year = calendarData.get(Calendar.YEAR)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    userBirthdayEditText.setText(""+ dayOfMonth + "." + (monthOfYear + 1) + "." + year)
                },
                year,
                month,
                day
            )

            dpd.show()
        }

        registerButton.setOnClickListener {
            userPhoneFormatted = userTelephoneEditText.text.toString()
            userPhoneFormatted = userPhoneFormatted.substring(1,userPhoneFormatted.length)

            val list = userFIOEditText.text.toString().split(" ")
            userNameSurnameFormatted = list[0] + " " + list[1]
            userDadNameFormatted = list[2]

            bundle.putString("user_fullname", userNameSurnameFormatted)
            bundle.putString("user_dadname", userDadNameFormatted)
            bundle.putString("user_iin", userIINEditText.text.toString())
            bundle.putString("user_email", userEmailEditText.text.toString())
            bundle.putString("user_birthday", userBirthdayEditText.text.toString())
            bundle.putString("user_apartment", userApartmentEditText.text.toString())
            bundle.putString("user_entrance", userEntranceEditText.text.toString())
            bundle.putString("user_telephone", userPhoneFormatted)
            bundle.putString("user_password", userPasswordEditText.text.toString())

            sendSmsCode()
            val dialog = CodeDialogFragment()
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "codeDialog")

        }
    }

    private fun initViews() {
        userFIOEditText = findViewById(R.id.user_name_surname)
        userIINEditText = findViewById(R.id.user_iin)
        userEmailEditText = findViewById(R.id.user_email)
        userBirthdayEditText = findViewById(R.id.user_birthday)
        userAddressEditText = findViewById(R.id.user_street)
        userApartmentEditText = findViewById(R.id.user_apartment)
        userEntranceEditText = findViewById(R.id.user_entrance)
        userTelephoneEditText = findViewById(R.id.user_telephone)
        userPasswordEditText = findViewById(R.id.user_password)
        registerButton = findViewById(R.id.submit_user_data_button)
    }

    private fun sendSmsCode() {
        ApiRetrofit.getApiClient().sendSMSCode(userPhoneFormatted).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    showToast(response.body()!!.string())
//                    okRequest = response.body()!!.string() == "Ok"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                okRequest = false
            }
        })
    }

    override fun inputAddress(street: String?, number: String?) {
        if (street==null && number==null) {
            userAddressEditText.setText("")
        } else {
            userAddressEditText.setText(("$street, $number"))
            bundle.putString("user_street", street)
            bundle.putString("user_street_nomer", number)
        }
    }

    private fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}