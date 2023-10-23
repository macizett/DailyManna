package com.ketchup.dailymanna

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
import com.ketchup.dailymanna.ui.screens.MainScreen
import com.ketchup.dailymanna.ui.theme.MannaNaCoDzieńTheme
import com.ketchup.dailymanna.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mannaDao = AppDatabase.getInstance(this).mannaTextDao()

        fun initializeDatabase() {
            val mannaCount = mannaDao.getAllMannaTexts().size

            if (mannaCount == 0) {
                contentParser.parseAndInsertMannaPages(this, lifecycleScope,"MannaText.json", mannaDao)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            initializeDatabase()
        }

        setContent {
            MannaNaCoDzieńTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = ViewModel(mannaDao))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MannaNaCoDzieńTheme {

    }
}