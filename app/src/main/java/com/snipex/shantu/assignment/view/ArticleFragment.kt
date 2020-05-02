package com.snipex.shantu.assignment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.snipex.shantu.assignment.R
import com.snipex.shantu.assignment.adapter.ArticleAdapter
import com.snipex.shantu.assignment.response.Entry
import com.snipex.shantu.assignment.viewmodel.ArticleViewModel
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration




class ArticleFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var recyclerView: RecyclerView? = null
    // private var progressCircularArticle: ProgressBar? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ArticleAdapter? = null
    private var articleArrayList = ArrayList<Entry>()
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    internal lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var rootView = inflater.inflate(com.snipex.shantu.assignment.R.layout.fragment_article, container, false)
        initialization(rootView)
        getArticles()
        // SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(com.snipex.shantu.assignment.R.id.swipeContainer) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setOnRefreshListener(this)
        mSwipeRefreshLayout!!.setColorSchemeResources(com.snipex.shantu.assignment.R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        return rootView
    }

    /**
     * initialization of views and others
     *
     * @param @null
     */
    private fun initialization(rootView: View) {
        recyclerView = rootView.findViewById<View>(com.snipex.shantu.assignment.R.id.my_recycler_view) as RecyclerView

        // use a Grid layout manager
        layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView!!.layoutManager = layoutManager

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.addItemDecoration(DividerItemDecoration(recyclerView!!.getContext(), DividerItemDecoration.VERTICAL))
        recyclerView!!.addItemDecoration(DividerItemDecoration(recyclerView!!.getContext(), DividerItemDecoration.HORIZONTAL))


        // adapter
        adapter = ArticleAdapter(requireContext(), articleArrayList)
        recyclerView!!.adapter = adapter

        // View Model
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
    }

    /**
     * get articles from news api
     *
     * @param @null
     */
    private fun getArticles() {
            articleArrayList.removeAll(articleArrayList)
            MainActivity.mIdlingResource?.setIdleState(true)
            articleViewModel.articleResponseLiveData.observe(this, Observer { articleResponse ->
                if (articleResponse != null) {
                    activity?.setTitle(articleResponse.feed.title.label)
                    val articles = articleResponse.feed.entry
                    articleArrayList.addAll(articles)
                    adapter?.notifyDataSetChanged()
                    mSwipeRefreshLayout?.setRefreshing(false);
                    MainActivity.mIdlingResource?.setIdleState(true)
                } else {
                    mSwipeRefreshLayout?.setRefreshing(false)
                    val message: String = getString(R.string.error_msg)
                    showSnack(message)
                }
            })

    }

    override fun onRefresh() {
        articleArrayList.clear()
        adapter?.notifyDataSetChanged()
        getArticles()
    }

    private fun showSnack(message: String) {
        val snackbar = Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }


}
