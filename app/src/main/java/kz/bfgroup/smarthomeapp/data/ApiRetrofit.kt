package kz.bfgroup.smarthomeapp.data

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiRetrofit {

    const val BASE_URL2 = "http://f0497377.xsph.ru/ksk_tenants/api/"

    val okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

    fun getApiClient(): ApiClient {
        val apiRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL2)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return apiRetrofit.create(ApiClient::class.java)
    }

}