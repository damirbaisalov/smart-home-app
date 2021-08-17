package kz.bfgroup.smarthomeapp.data

import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {

    @GET("ListKSK.php")
    fun getKskList(): Call<List<KskApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getNewsList(@Field("pvl_news_startFrom") startIndex: String) : Call<List<NewsApiData>>
}