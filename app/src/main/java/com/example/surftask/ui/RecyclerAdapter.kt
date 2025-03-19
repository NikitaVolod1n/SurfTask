package com.example.surftask.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surftask.Fragments.favorites
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.BookItemBinding

class RecyclerAdapter(
    private var books: List<BooksResponse.Item>?,
    private val onFavoriteClick: (List<BooksResponse.Item>?, Int) -> Unit
): RecyclerView.Adapter<RecyclerAdapter.BooksViewHolder>() {

    class BooksViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return books?.size ?: 0
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val book = books?.get(position)

        val isFavorite = if(favorites.any { it?.id == book?.id }) true else false
        holder.binding.bookTitle.text = book?.volumeInfo?.title
        holder.binding.bookAuthor.text = book?.volumeInfo?.authors?.joinToString()
        book?.volumeInfo?.isFavorite = isFavorite
        val imageUrl = book?.volumeInfo?.imageLinks?.thumbnail
        holder.binding.bookPoster.load(imageUrl){
            error(R.drawable.error_image)
        }
        if (book?.volumeInfo?.isFavorite == true) {
            holder.binding.favoriteIcon.setImageResource(R.drawable.favorite_fill_ico)
        } else {
            holder.binding.favoriteIcon.setImageResource(R.drawable.favorite_ico)
        }

        holder.binding.favoriteIcon.setOnClickListener {
            val context = holder.itemView.context
            val heartIcon = holder.binding.favoriteIcon
            val isFavorite = books?.get(position)?.volumeInfo?.isFavorite ?: false

            val shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.heart_to_small)
            heartIcon.startAnimation(shrinkAnimation)

            if (isFavorite) {
                heartIcon.setImageResource(R.drawable.favorite_ico)
            } else {
                heartIcon.setImageResource(R.drawable.favorite_fill_ico)
            }

            heartIcon.postDelayed({
                val growAnimation = AnimationUtils.loadAnimation(context, R.anim.heart_to_big)
                heartIcon.startAnimation(growAnimation)
            }, 150)

            onFavoriteClick(books, position)
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateBooks(newBooks: List<BooksResponse.Item>?) {
        books = newBooks
        notifyDataSetChanged()
    }
}