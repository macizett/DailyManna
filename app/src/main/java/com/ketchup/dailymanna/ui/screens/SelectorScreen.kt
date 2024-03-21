package com.ketchup.dailymanna.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.ketchup.dailymanna.R
import com.ketchup.dailymanna.viewmodel.MannaViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectorScreen(mannaViewModel: MannaViewModel, navController: NavController){

    val bookNamesList = listOf(
        R.string.gospel_of_matthew,
        R.string.gospel_of_mark,
        R.string.gospel_of_luke,
        R.string.gospel_of_john,
        R.string.acts_of_the_apostles,
        R.string.letter_to_the_romans,
        R.string.first_letter_to_the_corinthians,
        R.string.second_letter_to_the_corinthians,
        R.string.letter_to_the_galatians,
        R.string.letter_to_the_ephesians,
        R.string.letter_to_the_philippians,
        R.string.letter_to_the_colossians,
        R.string.first_letter_to_the_thessalonians,
        R.string.second_letter_to_the_thessalonians,
        R.string.first_letter_to_timothy,
        R.string.second_letter_to_timothy,
        R.string.letter_to_titus,
        R.string.letter_to_philemon,
        R.string.letter_to_the_hebrews,
        R.string.letter_of_james,
        R.string.first_letter_of_peter,
        R.string.second_letter_of_peter,
        R.string.first_letter_of_john,
        R.string.second_letter_of_john,
        R.string.third_letter_of_john,
        R.string.letter_of_jude,
        R.string.revelation_to_john
    )


    val allMannaTexts by mannaViewModel.allMannaTexts.observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }

    var id = mannaViewModel.getSavedPageBookID()

    if(expanded){
        BackHandler(enabled = true) {
            expanded = false
        }
    }

    LaunchedEffect(key1 = "loadFavoriteTexts") {
        mannaViewModel.allMannaTexts
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Wybieranie",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    top = 6.dp,
                    end = 12.dp,
                    start = 12.dp,
                    bottom = 6.dp
                )
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp, start = 8.dp, end = 8.dp)
        )  {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                ) {

                    Row {
                        Text(text = stringResource(bookNamesList[id]),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                            textAlign = TextAlign.Center)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "arrow drop down", modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .graphicsLayer {
                                scaleX = 1.2f
                                scaleY = 1.2f
                            }, tint = MaterialTheme.colorScheme.onBackground)
                    }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            properties = PopupProperties(focusable = false),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
                        ) {
                            bookNamesList.forEachIndexed { index, s ->
                                DropdownMenuItem(onClick = {
                                    id = index
                                    mannaViewModel.savePageBookID(id)
                                    expanded = false
                                }) {
                                    Text(text = stringResource(s), fontSize = 16.sp)
                                }

                                if (index < bookNamesList.size-1){
                                    Spacer(modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.outlineVariant))
                                }
                            }
                        }
                }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(allMannaTexts.filter { it.bookID == id }) {
                    MannaRowItem(item = it, mannaViewModel = mannaViewModel, navController = navController, showButton = false)
                }
            }
        }
    }
}