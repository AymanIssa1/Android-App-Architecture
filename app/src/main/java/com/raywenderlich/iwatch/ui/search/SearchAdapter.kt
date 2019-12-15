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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.iwatch.R
import com.raywenderlich.iwatch.data.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_details.view.*

class SearchAdapter(
        private val movies: List<Movie>,
        private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchAdapter.SearchMoviesHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, itemView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMoviesHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_details, parent, false)
        return SearchMoviesHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: SearchMoviesHolder, position: Int) = holder.bind(movies[position], clickListener)

    inner class SearchMoviesHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie, listener: OnItemClickListener) = with(view) {
            searchTitleTextView.text = movie.title
            searchReleaseDateTextView.text = movie.releaseDate
            searchOverviewTextView.text = movie.overview
            Picasso.get().load("https://image.tmdb.org/t/p/w500/" + movie.posterPath).into(searchImageView)

            setOnClickListener {
                listener.onItemClick(movie, it)
            }
        }
    }
}
