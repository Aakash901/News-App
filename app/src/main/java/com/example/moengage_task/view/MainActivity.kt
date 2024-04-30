package com.example.moengage_task.view

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moengage_task.R
import com.example.moengage_task.databinding.ActivityMainBinding
import com.example.moengage_task.viewmodels.NewsViewModel

class MainActivity : AppCompatActivity() {

    //initializing all the variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel
    private var isReversed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        // Setting  the status bar color to primary color of app
        setStatusBarColor()

        //fetching data from server and updating ui according to response
        fetchDataFromViewModel()

        //filter btn to show news according to old to new or new to old
        onFilterBtnClicked()


    }

    private fun onFilterBtnClicked() {
        binding.filterBtn.setOnClickListener {
            adapter.reverseList()
            isReversed = !isReversed
        }
    }

    private fun fetchDataFromViewModel() {
        showProgressBar()
        viewModel.articlesList.observe(this, Observer { articlesList ->
            hideProgressBar()

            // Initialize RecyclerView adapter with fetched data
            adapter = NewsAdapter(articlesList)
            binding.recyclerView.adapter = adapter

            Log.d("Network", "Fetched ${articlesList.size} articles")
        })

        viewModel.fetchDataFromApi()
    }

    // Function to set the status bar color
    private fun setStatusBarColor() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getResources().getColor(R.color.primaryColor)
    }

    // Function to show the progress bar
    private fun showProgressBar() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        binding.recyclerView.visibility = RecyclerView.GONE
    }

    // Function to hide the progress bar
    private fun hideProgressBar() {
        binding.progressBar.visibility = ProgressBar.GONE
        binding.recyclerView.visibility = RecyclerView.VISIBLE
    }

}