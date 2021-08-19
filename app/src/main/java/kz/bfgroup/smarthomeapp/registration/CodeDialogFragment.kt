package kz.bfgroup.smarthomeapp.registration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import kz.bfgroup.smarthomeapp.R

const val MY_APP = "MY_APP"
const val GENERATED_ACCESS_TOKEN = "ACCESS_TOKEN"
class CodeDialogFragment: DialogFragment() {

    private lateinit var rootView: View

    private lateinit var closeButton: Button
    private lateinit var code1EditText: EditText
    private lateinit var code2EditText: EditText
    private lateinit var code3EditText: EditText
    private lateinit var code4EditText: EditText

    private lateinit var fields: Map<String,String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.code_dialog_fragment,container,false)

        bindViews()

        closeButton.setOnClickListener {
            dismiss()
        }

        codeEditTextLogic()

        return rootView
    }

    private fun bindViews() {
        closeButton = rootView.findViewById(R.id.code_dialog_fragment_back_button)
        code1EditText = rootView.findViewById(R.id.code_number_1)
        code2EditText = rootView.findViewById(R.id.code_number_2)
        code3EditText = rootView.findViewById(R.id.code_number_3)
        code4EditText = rootView.findViewById(R.id.code_number_4)
    }


    private fun saveAccessToken(token: String) {
        val sharedPref = rootView.context.getSharedPreferences(MY_APP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(GENERATED_ACCESS_TOKEN, token)
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
}