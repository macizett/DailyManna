@file:OptIn(ExperimentalFoundationApi::class)

package com.ketchup.dailymanna.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.ketchup.dailymanna.R
import com.ketchup.dailymanna.viewmodel.MannaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun MainScreen(navController: NavController, mannaViewModel: MannaViewModel, initialPage: Int) {

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
            text = { Text(text = "Zamknąć aplikację?", textAlign = TextAlign.Center, fontSize = 18.sp, modifier = Modifier.fillMaxWidth()) })

    }

    LaunchedEffect(key1 = "loadTexts") {
        mannaViewModel.loadAllMannaTexts()
    }

    val allMannaTexts by mannaViewModel.allMannaTexts.observeAsState(initial = emptyList())
    val pagerState = rememberPagerState {
        allMannaTexts.size
    }

    LaunchedEffect(key1 = "scrollTo"){
        coroutineScope.launch {
            pagerState.animateScrollToPage(initialPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (allMannaTexts.isNotEmpty()){
                    mannaViewModel.savePageIndex(page)
                    mannaViewModel.savePageBookID(allMannaTexts[page].bookID)
                }
        }
    }

    val titles = listOf("Kartka", "Biblia")
    val tabIcons = listOf(R.drawable.baseline_menu_book_24, R.drawable.baseline_book_24)
    var tabIndex by remember { mutableStateOf(0) }


    Scaffold(
        bottomBar = {
            BottomAppBar( modifier = Modifier
                .height(140.dp)
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
                            val checkedStateSpk = remember { mutableStateOf(false) }

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

                            IconToggleButton(
                                checked = checkedStateSpk.value,
                                onCheckedChange = {
                                    checkedStateSpk.value = !checkedStateSpk.value
                                    if (!mannaViewModel.textToSpeech.isSpeaking){
                                        mannaViewModel.readText("Fragment:\r\n\r\n${allMannaTexts[pagerState.currentPage]
                                            .bibleText.replace(Regex("\\d+\\.?"), "")}\r\n\r\n Rozważanie: ${allMannaTexts[pagerState.currentPage].text}")
                                    }
                                    else{
                                        mannaViewModel.stopReading()
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(1f)
                            ) {
                                Icon(
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .graphicsLayer {
                                            scaleX = 1.3f
                                            scaleY = 1.3f
                                        }
                                        .padding(end = 6.dp),
                                    imageVector = if (!checkedStateSpk.value) {
                                        ImageVector.vectorResource(R.drawable.baseline_play_circle_24)
                                    } else {
                                        ImageVector.vectorResource(R.drawable.baseline_pause_circle_24)
                                    },
                                    contentDescription = null
                                )
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
            }

            else {
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 3
                ) { page ->
                    val mannaPage = allMannaTexts[pagerState.currentPage]

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        elevation = CardDefaults.elevatedCardElevation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 2.dp, bottom = 150.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Scaffold(
                            backgroundColor =  MaterialTheme.colorScheme.inverseOnSurface,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            bottomBar = {
                                BottomAppBar( modifier = Modifier
                                    .height(60.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomEnd = 8.dp,
                                            bottomStart = 8.dp,
                                            topEnd = 16.dp,
                                            topStart = 16.dp
                                        )
                                    ) ,
                                    content = {
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .align(Alignment.CenterVertically)) {
                                            val checkedStateFav = remember { mutableStateOf(mannaPage.isFavorite) }

                                            IconToggleButton(
                                                checked = checkedStateFav.value,
                                                onCheckedChange = {
                                                    checkedStateFav.value = it
                                                    mannaViewModel.setFavorite(mannaPage, checkedStateFav.value)
                                                },
                                                modifier = Modifier
                                                    .align(Alignment.CenterVertically)
                                                    .weight(1f)   // Align the button vertically
                                            ) {
                                                Icon(
                                                    tint = MaterialTheme.colorScheme.onBackground,
                                                    modifier = Modifier
                                                        .graphicsLayer {
                                                            scaleX = 1.3f
                                                            scaleY = 1.3f
                                                        }
                                                        .padding(end = 6.dp),
                                                    imageVector = if (checkedStateFav.value) {
                                                        Icons.Filled.Favorite
                                                    } else {
                                                        Icons.Default.FavoriteBorder
                                                    },
                                                    contentDescription = null
                                                )
                                            }

                                            IconButton(onClick = {mannaViewModel.shareText(allMannaTexts[pagerState.currentPage])}, modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .align(Alignment.CenterVertically)) {
                                                Icon(Icons.Filled.Share, contentDescription = "Share Button", modifier = Modifier.graphicsLayer {
                                                    scaleX = 1.4f
                                                    scaleY = 1.4f
                                                },
                                                    tint = MaterialTheme.colorScheme.onBackground)
                                            }
                                        }
                                    }
                                )
                            }
                        ) { padding ->
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
                                            .verticalScroll(rememberScrollState())
                                    )

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
}