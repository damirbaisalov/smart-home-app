package kz.bfgroup.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import kz.bfgroup.smarthomeapp.ksk_list.presentation.KskListActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val openKskListLayout = findViewById<LinearLayout>(R.id.open_ksk_list)
        openKskListLayout.setOnClickListener {
            val intent = Intent(this,KskListActivity::class.java)
            startActivity(intent)
        }
    }
}