@file:OptIn(ExperimentalFoundationApi::class)

package com.ketchup.dailymanna.ui.screens

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ketchup.dailymanna.AppDatabase
import com.ketchup.dailymanna.R
import com.ketchup.dailymanna.viewmodel.ViewModel

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun MainScreen(viewModel: ViewModel) {

    LaunchedEffect(key1 = "loadTexts") {
        viewModel.loadAllMannaTexts()
    }

    val allMannaTexts by viewModel.allMannaTexts.observeAsState(initial = emptyList())

    if (allMannaTexts.isNotEmpty()) {

        Scaffold(
            backgroundColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { /* Do something */ }) {
                            Icon(Icons.Filled.Home, contentDescription = null)
                        }
                        IconButton(onClick = { /* Do something */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            val pagerState = rememberPagerState()

            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalPager(
                    state = pagerState,
                    pageCount = allMannaTexts.size,
                    beyondBoundsPageCount = 4
                ) { page ->

                    val mannaPage = allMannaTexts[page]

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = mannaPage.title,  // Display the title from MannaTextEntity
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                        end = 12.dp,
                                        start = 12.dp,
                                        bottom = 10.dp
                                    )
                            )

                            val checkedState = remember { mutableStateOf(false) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Color.Gray)
                            )
                            Text(
                                text = mannaPage.bibleText,  // Display the text from MannaTextEntity
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(
                                        end = 12.dp,
                                        start = 12.dp
                                    )
                                    .verticalScroll(rememberScrollState())
                            )
                        }
                    }
                }
            }
        }
    }
    else{
        Box(
            modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){

}