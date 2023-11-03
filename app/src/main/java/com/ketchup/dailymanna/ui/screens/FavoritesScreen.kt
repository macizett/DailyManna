package com.ketchup.dailymanna.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.viewmodel.ViewModel

@Composable
fun FavoritesScreen(viewModel: ViewModel){
    LaunchedEffect(key1 = "loadTexts") {
        viewModel.getAllFavorites()
    }

    val allMannaTexts by viewModel.allMannaTexts.observeAsState(initial = emptyList())

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
    ){
        if(allMannaTexts.isEmpty()){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Brak ulubionych - śmiało, dodaj coś!", fontSize = 16.sp, color = Color.LightGray, modifier = Modifier.align(
                    Alignment.Center))
            }
        }
        else{
            LazyColumn {
                items(allMannaTexts) { item ->
                    MannaItem(item, viewModel)
                }
            }
        }
    }
}

@Composable
fun MannaItem(item: MannaTextEntity, viewModel: ViewModel){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(42.dp)) {
        
        Text(text = item.title, fontSize = 16.sp, modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(start = 8.dp, end = 8.dp)
            .weight(1f))

        val checkedState = remember { mutableStateOf(item.isFavorite) }

        IconToggleButton(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                viewModel.setFavorite(item, checkedState.value)
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
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(Color.Gray))
}
