package com.snipex.shantu.assignment.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.snipex.shantu.assignment.ArticleApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object RetrofitRequest {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://itunes.apple.com/us/rss/"
    private val TAG = "ServiceGenerator"
    val HEADER_CACHE_CONTROL = "Cache-Control"
    val HEADER_PRAGMA = "Pragma"

    // cache size mention here
    private val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
    var gson = GsonBuilder().serializeNulls().create()
    // for cache response
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(okHttpClient())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build()
    }

    private fun cache(): Cache {
        return Cache(File(ArticleApplication.instance!!.cacheDir, "someIdentifier"), cacheSize)
    }

    /**
     * @Author Ashish Ingale
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "offline interceptor: called.")
            var request = chain.request()

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!ArticleApplication.hasNetwork()) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
            }

            chain.proceed(request)
        }
    }

    /**
     * @Author Ashish Ingale
     * This interceptor will be called ONLY if the network is available
     * @return
     */
    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "network interceptor: called.")

            val response = chain.proceed(chain.request())

            val cacheControl = CacheControl.Builder()
                    .maxAge(60, TimeUnit.SECONDS)
                    .build()

            response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d(TAG, "log: http log: $message") })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}
