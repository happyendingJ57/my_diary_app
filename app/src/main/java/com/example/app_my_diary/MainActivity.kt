package com.example.app_my_diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.app_my_diary.databinding.ActivityMainBinding
import com.example.app_my_diary.utils.snackbar.CustomSnackBar
import com.example.app_my_diary.utils.snackbar.SnackBarType

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null
    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(null)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main)
        navController = findNavController(R.id.nav_host_fragment_main)
    }
    fun showSnackBar(
        snackBarType: SnackBarType,
        title: String,
        message: String,
        anchorView: View? = null
    ) {
        CustomSnackBar.make(
            findViewById(android.R.id.content), anchorView ?: binding!!.viewBottom,
            snackBarType,
            title,
            message
        ).show()
    }
}