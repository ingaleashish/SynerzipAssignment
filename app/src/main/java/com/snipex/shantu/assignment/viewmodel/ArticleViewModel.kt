package com.snipex.shantu.assignment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.snipex.shantu.assignment.repository.ArticleRepository
import com.snipex.shantu.assignment.response.ArticleResponse

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val articleRepository: ArticleRepository
    val articleResponseLiveData: LiveData<ArticleResponse>

    init {

        articleRepository = ArticleRepository()
        this.articleResponseLiveData = articleRepository.getArticlesList()
    }
}
