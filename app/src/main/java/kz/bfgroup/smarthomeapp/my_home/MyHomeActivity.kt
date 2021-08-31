package kz.bfgroup.smarthomeapp.my_home

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import com.yandex.runtime.ui_view.ViewProvider
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.my_home.models.HomeApiData
import kz.bfgroup.smarthomeapp.my_home.models.HomePassportApiData
import kz.bfgroup.smarthomeapp.registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyHomeActivity : AppCompatActivity(), Session.SearchListener, CameraListener {

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
    private lateinit var searchSession: Session
    private lateinit var searchManager: SearchManager
    private lateinit var homeAddressText : String

    private lateinit var progressBar: ProgressBar
    private lateinit var myHomeLayout: LinearLayout

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
        if (loadApiData() && loadApiData2()) {
            progressBar.visibility = View.GONE
            myHomeLayout.visibility = View.VISIBLE
        }

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

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView = findViewById(R.id.map_view)
        mapView.map.addCameraListener(this)
        mapView.map.isScrollGesturesEnabled = false
        mapView.map.isZoomGesturesEnabled = false
        progressBar = findViewById(R.id.activity_my_home_progress_bar)
        myHomeLayout = findViewById(R.id.activity_my_home_layout)
        myHomeLayout.visibility = View.INVISIBLE
    }

    private fun loadApiData(): Boolean {
        ApiRetrofit.getApiClient().getMyHomeAddress(getSavedHomeId()).enqueue(object: Callback<HomeApiData> {
            override fun onResponse(
                call: Call<HomeApiData>,
                response: Response<HomeApiData>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()!!
                    val addressGeoCode = responseBody.street + ", " + responseBody.nomer

                    homeAddressText = addressGeoCode

                    submitQuery(homeAddressText)

                    myHomeTitle.text = ("Адрес: " + responseBody.street + ", " + responseBody.nomer)
                    myHomeStatus.text = ("Статус: " + responseBody.status)
                }
            }

            override fun onFailure(call: Call<HomeApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MyHomeActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })

        return true
    }

    private fun loadApiData2(): Boolean {
        ApiRetrofit.getApiClient().getMyHomePassport(getSavedHomeId()).enqueue(object: Callback<HomePassportApiData> {
            override fun onResponse(
                call: Call<HomePassportApiData>,
                response: Response<HomePassportApiData>
            ) {
//                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    if (response.body() == null) {
                        Toast.makeText(this@MyHomeActivity, "null", Toast.LENGTH_SHORT).show()
                    } else {
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
            }

            override fun onFailure(call: Call<HomePassportApiData>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MyHomeActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })

        return true
    }

    private fun submitQuery(query: String) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            this
        )
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

    private fun drawHomeMarker(p: Point) : ViewProvider {
        val view = View(applicationContext).apply {
            background = applicationContext.getDrawable(R.drawable.ic_home_location)
        }

        return ViewProvider(view)
    }

    override fun onSearchResponse(p0: com.yandex.mapkit.search.Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()

        for (searchResult in p0.collection.children) {
            val resultLocation = searchResult.obj!!.geometry[0].point
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                    resultLocation,
                    drawHomeMarker(resultLocation)
                )

                mapView.map.move(
                    CameraPosition(resultLocation,16f,0.0f,0.0f),
                    Animation(Animation.Type.SMOOTH, 1F),
                    null
                )
            }
        }
    }

    override fun onSearchError(p0: Error) {
        var errorMessage = "Unknown error"
        if (p0 is RemoteError){
            errorMessage = "Remote error"
        } else if (p0 is NetworkError) {
            errorMessage = "Network error"
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onCameraPositionChanged(
        p0: Map,
        p1: CameraPosition,
        p2: CameraUpdateReason,
        p3: Boolean
    ) {
//       if (p3) {
//           submitQuery("Камзина, 167")
//       }
    }

    private fun getSavedHomeId(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            MY_APP,
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(GENERATED_HOME_ID, "default") ?: "default"
    }
}