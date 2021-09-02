package kz.bfgroup.smarthomeapp.home_list.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.home_list.presentation.view.HomeNumberClickListener
import kz.bfgroup.smarthomeapp.home_list.presentation.view.HomeNumberListAdapter
import kz.bfgroup.smarthomeapp.ksk_detailed.MY_APP_WITH_KSK_ID
import kz.bfgroup.smarthomeapp.ksk_detailed.SELECTED_KSK_ID
import kz.bfgroup.smarthomeapp.my_home.MyHomeActivity
import kz.bfgroup.smarthomeapp.registration.GENERATED_HOME_ID
import kz.bfgroup.smarthomeapp.registration.MY_APP
import kz.bfgroup.smarthomeapp.registration.models.NomerNameApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.NomerAdapter
import kz.bfgroup.smarthomeapp.registration.view.NomerClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeNumberListDialogFragment: DialogFragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var homeNumberListAdapter = HomeNumberListAdapter(getHomeNumberClickListener())
    private lateinit var progressBar: ProgressBar
    private lateinit var selectedStreet: String
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
//                saveHomeId(id!!)
                startActivity(intentMyHome)
            }
        }
    }

    private fun saveHomeId(homeId: String) {
        val sharedPref = rootView.context.getSharedPreferences(MY_APP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(GENERATED_HOME_ID, homeId)
        editor.apply()
    }
}