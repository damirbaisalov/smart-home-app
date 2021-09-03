package kz.bfgroup.smarthomeapp.registration

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
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.StreetAdapter
import kz.bfgroup.smarthomeapp.registration.view.StreetClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StreetListDialogFragment: DialogFragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var streetAdapter = StreetAdapter(getStreetClickListener())
    private lateinit var searchView: SearchView
    private var searchingStreetList: List<StreetApiData> = listOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var streetListLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        rootView = inflater.inflate(R.layout.street_list_dialog_fragment,container,false)

        bindViews()

        loadApiData()

        queryInSearchView()

        return rootView
    }

    private fun bindViews() {
        streetListLayout = rootView.findViewById(R.id.street_list_dialog_fragment_layout)
        streetListLayout.visibility = View.GONE

        recyclerView = rootView.findViewById(R.id.street_list_dialog_fragment_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            rootView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = streetAdapter
        searchView = rootView.findViewById(R.id.street_list_dialog_fragment_search_view)
        progressBar = rootView.findViewById(R.id.street_list_dialog_fragment_progressbar)
        progressBar.visibility = View.VISIBLE
    }

    private fun loadApiData() {
        ApiRetrofit.getApiClient().getUniqueStreetList("1").enqueue(object:
            Callback<List<StreetApiData>> {
            override fun onResponse(
                call: Call<List<StreetApiData>>,
                response: Response<List<StreetApiData>>
            ) {
                progressBar.visibility = View.GONE
                streetListLayout.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!
                    streetApiDataResponseList.addAll(list)
                    searchingStreetList = list
                    streetAdapter.setList(streetApiDataResponseList)
                    Toast.makeText(rootView.context, "Выберите адрес", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                streetListLayout.visibility = View.VISIBLE
                Toast.makeText(rootView.context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                val queryText = p0?.lowercase()

                streetAdapter.filter(queryText!!)

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

                val newStreetList : MutableList<StreetApiData> = mutableListOf()
                for (q in searchingStreetList) {
                    val streetWithNomer = q.street?.lowercase()
                    if (streetWithNomer?.contains(queryText!!)!!) {
                        newStreetList.add(q)
                    }
                }
                streetAdapter.setList(newStreetList)

                return false
            }
        })
    }

    private fun getStreetClickListener(): StreetClickListener {
        return object: StreetClickListener {
            override fun onClick(street: String?) {
                val bundle = Bundle()
                bundle.putString("selected_street", street)
                val nomerListDialogFragment = NomerListDialogFragment()
                nomerListDialogFragment.arguments = bundle
                nomerListDialogFragment.show(parentFragmentManager, "nomerListDialogFragment")
                dismiss()
            }
        }
    }
}