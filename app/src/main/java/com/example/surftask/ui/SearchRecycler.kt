package com.example.surftask.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surftask.Fragments.DetailedActivity
import com.example.surftask.Fragments.favorites
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.BookItemBinding

class SearchRecycler(
    private var books: List<BooksResponse.Item>?,
    private val onFavoriteClick: (List<BooksResponse.Item>?, Int) -> Unit
): RecyclerView.Adapter<SearchRecycler.BooksViewHolder>() {

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

        var isFavorite = if(favorites.any { it?.id == book?.id }) true else false
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


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailedActivity::class.java).apply {
                putExtra("title",book?.volumeInfo?.title)
                putExtra("author",book?.volumeInfo?.authors?.joinToString(", "))
                putExtra("description",book?.volumeInfo?.description)
                putExtra("date",book?.volumeInfo?.date)
                putExtra("image", book?.volumeInfo?.imageLinks?.thumbnail)
                putExtra("isFavorite", book?.volumeInfo?.isFavorite)
            }
            context.startActivity(intent)
            notifyDataSetChanged()
        }

        holder.binding.favoriteIcon.setOnClickListener {
            val context = holder.itemView.context
            val heartIcon = holder.binding.favoriteIcon

            animate(context, R.drawable.favorite_ico, R.drawable.favorite_fill_ico, isFavorite, heartIcon)

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