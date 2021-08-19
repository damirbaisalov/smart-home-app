package kz.bfgroup.smarthomeapp.registration

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kz.bfgroup.smarthomeapp.R
import java.util.*

class RegistrationActivity : AppCompatActivity() {

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
            val dialog = CodeDialogFragment()
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

}