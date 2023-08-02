package com.example.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie.adapter.MovieAdapter
import com.example.movie.api.ApiClient
import com.example.movie.api.ApiService
import com.example.movie.databinding.ActivityMainBinding
import com.example.movie.response.MovieListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy { MovieAdapter() }
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            prgBarMovie.visibility=View.VISIBLE

            val callMovieApi = api.getPopularMovie(1)
            callMovieApi.enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    prgBarMovie.visibility=View.GONE
                    when(response.code()){
                        in 200..299->{response.body().let { itBody->
                            itBody?.results.let {itData->
                                if(itData!!.isNotEmpty()){
                                    movieAdapter.differ.submitList(itData)
                                    rvMovie.apply {
                                        layoutManager=LinearLayoutManager(this@MainActivity)
                                        adapter=movieAdapter
                                    }
                                }
                            }
                        }}
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

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    binding.prgBarMovie.visibility=View.GONE
                    Log.e("onFailure", "Error : ${t.message}")
                }

            })
        }
    }
}