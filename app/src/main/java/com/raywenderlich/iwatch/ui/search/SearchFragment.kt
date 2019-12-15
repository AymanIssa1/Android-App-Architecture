/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.iwatch.ui.search

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.iwatch.R
import com.raywenderlich.iwatch.action
import com.raywenderlich.iwatch.data.model.Movie
import com.raywenderlich.iwatch.data.model.MoviesResponse
import com.raywenderlich.iwatch.data.net.RetrofitClient
import com.raywenderlich.iwatch.db
import com.raywenderlich.iwatch.snack
import com.raywenderlich.iwatch.ui.add.AddMovieFragment
import com.raywenderlich.iwatch.ui.list.MainActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_movie_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class SearchFragment : Fragment(), SearchAdapter.OnItemClickListener {

    lateinit var retrofitClient: RetrofitClient
    lateinit var query: String

    override fun onItemClick(movie: Movie, itemView: View) {
        activity?.searchLayout?.snack("Add ${movie.title} to your list?", Snackbar.LENGTH_LONG) {
            action("Ok") {
                thread {
                    db.movieDao().insert(movie)
                    goToMainActivity()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofitClient = RetrofitClient()
        activity?.intent?.getStringExtra(AddMovieFragment.SEARCH_QUERY)?.let {
            getMovieList(it)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_movie_search, container, false)
    }

    fun getMovieList(query: String) {
        val response = retrofitClient.searchMovies(query)

        response.enqueue(object : Callback<MoviesResponse> {

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results
                    if (movies != null) {
                        searchResultsRecyclerView.adapter = SearchAdapter(movies, this@SearchFragment)
                    }
                }
            }
        })
    }

    fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}
