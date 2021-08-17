package kz.bfgroup.smarthomeapp.data

import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.my_ksk.models.CandidatesApiData
import kz.bfgroup.smarthomeapp.my_ksk.models.MyKskApiData
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.net.ResponseCache

interface ApiClient {

    @GET("ListKSK.php")
    fun getKskList(): Call<List<KskApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getNewsList(@Field("pvl_news_startFrom") startIndex: String) : Call<List<NewsApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyKsk(@Field("my_ksk_id") kskId: String) : Call<MyKskApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getKskHomeNum(@Field("count_ksk_homes_id") kskId: String) : Call<String>

    @FormUrlEncoded
    @POST("post.php")
    fun getAllCandidates(@Field("get_all_candidates_home_id") homeId: String) : Call<List<CandidatesApiData>>
}