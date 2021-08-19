package kz.bfgroup.smarthomeapp.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.data.ApiRetrofit
import kz.bfgroup.smarthomeapp.data.ApiRetrofit2
import kz.bfgroup.smarthomeapp.my_ksk.models.CandidatesApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.StreetAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StreetListDialogFragment: DialogFragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var streetAdapter = StreetAdapter()
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.street_list_dialog_fragment,container,false)

        bindViews()

        loadApiData()

        return rootView
    }

    private fun bindViews() {
        recyclerView = rootView.findViewById(R.id.street_list_dialog_fragment_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            rootView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = streetAdapter
        searchView = rootView.findViewById(R.id.street_list_dialog_fragment_search_view)
        progressBar = rootView.findViewById(R.id.street_list_dialog_fragment_progressbar)
    }

    private fun loadApiData() {
        ApiRetrofit2.getApiClient().getHomeList().enqueue(object:
            Callback<List<StreetApiData>> {
            override fun onResponse(
                call: Call<List<StreetApiData>>,
                response: Response<List<StreetApiData>>
            ) {
                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!
                    streetApiDataResponseList.addAll(list)
                    streetAdapter.setList(streetApiDataResponseList)
                }
            }

            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(rootView.context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}