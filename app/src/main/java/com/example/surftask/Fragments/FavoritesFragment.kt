package com.example.surftask.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.FragmentFavoritesBinding
import com.example.surftask.databinding.FragmentSearchBinding
import com.example.surftask.ui.GridSpacingItemDecoration
import com.example.surftask.ui.RecyclerAdapter
import com.example.surftask.ui.favoritesRecycler
import com.example.surftask.ui.showToast

class FavoritesFragment: Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: favoritesRecycler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        adapter = favoritesRecycler(favorites){ favorites, position ->
            toggleFavorite(favorites, position)
        }

        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorites.addItemDecoration(GridSpacingItemDecoration())

        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    private fun toggleFavorite(favorites: MutableSet<BooksResponse.Item?>, position: Int) {
        favorites.elementAt(position)?.volumeInfo?.isFavorite =
            !favorites.elementAt(position)?.volumeInfo?.isFavorite!!
        if (favorites.elementAt(position)?.volumeInfo?.isFavorite == false) {
            try {
                favorites.remove(favorites.elementAt(position))
                context?.let {
                    showToast(
                        requireContext(),
                        it.getString(R.string.delete_from_favorite_success),
                        R.color.lightBlue
                    )
                }
            } catch (e: Exception) {
                context?.let {
                    showToast(
                        requireContext(),
                        it.getString(R.string.delete_from_favorite_unluck),
                        R.color.lightRed
                    )
                }
            }
        }
    }
}