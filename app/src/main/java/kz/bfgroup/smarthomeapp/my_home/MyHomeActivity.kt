package kz.bfgroup.smarthomeapp.my_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.my_home.models.HomeApiData
import kz.bfgroup.smarthomeapp.my_home.models.HomePassportApiData
import kz.bfgroup.smarthomeapp.my_ksk.models.MyKskApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyHomeActivity : AppCompatActivity() {

    private lateinit var myHomeTitle: TextView
    private lateinit var myHomeStatus: TextView
    private lateinit var backButton : ImageButton

    private lateinit var myHomeYearConst: TextView
    private lateinit var myHomeTotalArea: TextView
    private lateinit var myHomeFloorNum: TextView
    private lateinit var myHomeFlatNum: TextView
    private lateinit var myHomeLiftNum: TextView
    private lateinit var myHomeVideoSpec: TextView
    private lateinit var myHomeKrovlya: TextView
    private lateinit var myHomeFasad: TextView
    private lateinit var myHomeBalkon: TextView
    private lateinit var myHomeDolg: TextView

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_home)

        initViews()

        mapView.map.move(
            CameraPosition(Point(52.27401,77.00438),11.0f,0.0f,0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        backButton.setOnClickListener {
            onBackPressed()
        }

        loadApiData()
        loadApiData2()

    }

    private fun initViews() {
        myHomeTitle = findViewById(R.id.activity_my_home_adress)
        myHomeStatus = findViewById(R.id.activity_my_home_status)
        backButton = findViewById(R.id.activity_my_home_back_button)

        myHomeYearConst = findViewById(R.id.activity_my_home_year_construction)
        myHomeTotalArea = findViewById(R.id.activity_my_home_area)
        myHomeFloorNum = findViewById(R.id.activity_my_home_floor_num)
        myHomeFlatNum = findViewById(R.id.activity_my_home_flat_num)
        myHomeLiftNum = findViewById(R.id.activity_my_home_lifts)
        myHomeVideoSpec = findViewById(R.id.activity_my_home_video_spectating)
        myHomeKrovlya = findViewById(R.id.activity_my_home_krovlya)
        myHomeFasad = findViewById(R.id.activity_my_home_fasad)
        myHomeBalkon = findViewById(R.id.activity_my_home_balkon)
        myHomeDolg = findViewById(R.id.activity_my_home_dolg)

        mapView = findViewById(R.id.map_view)
        mapView.map.isScrollGesturesEnabled = false
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getMyHomeAddress("1020").enqueue(object: Callback<HomeApiData> {
            override fun onResponse(
                call: Call<HomeApiData>,
                response: Response<HomeApiData>
            ) {
//                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val responseBody = response.body()!!

                    myHomeTitle.text = ("Адрес: " + responseBody.street + ", " + responseBody.nomer)
                    myHomeStatus.text = ("Статус: " + responseBody.status)
                }
            }

            override fun onFailure(call: Call<HomeApiData>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@MyHomeActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loadApiData2() {
        ApiRetrofit.getApiClient().getMyHomePassport("1020").enqueue(object: Callback<HomePassportApiData> {
            override fun onResponse(
                call: Call<HomePassportApiData>,
                response: Response<HomePassportApiData>
            ) {
//                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val responseBody = response.body()!!
                    myHomeYearConst.text = (responseBody.year_construction + " год")
                    myHomeTotalArea.text = (responseBody.total_area + " м2")
                    myHomeFloorNum.text = (responseBody.number_floor + " этажей")
                    myHomeFlatNum.text = responseBody.number_apartments //need to change
                    myHomeLiftNum.text = responseBody.number_apartments //need to change
                    myHomeVideoSpec.text = (responseBody.number_floor + " шт.") //need to change
                    myHomeKrovlya.text = (responseBody.living_area + " м2") //need to change
                    myHomeFasad.text = (responseBody.area_premises + " м2") //need to change
                    myHomeBalkon.text = (responseBody.balcony_area + " м2")
                    myHomeDolg.text = "0 тенге" //need to change

                }
            }

            override fun onFailure(call: Call<HomePassportApiData>, t: Throwable) {
//                progressBar.visibility = View.GONE
                Toast.makeText(this@MyHomeActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }
}