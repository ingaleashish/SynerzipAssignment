package com.snipex.shantu.assignment.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snipex.shantu.assignment.response.ArticleResponse
import com.snipex.shantu.assignment.retrofit.ApiRequest
import com.snipex.shantu.assignment.retrofit.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ArticleRepository {
    private val apiRequest: ApiRequest

    init {
        apiRequest = RetrofitRequest.retrofitInstance!!.create(ApiRequest::class.java!!)
    }

    fun getArticlesList(): LiveData<ArticleResponse> {

        val data = MutableLiveData<ArticleResponse>()
        apiRequest.movieArticles
                .enqueue(object : Callback<ArticleResponse> {


                    override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                        Log.d(TAG, "onResponse response:: $response")
                        Log.d(TAG, "onResponse: " + response.body()!!)
                        if (response.raw().networkResponse() != null && response.raw().networkResponse()!!.code() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                            Log.d(TAG, "onResponse: response is from NETWORK...")
                            Log.d(TAG, "onResponse: response hasn't changed")


                            if (response.body() != null) {
                                data.setValue(response.body())
                            }
                        } else if (response.raw().networkResponse() != null && response.raw().networkResponse()!!.code() != HttpURLConnection.HTTP_NOT_MODIFIED) {
                            Log.d(TAG, "onResponse: response is from NETWORK...")
                            Log.d(TAG, "onResponse: response changed")

                            if (response.body() != null) {
                                data.setValue(response.body())
                            }
                        } else if (response.raw().cacheResponse() != null && response.raw().networkResponse() == null) {
                            Log.d(TAG, "onResponse: response is from CACHE...")

                            if (response.body() != null) {
                                data.setValue(response.body())
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                        data.setValue(null)
                    }
                })
        return data
    }

}
