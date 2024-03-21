package com.ketchup.dailymanna.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ketchup.dailymanna.R
import com.ketchup.dailymanna.viewmodel.MannaViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(mannaViewModel: MannaViewModel, navController: NavController) {

    val allMannaTexts by mannaViewModel.allFavoriteMannaTexts.observeAsState(initial = emptyList())

    LaunchedEffect(key1 = "loadFavoriteTexts") {
        mannaViewModel.getAllFavorites()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = stringResource(R.string.favorites),
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
                .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
        ) {
            if (allMannaTexts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_favorites),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn{
                    items(allMannaTexts) { item ->
                        MannaRowItem(item, mannaViewModel, navController, true)
                    }
                }
            }
        }
    }
}
