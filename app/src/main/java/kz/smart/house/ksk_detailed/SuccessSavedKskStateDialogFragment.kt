package kz.smart.house.ksk_detailed

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SuccessSavedKskStateDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Ваш КСК успешно сохранен!")
                .setNeutralButton("ОК") {
                        dialog, id ->  dialog.cancel()
                    activity?.finish()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}