package com.snipex.shantu.assignment;

import com.snipex.shantu.assignment.response.ArticleResponse;
import com.snipex.shantu.assignment.retrofit.ApiRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertTrue;

/**
 * Created by Ashish Ingale on 23-01-2020.
 */
@RunWith(JUnit4.class)
public class ArticleTest {
    private String BASE_URL = "https://dl.dropboxusercontent.com/s/";

    @Test
    public void testArticle() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url(BASE_URL).toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mockWebServer.enqueue(new MockResponse().setBody("your json body"));

        ApiRequest service = retrofit.create(ApiRequest.class);

        Call<ArticleResponse> call = service.getMovieArticles();

        assertTrue(call.execute() != null);

        mockWebServer.shutdown();
    }
}
