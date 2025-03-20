package com.example.surftask.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surftask.Fragments.DetailedActivity
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.BookItemBinding

class favoritesRecycler(
    private var favorites : MutableSet<BooksResponse.Item?>,
    private val onFavoriteClick: (MutableSet<BooksResponse.Item?>, Int) -> Unit
): RecyclerView.Adapter<favoritesRecycler.BooksViewHolder>() {

    private var favoriteBookList = favorites.toMutableList()

    class BooksViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int){
        var favoriteBook = favoriteBookList[position]

        holder.binding.bookTitle.text = favoriteBook?.volumeInfo?.title
        holder.binding.bookAuthor.text = favoriteBook?.volumeInfo?.authors?.joinToString()
        val imageUrl = favoriteBook?.volumeInfo?.imageLinks?.thumbnail
        holder.binding.bookPoster.load(imageUrl){
            error(R.drawable.error_image)
        }
        if (favoriteBook?.volumeInfo?.isFavorite == true) {
            holder.binding.favoriteIcon.setImageResource(R.drawable.favorite_fill_ico)
        } else {
            holder.binding.favoriteIcon.setImageResource(R.drawable.favorite_ico)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailedActivity::class.java).apply {
                putExtra("title",favoriteBook?.volumeInfo?.title)
                putExtra("author",favoriteBook?.volumeInfo?.authors?.joinToString(", "))
                putExtra("description",favoriteBook?.volumeInfo?.description)
                putExtra("date",favoriteBook?.volumeInfo?.date)
                putExtra("image", favoriteBook?.volumeInfo?.imageLinks?.thumbnail)
                putExtra("isFavorite", favoriteBook?.volumeInfo?.isFavorite)
            }
            context.startActivity(intent)
            notifyDataSetChanged()
        }

        holder.binding.favoriteIcon.setOnClickListener {
            val context = holder.itemView.context
            val heartIcon = holder.binding.favoriteIcon
            val isFavorite = favoriteBook?.volumeInfo?.isFavorite ?: false

            animate(context, R.drawable.favorite_ico, R.drawable.favorite_fill_ico, isFavorite, heartIcon)

            onFavoriteClick(favorites, position)
            notifyItemChanged(position)
        }
    }

    fun removeBook(book : BooksResponse.Item?) {
        favorites.remove(book)
        favoriteBookList.clear()
        favoriteBookList.addAll(favorites)
        book?.volumeInfo?.isFavorite = false
        notifyDataSetChanged()
    }

    fun updateData(newFavorites: MutableSet<BooksResponse.Item?>){
        favorites = newFavorites
        notifyDataSetChanged()
    }
}