package com.rmaproject.mynavdrawer

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MyNavDrawerApp() {
    /**
     * scaffoldState: merupakan state bawaan dari Scaffold untuk mengatur elemen di dalamnya dengan animasi default. Di dalamnya terdapat dua state, yakni drawerState untuk mengatur Navigation Drawer dan snackbarHostState untuk mengatur Snackbar.
     * rememberCoroutineScope: digunakan untuk memanggil Coroutine di dalam Composable. Karena fungsi open merupakan suspend function, Anda perlu menggunakan coroutine scope untuk memanggilnya.
     */
    val appState = rememberMyNavDrawerState()
    Scaffold(
        scaffoldState = appState.scaffoldState,
        topBar = {
            MyTopBar(
                onMenuClick = {
                    appState.onMenuClick()
                }
            )
        },
        drawerContent = {
            MyDrawerContent(
                onItemSelected = appState::onItemSelected,
                onBackPressed = appState::onBackPressed
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Composable
fun MyTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.menu)
                )
            }
        },
        title = {
            Text(stringResource(R.string.app_name))
        }
    )
}

@Composable
fun MyDrawerContent(
    onItemSelected: (title: String) -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    val items = listOf(
        MenuItem(
            title = "Home",
            icon = Icons.Default.Home
        ),
        MenuItem(
            title = "Favorite",
            icon = Icons.Default.Favorite
        ),
        MenuItem(
            title = "Profile",
            icon = Icons.Default.Person
        ),
    )

    Column(modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(128.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        for (item in items) {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .clickable { onItemSelected(item.title) }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Text(text = item.title, style = MaterialTheme.typography.subtitle2)
                }
            }
        }
        Divider()
    }

    BackPressHandler {
        onBackPressed()
    }
}

/**
 * Kodingan dibawah untuk mengatasi bug
 * Tombol back yang langsung menutup aplikasi (Seharusnyua menutup drawer)
 */
@Composable
fun BackPressHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit,
) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    val backCallBack =  remember {
        object: OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }

        }
    }

    SideEffect {
        backCallBack.isEnabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(lifecycle, backDispatcher) {
        backDispatcher.addCallback(lifecycle, backCallBack)
        onDispose {
            backCallBack.remove()
        }
    }


}
