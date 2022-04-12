package com.gs.apod.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gs.apod.R
import com.gs.apod.adapter.ApodAdapter
import com.gs.apod.databinding.ActivityHomeBinding
import com.gs.apod.utils.Utils
import com.gs.apod.viewmodel.SharedViewModel

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var adapter:ApodAdapter
    lateinit var sharedViewModel : SharedViewModel
    var isDark : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater);
        setContentView(binding.root)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //Initialise the bottom Bav bar with selection listeners
        getData()
        binding.darkModeToggle.setOnClickListener(View.OnClickListener {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                binding.darkModeToggle.text = "white mode"
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                binding.darkModeToggle.text = "dark mode"
            }
            isDark = !isDark
//            binding.root.invalidate()
        })

    }

    /**
     * Get the initial call to get the 10 APODs from NASA and it will be observed by Fragment -> HomeFragment
     * Called only once per app launch and data state for favourites can be posted to favourites fragment.
     */
    private fun getData() {
        initBnb()
        sharedViewModel.noInternet.observe(this, Observer { noInternet ->
            if(noInternet){
                Toast.makeText(this, "Please check your connectivity! You can see your favorites until then", Toast.LENGTH_LONG).show()
                if(Utils.isOnline(this)){
                    sharedViewModel.noInternet.value = false
                }
//                sharedViewModel.selectedFragId = R.id.nav_fav
                if(binding.pbMain.isVisible){
                    binding.pbMain.isVisible = false
                }
            }
        })
        if(sharedViewModel.apodItem.value.isNullOrEmpty()){
            binding.pbMain.isVisible = true
        }
        sharedViewModel.apodItem.observe(this, Observer { t ->
            /* if(t.isNotEmpty()){
                 Toast.makeText(this@HomeActivity, "Data fetched" , Toast.LENGTH_SHORT).show()
             }*/
            if(binding.pbMain.isVisible){
                binding.pbMain.isVisible = false
            }
        })


    }


    private fun initBnb() {

        binding.bnb.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->{
                    var fragment = supportFragmentManager.findFragmentByTag("Frag1")
                    if( fragment== null){
                        supportFragmentManager.beginTransaction().replace(binding.mainContainer.id, HomeFragment(), "Frag1").commit()
                    }else{
                        supportFragmentManager.beginTransaction().show(fragment).commit()
                    }
                    sharedViewModel.selectedFragId = R.id.nav_home
                    return@setOnItemSelectedListener true
                }

                R.id.nav_fav ->{
                    var fragment = supportFragmentManager.findFragmentByTag("Frag2")
                    if( fragment== null){
                        supportFragmentManager.beginTransaction().replace(binding.mainContainer.id, FavoritesFragment(), "Frag2").commit()
                    }else{
                        supportFragmentManager.beginTransaction().show(fragment).commit()
                    }
                    sharedViewModel.selectedFragId = R.id.nav_fav
                    return@setOnItemSelectedListener true
                }

                R.id.nav_search ->{
                    var fragment = supportFragmentManager.findFragmentByTag("Frag3")
                    if( fragment== null){
                        supportFragmentManager.beginTransaction().replace(binding.mainContainer.id, SearchFragment(), "Frag3").commit()
                    }else{
                        supportFragmentManager.beginTransaction().show(fragment).commit()
                    }
                    sharedViewModel.selectedFragId = R.id.nav_search
                    return@setOnItemSelectedListener true
                }
            }
            true
        }

        binding.bnb.selectedItemId = sharedViewModel.selectedFragId
    }


}