package kz.smart.house.data

import kz.smart.house.ksk_detailed.models.KskDetailedApiData
import kz.smart.house.ksk_list.models.KskApiData
import kz.smart.house.my_home.models.HomeApiData
import kz.smart.house.my_home.models.HomePassportApiData
import kz.smart.house.my_ksk.models.CandidatesApiData
import kz.smart.house.my_ksk.models.MyKskApiData
import kz.smart.house.my_requests.models.MyRequestApiData
import kz.smart.house.news.models.NewsApiData
import kz.smart.house.registration.models.StreetApiData
import kz.smart.house.registration.models.UserApiData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

//    @GET("ListKSK.php")
//    fun getKskList(): Call<List<KskApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getKskList(@Field("ksk_list") kskList: String): Call<List<KskApiData>>

    @GET("news.php")
    fun getNews() : Call<List<NewsApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun userLogin(@FieldMap fields: Map<String, String>) : Call<UserApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getNewsList(@Field("pvl_news_startFrom") startIndex: String) : Call<List<NewsApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getNewsById(@Field("one_news_id") newsId: String) : Call<NewsApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyKsk(@Field("my_ksk_id") kskId: String) : Call<MyKskApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getKskHomeNum(@Field("count_ksk_homes_id") kskId: String) : Call<String>

    @FormUrlEncoded
    @POST("post.php")
    fun getAllCandidates(@Field("get_all_candidates_home_id") homeId: String) : Call<List<CandidatesApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyHomeAddress(@Field("home_adress_id") homeId: String) : Call<HomeApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyHomeRepairHistory(@Field("repair_home_id") homeId: String) : Call<String>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyHomePassport(@Field("teh_passport_home_id") homeId: String) : Call<HomePassportApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getMyRequests(@FieldMap fields: Map<String, String>): Call<List<MyRequestApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun sendNewRequest(@FieldMap fields: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST("sms_code.php")
    fun sendSMSCode(@Field("verify_number_tenants") phone: String) : Call<ResponseBody>

    @FormUrlEncoded
    @POST("post.php")
    fun registerUser(@FieldMap fields: Map<String, String>) : Call<UserApiData>

//    @GET("orderByName.php")
//    fun getHomeList(): Call<List<StreetApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getKskById(@Field("ksk_one_info_id") kskId: String) : Call<KskDetailedApiData>

    @FormUrlEncoded
    @POST("post.php")
    fun getUniqueStreetList(@Field("all_unicue_streets") street: String) : Call<List<StreetApiData>>

    @FormUrlEncoded
    @POST("post.php")
    fun getHomeListByStreet(@Field("get_homes_where_street") street: String) : Call<List<StreetApiData>>
}