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

package com.raywenderlich.iwatch.ui.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.iwatch.R
import com.raywenderlich.iwatch.data.model.Movie
import com.raywenderlich.iwatch.db
import com.raywenderlich.iwatch.ui.add.AddMovieActivity
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlin.concurrent.thread

class MovieListFragment : Fragment(), MovieListAdapter.OnCheckedListener {

    override fun onChecked(movie: Movie, isChecked: Boolean) {
        movie.watched = isChecked
        thread {
            db.movieDao().updateMovie(movie)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thread {
            val moviesList = db.movieDao().getAll()
            activity?.runOnUiThread {
                moviesRecyclerView.adapter = MovieListAdapter(moviesList, this)
            }
        }
        fab.setOnClickListener {
            goToAddMovieActivity()
        }
        deleteMoviesButton.setOnClickListener {
            deleteMovies()
        }
    }

    fun goToAddMovieActivity() {
        val intent = Intent(activity, AddMovieActivity::class.java)
        startActivity(intent)
    }

    fun deleteMovies() {
        db.movieDao().deleteMovies(true)
        thread {
            val moviesList = db.movieDao().getAll()
            activity?.runOnUiThread {
                moviesRecyclerView.adapter = MovieListAdapter(moviesList, this)
                moviesRecyclerView.adapter.notifyDataSetChanged()
            }
        }
    }
}
