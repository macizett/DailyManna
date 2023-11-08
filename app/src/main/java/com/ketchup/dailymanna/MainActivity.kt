package com.ketchup.dailymanna

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.compose.DailyMannaTheme
import com.ketchup.dailymanna.ui.NavController
import com.ketchup.dailymanna.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mannaDao = AppDatabase.getInstance(this).mannaTextDao()
        val viewModel = ViewModel(mannaDao, application, this)

        val sharedPreferences = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        fun initializeDatabase() {
            lifecycleScope.launch(Dispatchers.IO) {
                    contentParser.parseAndInsertMannaPages(this@MainActivity, lifecycleScope,"MannaText.json", mannaDao)
                    withContext(Dispatchers.Main){

                        sharedPreferences.edit().putInt("databaseStatement", 1).apply()
                        setContent {
                            DailyMannaTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    NavController(viewModel = viewModel)
                                }
                            }
                        }
                    }
            }
        }

        if (sharedPreferences.getInt("databaseStatement", 0) == 0) {
            initializeDatabase()
        }
        else{
            setContent {
                DailyMannaTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavController(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyMannaTheme {

    }
}