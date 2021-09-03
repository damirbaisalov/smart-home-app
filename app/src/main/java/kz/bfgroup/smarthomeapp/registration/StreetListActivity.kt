package kz.bfgroup.smarthomeapp.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.mukesh.tinydb.TinyDB
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.StreetAdapter
import kz.bfgroup.smarthomeapp.registration.view.StreetClickListener

class StreetListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var streetAdapter = StreetAdapter(getStreetClickListener())
    private lateinit var searchView: SearchView
    private var searchingStreetList: List<StreetApiData> = listOf()
    private lateinit var progressBar: ProgressBar
//    private lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_list)

        bindViews()
//        loadApiData()
//        queryInSearchView()

    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.street_list_dialog_fragment_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.adapter = streetAdapter
        searchView = findViewById(R.id.street_list_dialog_fragment_search_view)
        progressBar = findViewById(R.id.street_list_dialog_fragment_progressbar)
//        tinyDB = TinyDB(applicationContext)
    }

//    private fun loadApiData() {
//        ApiRetrofit2.getApiClient().getHomeList().enqueue(object:
//            Callback<List<StreetApiData>> {
//            override fun onResponse(
//                call: Call<List<StreetApiData>>,
//                response: Response<List<StreetApiData>>
//            ) {
//                progressBar.visibility = View.GONE
////                recyclerView.visibility = View.VISIBLE
//                if (response.isSuccessful) {
//                    val streetApiDataResponseList: MutableList<StreetApiData> = mutableListOf()
//                    val list = response.body()!!
//                    streetApiDataResponseList.addAll(list)
//                    searchingStreetList = list
//                    streetAdapter.setList(streetApiDataResponseList)
//                }
//            }
//
//            override fun onFailure(call: Call<List<StreetApiData>>, t: Throwable) {
//                progressBar.visibility = View.GONE
//                Toast.makeText(this@StreetListActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//
//        })
//    }

//    private fun queryInSearchView() {
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                searchView.clearFocus()
//                val queryText = p0?.lowercase()
//
//                streetAdapter.filter(queryText!!)
//
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//
//                val queryText = p0?.lowercase()
//
//                val newStreetList : MutableList<StreetApiData> = mutableListOf()
//                for (q in searchingStreetList) {
//                    val streetWithNomer = q.street?.lowercase() + ", " + q.nomer?.lowercase()
//                    if (streetWithNomer?.contains(queryText!!)!!) {
//                        newStreetList.add(q)
//                    }
//                }
//                streetAdapter.setList(newStreetList)
//
//                return false
//            }
//        })
//    }

    private fun getStreetClickListener(): StreetClickListener {
        return object: StreetClickListener {
            override fun onClick(street: String?) {
//                onInputNewListener.inputAddress(street, nomer)
//                dismiss()
//                tinyDB.putString("street_tiny_db", street)
//                tinyDB.putString("nomer_tiny_db", nomer)

                finish()
            }
        }
    }
}