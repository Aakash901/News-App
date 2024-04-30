package com.example.moengage_task.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moengage_task.model.Article
import com.example.moengage_task.model.ResponseData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NewsViewModel : ViewModel() {
    // MutableLiveData for holding the list of articles
    private val _articlesList = MutableLiveData<List<Article>>()
    val articlesList: LiveData<List<Article>>
        get() = _articlesList

    // Function to fetch data from the API using coroutines
    fun fetchDataFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            var httpsURLConnection: HttpURLConnection? = null

            try {
                // here first i have to create URL object for the API endpoint
                val url = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
                httpsURLConnection = url.openConnection() as HttpURLConnection

                val code = httpsURLConnection.responseCode
              // Checking if the response code is not 200 (OK) then printing exception
                if (code != 200) {
                    throw Exception("Error from server: $code")
                }
                // now here we read the response data from the input stream
                val bufferedReader = BufferedReader(InputStreamReader(httpsURLConnection.inputStream))
                val jsonStringHolder = StringBuilder()
                while (true) {
                    val readLine = bufferedReader.readLine() ?: break
                    jsonStringHolder.append(readLine)
                }

                // Parsing the JSON response using Gson into a ResponseData object
                //so gson is 3rd party library but not used in any type of network call here
                val responseData = Gson().fromJson(jsonStringHolder.toString(), ResponseData::class.java)

                // Update the MutableLiveData with the list of articles coming from server
                _articlesList.postValue(responseData.articles)

            } catch (e: Exception) {
                // Handle exception
            } finally {
                httpsURLConnection?.disconnect()
            }
        }
    }
}
