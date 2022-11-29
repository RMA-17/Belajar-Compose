package com.rmaproject.jetheroes

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rmaproject.jetheroes.data.HeroRepository
import com.rmaproject.jetheroes.data.JetHeroesViewModel
import com.rmaproject.jetheroes.data.ViewModelFactory
import com.rmaproject.jetheroes.ui.theme.JetHeroesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JetHeroesApp(
    modifier: Modifier = Modifier,
    viewModel: JetHeroesViewModel = viewModel(factory = ViewModelFactory(HeroRepository()))
) {
    // collectAsState() untuk mengubah StateFlow menjadi State.
    val groupedHeroes by viewModel.groupedHeroes.collectAsState()
    Box(
        modifier = modifier
    ) {
        val scope = rememberCoroutineScope()
        // rememberCoroutineScope digunakan untuk menjalankan suspend function di dalam Composable function.
        val listState = rememberLazyListState()
        // rememberLazyListState merupakan state dari Lazy List yang digunakan untuk mengontrol dan membaca posisi item. (Controller)
        val showButton: Boolean by remember {
            derivedStateOf { listState.firstVisibleItemIndex > 0 }
            //firstVisibleItemIndex digunakan untuk mengetahui index item pertama yang terlihat di layar.
        }

        /**
         * Variabel showButton akan menyimpan state menggunakan derivedStateOf ketika index item
         * pertama yang dilihat lebih dari 0 alias item pertama sudah tidak terlihat. Dengan derivedStateOf,
         * nilai showButton hanya akan diperbarui ketika state di dalamnya berubah,
         * sehingga proses composition menjadi lebih efektif.
         */


        LazyColumn(
            state = listState,
            // Untuk menjorokkan konten kedalam
            contentPadding = PaddingValues(bottom = 30.dp)
        ) {
            groupedHeroes.forEach { (initial, heroes) ->
                stickyHeader {
                    CharacterHeader(char = initial)
                }
                items(heroes, key = {it.id}) { hero ->
                    HeroListItem(name = hero.name, photoUrl = hero.photoUrl)
                }
            }
        }

        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
fun HeroListItem(
    name: String,
    photoUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { }
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun CharacterHeader(
    char: Char,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Text(
            char.toString(),
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
fun JetHeroesAppPreview() {
    JetHeroesTheme {
        Surface {
            JetHeroesApp()
        }
    }
}