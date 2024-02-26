package com.ketchup.dailymanna

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import java.util.Locale

class MainActivity : ComponentActivity() {

    lateinit var textToSpeech: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.forLanguageTag("pl-PL"))
            }
        })

        val mannaDao = AppDatabase.getInstance(this).mannaTextDao()
        val viewModel = ViewModel(mannaDao, application, this, textToSpeech)


        fun initializeDatabase() {
            lifecycleScope.launch(Dispatchers.IO) {
                    contentParser.parseAndInsertMannaPages(this@MainActivity, lifecycleScope,"MannaText.json", mannaDao)
                    withContext(Dispatchers.Main){

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
            initializeDatabase()
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

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyMannaTheme {

    }
}