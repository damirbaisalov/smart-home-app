package kz.bfgroup.smarthomeapp.common

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import kz.bfgroup.smarthomeapp.R

class LoadingDialog(
    private val activity: Activity
) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dialogDismiss() {
        dialog.dismiss()
    }

}