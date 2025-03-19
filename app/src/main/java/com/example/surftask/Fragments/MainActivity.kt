package com.example.surftask.Fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.surftask.R
import com.example.surftask.Retrofit.BooksResponse
import com.example.surftask.databinding.ActivityMainBinding

var favorites: MutableSet<BooksResponse.Item?> = mutableSetOf()

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var favorites : MutableSet<BooksResponse.Item?> = mutableSetOf()

        val searchFragment=SearchFragment()
        val favoritesFragment=FavoritesFragment()

        setCurrentFragment(searchFragment)

        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_search->setCurrentFragment(searchFragment)
                R.id.navigation_favorites->setCurrentFragment(favoritesFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
}