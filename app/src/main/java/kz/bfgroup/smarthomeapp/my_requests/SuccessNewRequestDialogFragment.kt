package kz.bfgroup.smarthomeapp.my_requests

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import kz.bfgroup.smarthomeapp.R

class SuccessNewRequestDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Ваша заявка успешно отправлена!")
                .setNeutralButton("ОК") {
                        dialog, id ->  dialog.cancel()
                    activity?.finish()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}