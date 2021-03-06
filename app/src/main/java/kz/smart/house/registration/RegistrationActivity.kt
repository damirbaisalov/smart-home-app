package kz.smart.house.registration

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import kz.smart.house.R
import kz.smart.house.common.LoadingDialog
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.ksk_detailed.MY_APP_WITH_KSK_ID
import kz.smart.house.my_home.SELECTED_HOME_NUMBER
import kz.smart.house.my_home.SELECTED_HOME_STREET
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegistrationActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, NomerListDialogFragment.OnInputNewListener{

    private val bundle = Bundle()
    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    private lateinit var registerButton: Button
    private lateinit var validation: AwesomeValidation
    private lateinit var logOutImageButton: ImageButton

    private lateinit var userFIOEditText: EditText
    private lateinit var userIINEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var userBirthdayEditText: EditText
    private lateinit var userAddressEditText: EditText
    private lateinit var userApartmentEditText: EditText
    private lateinit var userEntranceEditText: EditText
    private lateinit var userTelephoneEditText: EditText
    private lateinit var userPasswordEditText: EditText

    private val loadingDialog: LoadingDialog = LoadingDialog(this)

    private var userNameSurnameFormatted: String = ""
    private var userDadNameFormatted: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initViews()

        formValidation()

        userAddressEditText.setOnClickListener {
            val dialog = StreetListDialogFragment()
            dialog.show(supportFragmentManager, "streetListDialogFragment")
//            val intent = Intent(this, StreetListActivity::class.java)
//            startActivity(intent)
        }

        pickDate()

        registerButton.setOnClickListener {

            if (validation.validate() &&
                formatTelephoneNumber(userTelephoneEditText.text.toString()).length==11
                && userAddressEditText.text.toString().isNotEmpty()) {

                loadingDialog.startLoadingDialog()

                val list = userFIOEditText.text.toString().split(" ")
                when(list.size) {
                    1 -> {
                        userNameSurnameFormatted = list[0]
                        userDadNameFormatted = ""
                    }
                    2 -> {
                        userNameSurnameFormatted = list[0] + " " + list[1]
                        userDadNameFormatted = ""
                    }
                    3 -> {
                        userNameSurnameFormatted = list[0] + " " + list[1]
                        userDadNameFormatted = list[2]
                    }
                    else -> {
                        userNameSurnameFormatted = list[0] + " " + list[1]
                        userDadNameFormatted = list[2]
                    }
                }

                bundle.putString("user_fullname", userNameSurnameFormatted)
                bundle.putString("user_dadname", userDadNameFormatted)
                bundle.putString("user_iin", userIINEditText.text.toString())
                bundle.putString("user_email", userEmailEditText.text.toString())
                bundle.putString("user_birthday", userBirthdayEditText.text.toString())
                bundle.putString("user_apartment", userApartmentEditText.text.toString())
                bundle.putString("user_entrance", userEntranceEditText.text.toString())
                bundle.putString("user_telephone", formatTelephoneNumber(userTelephoneEditText.text.toString()))
                bundle.putString("user_password", userPasswordEditText.text.toString())

                sendSmsCode()

            } else if (validation.validate() && userAddressEditText.text.toString().isEmpty()) {
                showToast("???????????????? ?????????? ???? ????????????")
            } else if (validation.validate() && formatTelephoneNumber(userTelephoneEditText.text.toString()).length<10) {
                showToast("?????????????? ??????????")
            } else {
                showToast("?????????????????? ?????? ????????")
            }
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
        userBirthdayEditText.isFocusable = false
        userAddressEditText.isFocusable = false
        logOutImageButton = findViewById(R.id.activity_menu_toolbar_logout)
        logOutImageButton.visibility = View.INVISIBLE

        if (getSavedHomeStreet()!="default" && getSavedHomeNumber()!="default") {
            userAddressEditText.setText("${getSavedHomeStreet()}, ${getSavedHomeNumber()}")
            bundle.putString("user_street", getSavedHomeStreet())
            bundle.putString("user_street_nomer", getSavedHomeNumber())
        }

        validation = AwesomeValidation(ValidationStyle.BASIC)
    }

    private fun pickDate() {
        userBirthdayEditText.setOnClickListener {
            val cal = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year = cal.get(Calendar.YEAR)

            DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,this,year,month,day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedMonth+=1
        var convertedMonth = savedMonth.toString()
        if (savedMonth.toString().length==1) {
            convertedMonth = "0$savedMonth"
        }
        savedYear = year
        userBirthdayEditText.setText("$savedYear.$convertedMonth.$savedDay")
    }

    private fun sendSmsCode() {
        ApiRetrofit.getApiClient().sendSMSCode(formatTelephoneNumber(userTelephoneEditText.text.toString())).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loadingDialog.dialogDismiss()
                if (response.isSuccessful) {
                    if (response.body()!!.string() == "Ok") {
                        showToast(response.body()!!.string())
                        val dialog = CodeDialogFragment()
                        dialog.arguments = bundle
                        dialog.show(supportFragmentManager, "codeDialog")
                    } else {
                        showToast(response.body()!!.string())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dialogDismiss()
                showToast(t.message)
            }
        })
    }

    private fun formValidation() {

        validation.addValidation(
            this,
            R.id.user_name_surname,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_iin,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_email,
            Patterns.EMAIL_ADDRESS,
            R.string.email_format_wrong
        )

        validation.addValidation(
            this,
            R.id.user_apartment,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_entrance,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_password,
            ".{6,}",
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_street,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )

        validation.addValidation(
            this,
            R.id.user_birthday,
            RegexTemplate.NOT_EMPTY,
            R.string.required_field
        )
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

    private fun formatTelephoneNumber(tel: String): String {
        var telephoneNumber = tel.replace("(","")
        telephoneNumber = telephoneNumber.replace(")", "")
        telephoneNumber = telephoneNumber.replace("-","")
        telephoneNumber = telephoneNumber.replace("+","")

        return telephoneNumber
    }

    private fun getSavedHomeStreet(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(SELECTED_HOME_STREET, "default") ?: "default"
    }

    private fun getSavedHomeNumber(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP_WITH_KSK_ID,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(SELECTED_HOME_NUMBER, "default") ?: "default"
    }

    private fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}