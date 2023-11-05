package com.ketchup.dailymanna.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.viewmodel.ViewModel


@Composable
fun MannaRowItem(item: MannaTextEntity, viewModel: ViewModel, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth().clickable {

            viewModel.savePageIndex(item.id)
            navController.navigate("MainScreen")

        }) {

        Text(text = item.title, fontSize = 16.sp, modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
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
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(MaterialTheme.colorScheme.outlineVariant))
}