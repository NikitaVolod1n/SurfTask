package com.example.surftask.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.Retrofit.restBooksAPI
import com.example.surftask.databinding.FragmentSearchBinding
import com.example.surftask.ui.GridSpacingItemDecoration
import com.example.surftask.ui.SearchRecycler
import com.example.surftask.ui.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchRecycler
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private var searchJob: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = SearchRecycler(emptyList()){ books, position ->
            toggleFavorite(books, position)
        }
        binding.rvBooks.adapter = adapter
        binding.rvBooks.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvBooks.addItemDecoration(GridSpacingItemDecoration())
        val searchIcon = ContextCompat.getDrawable(requireContext(), R.drawable.search_ico)
        binding.editText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)

        afterTextChanged()
        setupSearchListener()

        binding.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val query = binding.editText.text.toString().trim()
                hideKeyboard()
                if (query.isNotEmpty()) {
                    getBooks(query)
                }
                binding.editText.clearFocus()
                true
            } else {
                false
            }
        }

        binding.editText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.editText.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (binding.editText.right - drawableEnd.bounds.width() - binding.editText.paddingEnd)) {
                    binding.editText.text.clear()
                    binding.emptyView.visibility = View.GONE
                    binding.errorTextView.visibility = View.GONE
                    binding.reloadButton.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    searchJob?.cancel()
                    return@setOnTouchListener true
                }
            }
            false
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchListener() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                adapter.updateBooks(emptyList())
                binding.progressBar.visibility = View.GONE

                val query = s?.toString()?.trim()
                if (query.isNullOrEmpty()) return

                searchRunnable?.let { handler.removeCallbacks(it) }

                searchRunnable = Runnable {
                    getBooks(query)
                    hideKeyboard()
                }

                handler.postDelayed(searchRunnable!!, 2000)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun hideKeyboard() {
        requireActivity().currentFocus?.let { view ->
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun afterTextChanged(){
        binding.editText.doAfterTextChanged { text ->
            binding.searchPrompt.visibility = if (text.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.emptyView.visibility = View.GONE
            val searchIcon = ContextCompat.getDrawable(requireContext(), R.drawable.search_ico) // Левая иконка
            val clearIcon = if (text.isNullOrEmpty()) null else ContextCompat.getDrawable(requireContext(), R.drawable.close_ico) // Правая иконка

            binding.editText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, clearIcon, null)
            }
    }

    private fun getBooks(query: String) {
        searchJob?.cancel()
        adapter.updateBooks(emptyList())
        binding.progressBar.visibility = View.VISIBLE
        searchJob = lifecycleScope.launch {
            try {
                val response = restBooksAPI.getBookByName(query)
                val books = response.items
                binding.searchPrompt.visibility = View.GONE
                binding.errorTextView.visibility = View.GONE
                binding.reloadButton.visibility = View.GONE
                adapter.updateBooks(books)
                if(books == null){
                    binding.emptyView.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                binding.errorTextView.visibility = View.VISIBLE
                binding.reloadButton.visibility = View.VISIBLE
                binding.reloadButton.setOnClickListener {
                    binding.errorTextView.visibility = View.GONE
                    binding.reloadButton.visibility = View.GONE
                    getBooks(query)
                }
            }
            finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    fun toggleFavorite(books: List<BooksResponse.Item>?, position: Int) {
        books?.get(position)?.volumeInfo?.isFavorite = !books[position].volumeInfo.isFavorite
        if (books?.get(position)?.volumeInfo?.isFavorite == true) {
            try {
                favorites.add(books[position])
                context?.let { showToast(requireContext(), it.getString(R.string.add_to_favorite_success), R.color.lightBlue) }
            }catch (e: Exception){
                context?.let { showToast(requireContext(), it.getString(R.string.add_to_favorite_unluck), R.color.lightRed) }
            }
        } else if (books?.get(position)?.volumeInfo?.isFavorite == false) {
            try {
                books[position].volumeInfo.isFavorite = true
                favorites.remove(books[position])
                books[position].volumeInfo.isFavorite = false
                context?.let { showToast(requireContext(), it.getString(R.string.delete_from_favorite_success), R.color.lightBlue) }
            }catch (e: Exception) {
                context?.let { showToast(requireContext(), it.getString(R.string.delete_from_favorite_unluck), R.color.lightRed) }
            }
        }
    }
}
