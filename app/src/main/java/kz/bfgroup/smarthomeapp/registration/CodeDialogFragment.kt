package kz.bfgroup.smarthomeapp.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import kz.bfgroup.smarthomeapp.MenuActivity
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.common.LoadingDialog
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.registration.models.UserApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val MY_APP = "MY_APP"
const val GENERATED_ACCESS_TOKEN = "ACCESS_TOKEN"
const val GENERATED_USER_ID = "USER_ID"
const val GENERATED_HOME_ID = "HOME_ID"
const val GENERATED_KSK_ID = "KSK_ID"
class CodeDialogFragment: DialogFragment() {

    private lateinit var rootView: View

    private lateinit var closeButton: Button
    private lateinit var code1EditText: EditText
    private lateinit var code2EditText: EditText
    private lateinit var code3EditText: EditText
    private lateinit var code4EditText: EditText
    private lateinit var confirmRegCode: Button

    private lateinit var userEmail : String
    private lateinit var userFullName : String
    private lateinit var userDadName : String
    private lateinit var userIin : String
    private lateinit var userStreet: String
    private lateinit var userNomer : String
    private lateinit var userApartment : String
    private lateinit var userEntrance: String
    private lateinit var userDate : String
    private lateinit var userTelephone : String
    private lateinit var userPassword : String


    private var okRequest = false
    private lateinit var fields: Map<String,String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.code_dialog_fragment,container,false)

        userEmail = arguments?.getString("user_email").toString()
        userFullName = arguments?.getString("user_fullname").toString()
        userDadName = arguments?.getString("user_dadname").toString()
        userIin = arguments?.getString("user_iin").toString()
        userStreet = arguments?.getString("user_street").toString()
        userNomer = arguments?.getString("user_street_nomer").toString()
        userDate = arguments?.getString("user_birthday").toString()
        userApartment = arguments?.getString("user_apartment").toString()
        userEntrance = arguments?.getString("user_entrance").toString()
        userTelephone = arguments?.getString("user_telephone").toString()
        userPassword = arguments?.getString("user_password").toString()

        bindViews()

        closeButton.setOnClickListener {
            dismiss()
        }

        codeEditTextLogic()

        confirmRegCode.setOnClickListener {

            val code4signs = code1EditText.text.toString() +
                    code2EditText.text.toString() +
                    code3EditText.text.toString() +
                    code4EditText.text.toString()

            fields = mutableMapOf(
                "sign_up" to "1",
                "businessId" to "0",
                "email" to userEmail,
                "fullname" to userFullName,
                "dadname" to userDadName,
                "serialnumber" to userIin,
                "street" to userStreet,
                "nomer" to userNomer,
                "apartment" to userApartment,
                "entrance" to userEntrance,
                "date" to userDate,
                "tel" to userTelephone,
                "password" to userPassword,
                "code" to code4signs
            )
            sendUserData()
        }

        return rootView
    }

    private fun bindViews() {
        closeButton = rootView.findViewById(R.id.code_dialog_fragment_back_button)
        code1EditText = rootView.findViewById(R.id.code_number_1)
        code2EditText = rootView.findViewById(R.id.code_number_2)
        code3EditText = rootView.findViewById(R.id.code_number_3)
        code4EditText = rootView.findViewById(R.id.code_number_4)
        confirmRegCode = rootView.findViewById(R.id.code_dialog_fragment_confirm_reg)
    }

    private fun sendUserData() {
        ApiRetrofit.getApiClient().registerUser(fields).enqueue(object: Callback<UserApiData> {
            override fun onResponse(call: Call<UserApiData>, response: Response<UserApiData>) {

                if (response.isSuccessful){
                    if (response.body()!!.mess_status == "0") {
                        showToast(response.body()!!.mess)
                    } else {
                        saveUserDataNeeded(
                            response.body()!!.serial_number!!,
                            response.body()!!.id!!,
                            response.body()!!.home_id!!,
                            response.body()!!.ksk_id!!
                        )

                        val intent = Intent(rootView.context, MenuActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }

            override fun onFailure(call: Call<UserApiData>, t: Throwable) {
                showToast(t.message)
                okRequest = false
            }

        })
    }


    private fun saveUserDataNeeded(token: String, userId: String, homeId: String, kskId: String) {
        val sharedPref = rootView.context.getSharedPreferences(MY_APP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(GENERATED_ACCESS_TOKEN, token)
        editor.putString(GENERATED_USER_ID, userId)
        editor.putString(GENERATED_HOME_ID, homeId)
        editor.putString(GENERATED_KSK_ID, kskId)
        editor.apply()
    }

    private fun codeEditTextLogic() {
        code1EditText.doOnTextChanged { text, start, before, count ->
            if (count==1) {
                code2EditText.requestFocus()
            }
        }

        code2EditText.doOnTextChanged { text, start, before, count ->
            if (count==1) {
                code3EditText.requestFocus()
            } else {
                code1EditText.requestFocus()
            }
        }

        code3EditText.doOnTextChanged { text, start, before, count ->
            if (count == 1) {
                code4EditText.requestFocus()
            } else {
                code2EditText.requestFocus()
            }
        }

        code4EditText.doOnTextChanged { text, start, before, count ->
            if (count == 0) {
                code3EditText.requestFocus()
            }
        }
    }

    private fun showToast(text: String?) {
        Toast.makeText(rootView.context, text, Toast.LENGTH_SHORT).show()
    }
}