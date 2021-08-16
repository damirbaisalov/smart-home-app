package kz.bfgroup.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import kz.bfgroup.smarthomeapp.ksk_list.presentation.KskListActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val openKskListTextView = findViewById<TextView>(R.id.open_ksk_list)
        openKskListTextView.setOnClickListener {
            val intent = Intent(this,KskListActivity::class.java)
            startActivity(intent)
        }
    }
}