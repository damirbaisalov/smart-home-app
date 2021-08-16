package kz.bfgroup.smarthomeapp.data

import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import retrofit2.Call
import retrofit2.http.GET

interface ApiClient {

    @GET("ListKSK.php")
    fun getKskList(): Call<List<KskApiData>>
}