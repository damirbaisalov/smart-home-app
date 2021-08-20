package kz.bfgroup.smarthomeapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kz.bfgroup.smarthomeapp.registration.GENERATED_ACCESS_TOKEN
import kz.bfgroup.smarthomeapp.registration.MY_APP
import kz.bfgroup.smarthomeapp.registration.RegistrationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getSavedSerialNumber() != "default") {
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val startButton : Button = findViewById(R.id.start_button)
            startButton.setOnClickListener {
                val intent = Intent(this,RegistrationActivity::class.java)
                startActivity(intent)
                finish()
            }
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