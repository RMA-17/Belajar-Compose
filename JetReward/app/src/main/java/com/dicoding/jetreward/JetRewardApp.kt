package com.dicoding.jetreward

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.jetreward.ui.navigation.NavigationItem
import com.dicoding.jetreward.ui.navigation.Screen
import com.dicoding.jetreward.ui.screen.cart.CartScreen
import com.dicoding.jetreward.ui.screen.detail.DetailScreen
import com.dicoding.jetreward.ui.screen.home.HomeScreen
import com.dicoding.jetreward.ui.screen.profile.ProfileScreen
import com.dicoding.jetreward.ui.theme.JetRewardTheme

@Composable
fun JetRewardApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailReward.route)
                BottomNav(navController)
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            /**
             * composable merupakan extension function dari NavGraphBuilder yang berfungsi untuk menambahkan composable screen ke dalam NavGraph.
             * Di dalamnya, Anda bisa menambahkan Composable dari halaman yang ingin ditampilkan.
             * Selain itu, terdapat parameter route yang berisi string unik untuk menandai setiap destination.
             */
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { rewardId ->
                        navController.navigate(Screen.DetailReward.createRoute(rewardId))
                    }
                )
            }
            composable(Screen.Cart.route) {
                val context = LocalContext.current
                CartScreen(
                    orderButtonClicked = { message ->
                        shareOrder(context, message)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(
                /**
                 * Untuk menambahkan argument pada route composable, Anda perlu menggunakan tanda / (slash) diikuti dengan data yang dibungkus kurung kurawal.
                 * Kemudian untuk melakukan navigasi, cukup isikan data sesuai format yang ada pada route.
                 */
                route = Screen.DetailReward.route,
                arguments = listOf(navArgument("rewardId") {type = NavType.LongType})
            ) {
                val id = it.arguments?.getLong("rewardId")?: -1L
                DetailScreen(
                    rewardId = id,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToCart = {
                        navController.popBackStack()
                        navController.navigate(Screen.Cart.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNav(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackstackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackstackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(id = R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(id = R.string.menu_cart),
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Cart
            ),
            NavigationItem(
                title = stringResource(id = R.string.menu_profile),
                icon = Icons.Default.Person,
                screen = Screen.Profile
            )
        )
        BottomNavigation {
            navigationItems.map { navItem ->
                BottomNavigationItem(
                    icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.title) },
                    label = { Text(navItem.title) },
                    selected = currentRoute == navItem.screen.route,
                    onClick = {
                        // navController.navigate: digunakan untuk eksekusi navigasi ke route sesuai dengan item yang dipilih.
                        navController.navigate(navItem.screen.route) {
                            //popUpTo: digunakan untuk kembali ke halaman awal supaya tidak membuka halaman baru terus menerus
                            popUpTo(navController.graph.findStartDestination().id) {
                                //saveState dan restoreState: mengembalikan state ketika item dipilih lagi.
                                saveState = true
                            }
                            restoreState = true
                            //launchSingleTop: digunakan supaya tidak ada halaman yang dobel ketika memilih ulang item yang sama.
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

fun shareOrder(context: Context, summary: String) {
    //ACTION_SEND, artinya hanya aplikasi yang bisa menerima jenis ACTION_SEND sajalah yang bisa menangani tugas ini, seperti aplikasi SMS dan email.
    val intent = Intent(ACTION_SEND).apply {
        type = "text/plain"
        //EXTRA_SUBJECT digunakan untuk judul dan EXTRA_TEXT digunakan untuk isi pesan.
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.dicoding_reward))
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        //createChooser merupakan jenis eksekusi Intent yang menampilkan beberapa pilihan aplikasi yang bisa membuka data yang bagikan.
        Intent.createChooser(
            intent,
            context.getString(R.string.dicoding_reward)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun JetHeroesAppPreview() {
    JetRewardTheme {
        JetRewardApp()
    }
}
