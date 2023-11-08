@file:OptIn(ExperimentalFoundationApi::class)

package com.ketchup.dailymanna.ui.screens

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Layout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.ketchup.dailymanna.R
import com.ketchup.dailymanna.viewmodel.ViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun MainScreen(navController: NavController, viewModel: ViewModel, initialPageIndex: Int) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showExitConfirmationDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showExitConfirmationDialog = true
    }

    if (showExitConfirmationDialog) {

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showExitConfirmationDialog = false },
            confirmButton = {TextButton(
                onClick = { ActivityCompat.finishAffinity(context as Activity) }
            ) {
                Text("Tak")
            } },
            dismissButton = {TextButton(
                onClick = { showExitConfirmationDialog = false }
            ) {
                Text("Nie")
            }},
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_book_24), contentDescription = "exitAppIcon", tint = MaterialTheme.colorScheme.onBackground) },
            text = { Text(text = "Na pewno chcesz zamknąć aplikację?") })

    }

    LaunchedEffect(key1 = "loadTexts") {
        viewModel.loadAllMannaTexts()
    }

    val allMannaTexts by viewModel.allMannaTexts.observeAsState(initial = emptyList())
    val pagerState = rememberPagerState {
        allMannaTexts.size
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.savePageIndex(page)
        }
    }

    LaunchedEffect(key1 = "scrollTo"){
        coroutineScope.launch {
            pagerState.animateScrollToPage(initialPageIndex)
        }
    }

    val titles = listOf("Kartka", "Biblia")
    val tabIcons = listOf(R.drawable.baseline_menu_book_24, R.drawable.baseline_book_24)
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomAppBar( modifier = Modifier
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)) ,
                content = {
                    Column {
                        TabRow(
                            selectedTabIndex = tabIndex,
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        ) {
                            titles.forEachIndexed { index, title ->
                                Tab(
                                    text = { Text(title) },
                                    icon = {
                                        Icon(
                                            tint = MaterialTheme.colorScheme.onBackground,
                                            imageVector = ImageVector.vectorResource(id = tabIcons[index]),
                                            contentDescription = "book icon"
                                        )
                                    },
                                    selected = tabIndex == index,
                                    onClick = { tabIndex = index }
                                )
                            }
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                ){
                            IconButton(onClick = {
                                navController.navigate("FavoritesScreen") }, modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Favorites Button", modifier = Modifier.graphicsLayer {
                                    scaleX = 1.4f
                                    scaleY = 1.4f
                                },
                                    tint = MaterialTheme.colorScheme.onBackground)
                            }

                            IconButton(onClick = { navController.navigate("SelectorScreen") }, modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()) {
                                Icon(Icons.Filled.Menu, contentDescription = "SelectorScreen", modifier = Modifier.graphicsLayer {
                                    scaleX = 1.4f
                                    scaleY = 1.4f
                                },
                                    tint = MaterialTheme.colorScheme.onBackground)
                            }

                            IconButton(onClick = { viewModel.shareText(allMannaTexts[pagerState.currentPage])}, modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()) {
                                Icon(Icons.Filled.Share, contentDescription = "Share Button", modifier = Modifier.graphicsLayer {
                                    scaleX = 1.4f
                                    scaleY = 1.4f
                                },
                                    tint = MaterialTheme.colorScheme.onBackground)
                            }
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
                    beyondBoundsPageCount = 3
                ) { page ->
                    val mannaPage = allMannaTexts[page]

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        elevation = CardDefaults.elevatedCardElevation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 140.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = mannaPage.title, // Display the title from MannaTextEntity
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .weight(1f)
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
                                        tint = MaterialTheme.colorScheme.onBackground,
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
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant)
                                )

                                when (tabIndex) {
                                    0 -> {
                                        Text(
                                            text = "${mannaPage.text}\r\n\r\n\r\n\r\n",  // Display the text from MannaTextEntity
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(
                                                    end = 10.dp,
                                                    start = 10.dp,
                                                    top = 6.dp
                                                )
                                                .verticalScroll(rememberScrollState())
                                        )
                                    }

                                    1 -> {
                                        Text(
                                            text = "${mannaPage.bibleText}\r\n\r\n\r\n\r\n",  // Display the bibleText from MannaTextEntity
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(
                                                    end = 6.dp,
                                                    start = 6.dp,
                                                    top = 6.dp
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

@Composable
fun BackAlertDialog(){
    AlertDialog(onDismissRequest = { /*TODO*/ }, buttons = {

    })
}