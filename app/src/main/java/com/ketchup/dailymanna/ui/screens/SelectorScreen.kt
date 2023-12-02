package com.ketchup.dailymanna.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.ketchup.dailymanna.viewmodel.ViewModel

@Composable
fun SelectorScreen(viewModel: ViewModel, navController: NavController){

    val bookNamesList = listOf(
        "Ewangelia Mateusza",
        "Ewangelia Marka",
        "Ewangelia Łukasza",
        "Ewangelia Jana",
        "Dzieje Apostolskie",
        "List do Rzymian",
        "Pierwszy list do Koryntian",
        "Drugi list do Koryntian",
        "List do Galacjan",
        "List do Efezjan",
        "List do Filipian",
        "List do Kolosan",
        "Pierwszy list do Tesaloniczan",
        "Drugi list do Tesaloniczan",
        "Pierwszy list do Tymoteusza",
        "Drugi list do Tymoteusza",
        "List do Tytusa",
        "List do Filemona",
        "List do Hebrajczyków",
        "List Jakuba",
        "Pierwszy list Piotra",
        "Drugi list Piotra",
        "Pierwszy list Jana",
        "Drugi list Jana",
        "Trzeci list Jana",
        "List Judy",
        "Objawienie Janowi"
    )

    val allMannaTexts by viewModel.allMannaTexts.observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }

    var id = viewModel.getSavedPageBookID()

    if(expanded){
        BackHandler(enabled = true) {
            expanded = false
        }
    }

    LaunchedEffect(key1 = "loadFavoriteTexts") {
        viewModel.allMannaTexts
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

                    Text(text = bookNamesList[id],
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                        textAlign = TextAlign.Center)

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
                                    viewModel.savePageBookID(id)
                                    expanded = false
                                }) {
                                    Text(text = s, fontSize = 16.sp)
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
                    MannaRowItem(item = it, viewModel = viewModel, navController = navController, showButton = false)
                }
            }
        }
    }
}