package ru.gb.homework_8.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import ru.gb.homework_8.R
import ru.gb.homework_8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavigationBar.setOnItemSelectedListener(this)
        setContentView(binding.root)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.e("onNavigationItemSelected", "hello")
        when (item.itemId){
            R.id.action_main ->{
                Log.e("onNavigationItemSelected", "hello")
                setContentProviderFragment()}
        }
        return true
    }


    @SuppressLint("CommitTransaction")
    private fun setContentProviderFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_holder, ContentProviderFragment.newInstance())
            .addToBackStack(null)
            .commit()

    }

}