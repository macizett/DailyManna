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
import com.example.compose.DailyMannaTheme
import com.ketchup.dailymanna.ui.NavController
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mannaDao: MannaTextDao by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentParser.parseAndInsertMannaPages(this@MainActivity, lifecycleScope, dao = mannaDao)

            setContent {
                DailyMannaTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavController(context = this@MainActivity)
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