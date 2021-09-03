package kz.bfgroup.smarthomeapp.registration

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import kz.bfgroup.smarthomeapp.registration.view.NomerAdapter
import kz.bfgroup.smarthomeapp.registration.view.NomerClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ClassCastException

class NomerListDialogFragment: DialogFragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private var nomerAdapter = NomerAdapter(getNomerClickListener())
    private lateinit var searchView: SearchView
    private var searchingStreetList: List<StreetApiData> = listOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var selectedStreet: String

    private lateinit var nomerListLayout: LinearLayout

    interface OnInputNewListener {
        fun inputAddress(street : String?, number: String?)
    }

    private lateinit var onInputNewListener: OnInputNewListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        rootView = inflater.inflate(R.layout.nomer_list_dialog_fragment,container,false)
        selectedStreet = arguments?.getString("selected_street").toString()

        bindViews()

        loadApiData()

        queryInSearchView()

        return rootView
    }

    private fun bindViews() {
        nomerListLayout = rootView.findViewById(R.id.nomer_list_dialog_fragment_layout)
        nomerListLayout.visibility = View.INVISIBLE

        recyclerView = rootView.findViewById(R.id.nomer_list_dialog_fragment_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            rootView.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = nomerAdapter
        searchView = rootView.findViewById(R.id.nomer_list_dialog_fragment_search_view)
        progressBar = rootView.findViewById(R.id.nomer_list_dialog_fragment_progressbar)
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
                nomerListLayout.visibility = View.VISIBLE
                if (response.isSuccessful) {
                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
                    val list = response.body()!!
                    streetApiDataResponseList.addAll(list)
                    searchingStreetList = list
                    nomerAdapter.setList(streetApiDataResponseList)
                    Toast.makeText(rootView.context, "Выберите номер дома", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
                progressBar.visibility = View.GONE
                nomerListLayout.visibility = View.VISIBLE
                Toast.makeText(rootView.context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun queryInSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                val queryText = p0?.lowercase()

                nomerAdapter.filter(queryText!!)

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                val queryText = p0?.lowercase()

                val newStreetList : MutableList<StreetApiData> = mutableListOf()
                for (q in searchingStreetList) {
                    val nomerName= q.nomer?.lowercase()
                    if (nomerName?.contains(queryText!!)!!) {
                        newStreetList.add(q)
                    }
                }
                nomerAdapter.setList(newStreetList)

                return false
            }
        })
    }

    private fun getNomerClickListener(): NomerClickListener {
        return object: NomerClickListener {
            override fun onClick(nomer: String?) {
                onInputNewListener.inputAddress(selectedStreet, nomer)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onInputNewListener = activity as OnInputNewListener
        } catch (e: ClassCastException){
            Log.d("TAG", "onAttach: ClassException: " + e.message)
        }
    }
}