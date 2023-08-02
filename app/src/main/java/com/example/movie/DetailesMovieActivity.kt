package com.example.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.movie.api.ApiClient
import com.example.movie.api.ApiService
import com.example.movie.databinding.ActivityDetailesMovieBinding
import com.example.movie.response.DetailMovieResponse
import com.example.movie.utils.Constants.POSTER_BASEURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailesMovieActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityDetailesMovieBinding
    private val api: ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailesMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra("id", 1)

        binding.apply {
            val callDetailMovieApi = api.getMoviedetail(movieId)
            callDetailMovieApi.enqueue(object : Callback<DetailMovieResponse> {
                override fun onResponse(
                    call: Call<DetailMovieResponse>,
                    response: Response<DetailMovieResponse>
                ) {
                    when(response.code()){
                        in 200..299->{
                            response.body().let { itBody->
                                val imagePoster = POSTER_BASEURL + itBody!!.poster_path
                                imgMovie.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgBackground.load(imagePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }

                                tvMovieName.text=itBody.title
                                tvTagline.text=itBody.tagline
                                tvMovieDateReleased.text=itBody.release_date
                                tvRatingReleased.text=itBody.vote_average.toString()
                                tvRuntimeReleased.text=itBody.runtime.toString()
                                tvBudgetReleased.text=itBody.budget.toString()
                                tvRevenueReleased.text=itBody.revenue.toString()
                                tvOverviewReleased.text=itBody.overview
                            }
                        }
                        in 300..399->{
                            Log.d("Response Code", "Redirection messages : ${response.code()}")
                        }
                        in 400..499->{
                            Log.d("Response Code", "Client error responses : ${response.code()}")
                        }
                        in 500..599->{
                            Log.d("Response Code", "Server error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}