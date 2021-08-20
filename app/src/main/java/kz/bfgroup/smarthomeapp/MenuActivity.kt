package kz.bfgroup.smarthomeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import kz.bfgroup.smarthomeapp.ksk_list.presentation.KskListActivity
import kz.bfgroup.smarthomeapp.login.LoginActivity
import kz.bfgroup.smarthomeapp.my_home.MyHomeActivity
import kz.bfgroup.smarthomeapp.my_ksk.MyKskActivity
import kz.bfgroup.smarthomeapp.news.presentation.NewsActivity
import kz.bfgroup.smarthomeapp.registration.GENERATED_ACCESS_TOKEN
import kz.bfgroup.smarthomeapp.registration.MY_APP

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

            if (getSavedSerialNumber()=="default"){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MyKskActivity::class.java)
                startActivity(intent)
            }
        }

        val openMyHomeActivity = findViewById<LinearLayout>(R.id.open_my_home_activity)
        openMyHomeActivity.setOnClickListener {
            if (getSavedSerialNumber()=="default"){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MyHomeActivity::class.java)
                startActivity(intent)
            }
        }

        val openMyRequestActivity = findViewById<LinearLayout>(R.id.open_my_request_activity)
        openMyRequestActivity.setOnClickListener {
            if (getSavedSerialNumber()=="default"){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MyHomeActivity::class.java)
                startActivity(intent)
            }
        }

        val openKskListTextView = findViewById<LinearLayout>(R.id.open_ksk_list)
        openKskListTextView.setOnClickListener {
            val intent = Intent(this, KskListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getSavedSerialNumber(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_ACCESS_TOKEN, "default") ?: "default"
    }
}