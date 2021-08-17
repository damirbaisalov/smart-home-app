package kz.bfgroup.smarthomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import kz.bfgroup.smarthomeapp.ksk_list.presentation.KskListActivity
import kz.bfgroup.smarthomeapp.my_ksk.MyKskActivity
import kz.bfgroup.smarthomeapp.news.presentation.NewsActivity
import org.w3c.dom.Text

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val openNewsTextView = findViewById<LinearLayout>(R.id.open_news_activity)
        openNewsTextView.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        val openMyKskActivity = findViewById<LinearLayout>(R.id.open_my_ksk_activity)
        openMyKskActivity.setOnClickListener {
            val intent = Intent(this, MyKskActivity::class.java)
            startActivity(intent)
        }

        val openKskListTextView = findViewById<LinearLayout>(R.id.open_ksk_list)
        openKskListTextView.setOnClickListener {
            val intent = Intent(this, KskListActivity::class.java)
            startActivity(intent)
        }
    }
}