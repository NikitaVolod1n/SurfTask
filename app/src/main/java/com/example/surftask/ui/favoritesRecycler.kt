package com.example.surftask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.BookItemBinding

class favoritesRecycler(
    private var favorites : MutableSet<BooksResponse.Item?>,
    private val onFavoriteClick: (MutableSet<BooksResponse.Item?>, Int) -> Unit
): RecyclerView.Adapter<favoritesRecycler.BooksViewHolder>() {

    class BooksViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int){
        var favoriteBook = favorites.elementAt(position)

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

        holder.binding.favoriteIcon.setOnClickListener {
            val context = holder.itemView.context
            val heartIcon = holder.binding.favoriteIcon
            val isFavorite = favoriteBook?.volumeInfo?.isFavorite ?: false

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

            onFavoriteClick(favorites, position)
            notifyItemChanged(position)
        }
    }

    fun updateData(newFavorites: MutableSet<BooksResponse.Item?>){
        favorites = newFavorites
        notifyDataSetChanged()
    }
}