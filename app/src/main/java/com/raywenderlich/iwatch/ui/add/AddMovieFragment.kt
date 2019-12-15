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

package com.raywenderlich.iwatch.ui.add

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.iwatch.R
import com.raywenderlich.iwatch.data.model.Movie
import com.raywenderlich.iwatch.db
import com.raywenderlich.iwatch.snack
import com.raywenderlich.iwatch.ui.list.MainActivity
import com.raywenderlich.iwatch.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_add_movie.*
import kotlinx.android.synthetic.main.fragment_add_movie.*
import kotlin.concurrent.thread

class AddMovieFragment : Fragment() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_button.setOnClickListener {
            goToSearchMovieActivity()
        }
        add_movie_button.setOnClickListener {
            addMovie()
        }
    }

    fun goToSearchMovieActivity() {
        if (movie_title.text.isBlank()) {
            activity?.addMovieLayout?.snack(getString(R.string.provide_title_message), Snackbar.LENGTH_LONG)
            return
        }
        val title: String = movie_title.text.toString()
        val intent = Intent(activity, SearchActivity::class.java)
        intent.putExtra(AddMovieFragment.SEARCH_QUERY, title)
        startActivity(intent)
    }

    fun addMovie() {
        if (movie_title.text.isBlank()) {
            activity?.addMovieLayout?.snack(getString(R.string.provide_title_message), Snackbar.LENGTH_LONG)
            return
        }
        val movie = Movie(title = movie_title.text.toString(), releaseDate = releaseDate.text.toString())
        thread {
            db.movieDao().insert(movie)
        }
        goToMainActivity()
    }

    fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
