package com.example.movie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.movie.DetailesMovieActivity
import com.example.movie.R
import com.example.movie.databinding.ItemRowsBinding
import com.example.movie.response.MovieListResponse
import com.example.movie.utils.Constants.POSTER_BASEURL
import com.google.android.gms.common.api.Result

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var binding: ItemRowsBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowsBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder: RecyclerView.ViewHolder(binding.root){

        fun bind(item: com.example.movie.response.Result){
            binding.apply {
                tvMovieName.text = item.title
                tvRate.text = item.vote_average.toString()
                val moviePosterURL = POSTER_BASEURL + item.poster_path
                imgMovie.load(moviePosterURL){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                tvLang.text=item.original_language
                tvMovieDateReleased.text=item.release_date

                root.setOnClickListener{
                    val intent = Intent(context, DetailesMovieActivity::class.java)
                    intent.putExtra("id", item.id)
                    context.startActivity(intent)
                }
            }
        }

    }

    private val differCallBack=object : DiffUtil.ItemCallback<com.example.movie.response.Result>(){
        override fun areItemsTheSame(
            oldItem: com.example.movie.response.Result,
            newItem: com.example.movie.response.Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: com.example.movie.response.Result,
            newItem: com.example.movie.response.Result
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

}