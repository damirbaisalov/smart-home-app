package kz.smart.house.home_list.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.data.ApiRetrofit
import kz.smart.house.home_list.presentation.view.HomeNumberClickListener
import kz.smart.house.home_list.presentation.view.HomeNumberListAdapter
import kz.smart.house.my_home.MyHomeActivity
import kz.smart.house.registration.models.StreetApiData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeNumberListDialogFragment: DialogFragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var homeNumberListAdapter = HomeNumberListAdapter(getHomeNumberClickListener())
    private lateinit var progressBar: ProgressBar
    private lateinit var selectedStreet: String
    private lateinit var streetTitleTextView: TextView
    private lateinit var homeNumberLayout : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        rootView = inflater.inflate(R.layout.home_number_list_dialog_fragment,container,false)
        selectedStreet = arguments?.getString("selected_street").toString()

        bindViews()

        loadApiData()

        return rootView
    }

    private fun bindViews() {
        homeNumberLayout = rootView.findViewById(R.id.home_number_list_dialog_fragment_layout)
        homeNumberLayout.visibility = View.INVISIBLE

        recyclerView = rootView.findViewById(R.id.home_number_dialog_fragment_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            rootView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = homeNumberListAdapter
        progressBar = rootView.findViewById(R.id.home_number_list_dialog_fragment_progressbar)
        progressBar.visibility = View.VISIBLE
        streetTitleTextView = rootView.findViewById(R.id.home_number_list_dialog_fragment_home_name)
        streetTitleTextView.text = ("ул. $selectedStreet")
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getHomeListByStreet(selectedStreet).enqueue(object:
            Callback<List<StreetApiData>> {
            override fun onResponse(
                call: Call<List<StreetApiData>>,
                response: Response<List<StreetApiData>>
            ) {
                progressBar.visibility = View.GONE
                homeNumberLayout.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!
                    streetApiDataResponseList.addAll(list)
                    homeNumberListAdapter.setList(streetApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                homeNumberLayout.visibility = View.VISIBLE
                Toast.makeText(rootView.context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun getHomeNumberClickListener(): HomeNumberClickListener {
        return object: HomeNumberClickListener {
            override fun onClick(id: String?, nomer: String?) {
                val intentMyHome = Intent(rootView.context, MyHomeActivity::class.java)
                intentMyHome.putExtra("selected_street_with_number", id)
//                saveHomeIdFromList(id!!)
                startActivity(intentMyHome)
            }
        }
    }

//    private fun saveHomeIdFromList(homeId: String) {
//        val sharedPref = rootView.context.getSharedPreferences(MY_APP_WITH_KSK_ID, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = sharedPref.edit()
//
//        editor.putString(SELECTED_HOME_ID, homeId)
//        editor.apply()
//    }
}