package kz.bfgroup.smarthomeapp.data

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiRetrofit {

    const val BASE_URL = "https://15000pvl.kz/smart_dom/api/"

    val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

    fun getApiClient(): ApiClient {
        val apiRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return apiRetrofit.create(ApiClient::class.java)
    }

}