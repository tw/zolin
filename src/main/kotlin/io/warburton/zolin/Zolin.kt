package io.warburton.zolin

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.warburton.zolin.model.AccountResponse
import io.warburton.zolin.model.Balance
import io.warburton.zolin.model.TransactionResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author tw
 */
interface Zolin {

    @GET("/accounts")
    fun accounts(): Call<AccountResponse>

    @GET("/balance")
    fun balance(@Query("account_id") accountId: String): Call<Balance>

    @GET("/transactions?expand[]=merchant")
    fun transactions(@Query("account_id") accountId: String): Call<TransactionResponse>

}

object ZolinFactory {
    val mapper: ObjectMapper = ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .registerModule(KotlinModule())
            .registerModule(JodaModule())

    fun create(url: String, accessToken: String): Zolin {
        val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .header("Authorization", "Bearer $accessToken")
                            .build()
                    chain.proceed(request)
                }
                .build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()
        return retrofit.create(Zolin::class.java)
    }

}