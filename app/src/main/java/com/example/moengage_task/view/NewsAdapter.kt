package com.example.moengage_task.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moengage_task.R
import com.example.moengage_task.databinding.ItemNewsArticleBinding
import com.example.moengage_task.model.Article

class NewsAdapter(private var articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        //here im trying to bind the data into views
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
    // Reverses the list of articles and notifies the adapter ,
    // this function is used to filter the news according to user
    fun reverseList() {
        articles = articles.reversed()
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemNewsArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            with(binding) {
                // Bind data to views using ViewBinding
                tvArticleTitle.text = article.title ?: "N/A"
                tvAuthor.text = " ${article.author ?: "N/A"}"
                tvSource.text = "${article.source.name ?: "Unknown"}"
                tvPublishedDate.text = " ${article.publishedAt ?: "N/A"}"
                tvDescription.text = article.description?.let {
                    val maxLength = 100 // fixing the size of description so that user can open the news
                    if (it.length > maxLength) {
                        val trimmedDescription = it.substring(0, maxLength)
                        val lastSpaceIndex = trimmedDescription.lastIndexOf(" ")
                        if (lastSpaceIndex != -1) {
                            "${trimmedDescription.substring(0, lastSpaceIndex)}. . .🌐"
                        } else {
                            "${trimmedDescription.substring(0, maxLength)}. . .🌐"
                        }
                    } else {
                        it
                    }
                } ?: "N/A"

                // Loading image using Glide library to look news page more user attractive
                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.loading_icon)
                    .centerCrop()
                    .error(R.drawable.loading_icon)
                    .into(ivArticleImage)

                // Handle "Read More" button click , it will open web page
                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
