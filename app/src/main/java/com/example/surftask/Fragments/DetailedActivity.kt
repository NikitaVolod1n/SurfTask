package com.example.surftask.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.surftask.R
import com.example.surftask.databinding.ActivityDetailedBinding
import com.example.surftask.ui.animate
import com.example.surftask.ui.showToast

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val description = intent.getStringExtra("description")
        val date = intent.getStringExtra("date")?.substring(0,4)
        val image = intent.getStringExtra("image")
        var isFavorite = intent.getBooleanExtra("isFavorite", false)

        binding.bookAuthor.text = author
        binding.bookTitle.text = title
        binding.descriptionText.text = description
        binding.bookYear.text = date
        binding.bookPoster.load(image){
            error(R.drawable.error_image)
        }
        if (isFavorite == true) {
            binding.favoriteIcon.setImageResource(R.drawable.favorite_fill_ico)
        } else {
            binding.favoriteIcon.setImageResource(R.drawable.favorite_ico)
        }

        binding.arrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.favoriteIcon.setOnClickListener {
            val context = this
            val heartIcon = binding.favoriteIcon

            animate(context, R.drawable.favorite_ico, R.drawable.favorite_fill_ico, isFavorite, heartIcon)

            isFavorite = !isFavorite
            if(isFavorite){
                try {
                    TODO("по факту надо прописать удаление, отсылку isFavorite в активити, не успеваю")
                    showToast(this, context.getString(R.string.add_to_favorite_success), R.color.lightBlue)
                }catch (e: Exception){
                    showToast(this, context.getString(R.string.add_to_favorite_unluck), R.color.lightRed)
                }
            }
            else {
                try {
                    showToast(this, context.getString(R.string.delete_from_favorite_success), R.color.lightBlue)
                }catch (e: Exception) {
                    showToast(this, context.getString(R.string.delete_from_favorite_unluck), R.color.lightRed)
                }
            }
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("isFavorite", isFavorite)
            }
            sendBroadcast(intent)
        }
    }
}