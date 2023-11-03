@file:OptIn(ExperimentalFoundationApi::class)

package com.ketchup.dailymanna.ui.screens

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ketchup.dailymanna.viewmodel.ViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun MainScreen(viewModel: ViewModel, isCalledFromFav: Boolean, context: Context) {

    val savedIndex = viewModel.getSavedPageIndex() // Get the saved index
    val pagerState = rememberPagerState(initialPage = savedIndex)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = "scrollTo"){
        coroutineScope.launch {
            pagerState.animateScrollToPage(savedIndex)
        }
    }

    LaunchedEffect(key1 = "loadTexts") {
        if (isCalledFromFav){
            viewModel.getAllFavorites()
        }
        else{
            viewModel.loadAllMannaTexts()
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.savePageIndex(page)
        }
    }

    val allMannaTexts by viewModel.allMannaTexts.observeAsState(initial = emptyList())
    val titles = listOf("Kartka", "Biblia") // List of tab titles
    var tabIndex by remember { mutableStateOf(0) } // Current selected tab index

    Scaffold(
        bottomBar = {
            BottomAppBar( modifier = Modifier.height(60.dp),
                content = {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        containerColor = MaterialTheme.colorScheme.background, // To make it transparent within BottomAppBar
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
        Column() {
            if (allMannaTexts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    pageCount = allMannaTexts.size,
                    beyondBoundsPageCount = 3
                ) { page ->
                    val mannaPage = allMannaTexts[page]

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 70.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = mannaPage.title, // Display the title from MannaTextEntity
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .weight(1f) // This will make the Text take up all available space
                                        .padding(
                                            top = 10.dp,
                                            end = 12.dp,
                                            start = 12.dp,
                                            bottom = 10.dp
                                        )
                                )

                                val checkedState = remember { mutableStateOf(mannaPage.isFavorite) }

                                IconToggleButton(
                                    checked = checkedState.value,
                                    onCheckedChange = {
                                        checkedState.value = it
                                        viewModel.setFavorite(mannaPage, checkedState.value)
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically) // Align the button vertically
                                ) {
                                    Icon(
                                        tint = if (checkedState.value) {
                                            Color.Cyan
                                        } else {
                                            Color.LightGray
                                        },
                                        modifier = Modifier
                                            .graphicsLayer {
                                                scaleX = 1.3f
                                                scaleY = 1.3f
                                            }
                                            .padding(end = 6.dp),
                                        imageVector = if (checkedState.value) {
                                            Icons.Filled.Favorite
                                        } else {
                                            Icons.Default.FavoriteBorder
                                        },
                                        contentDescription = null
                                    )
                                }
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(Color.Gray)
                                )

                                when (tabIndex) {
                                    0 -> {
                                        Text(
                                            text = mannaPage.text,  // Display the text from MannaTextEntity
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(
                                                    end = 12.dp,
                                                    start = 12.dp,
                                                    top = 8.dp
                                                )
                                                .verticalScroll(rememberScrollState())
                                        )
                                    }

                                    1 -> {
                                        Text(
                                            text = mannaPage.bibleText,  // Display the text from MannaTextEntity
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(
                                                    end = 12.dp,
                                                    start = 12.dp,
                                                    top = 8.dp
                                                )
                                                .verticalScroll(rememberScrollState())
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
         }
       }
    }
}