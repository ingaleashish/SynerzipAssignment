package com.snipex.shantu.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snipex.shantu.assignment.R
import com.snipex.shantu.assignment.response.Entry
import com.squareup.picasso.Picasso
import java.util.*


class ArticleAdapter(private val context: Context, internal var articleArrayList: ArrayList<Entry>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ArticleAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_each_row_movie_article, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ArticleAdapter.ViewHolder, i: Int) {
        val article = articleArrayList[i]
        Picasso.with(context).load(article.imimage[2].label).placeholder(R.mipmap.ic_launcher).into(viewHolder.imgViewCover)
        viewHolder.tvTitle.text = article.title.label
        viewHolder.tvName.text = article.imname.label
        viewHolder.tvRights.text = article.rights.label
        viewHolder.tvPrice.text = article.imprice.attributes.amount + article.imprice.attributes.currency
        viewHolder.tvArtist.text = article.imartist.label
        viewHolder.tvReleaseDate.text = article.imreleaseDate.label

    }

    override fun getItemCount(): Int {
        return articleArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgViewCover: ImageView
        val tvTitle: TextView
        val tvName: TextView
        val tvRights: TextView
        val tvPrice: TextView
        val tvArtist: TextView
        val tvReleaseDate: TextView

        init {
            imgViewCover = itemView.findViewById<View>(R.id.imgViewCover) as ImageView
            tvTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
            tvName = itemView.findViewById<View>(R.id.tvName) as TextView
            tvRights = itemView.findViewById<View>(R.id.tvRights) as TextView
            tvPrice = itemView.findViewById<View>(R.id.tvPrice) as TextView
            tvArtist = itemView.findViewById<View>(R.id.tvArtist) as TextView
            tvReleaseDate = itemView.findViewById<View>(R.id.tvReleaseDate) as TextView
        }
    }
}
