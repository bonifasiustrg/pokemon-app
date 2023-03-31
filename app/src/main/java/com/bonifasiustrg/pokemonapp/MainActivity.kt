package com.bonifasiustrg.pokemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bonifasiustrg.pokemonapp.pokemonlist.PokemonListScreen
import com.bonifasiustrg.pokemonapp.ui.theme.PokemonAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint // anotation for digger hilt to tell that in this activity, we want to inject something
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Text(text = "testesrggfhdgdfsdgggh")
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "pokemon_list_screen"
                    ) {
                        composable("pokemon_list_screen") {
                            PokemonListScreen(navController = navController)
                        }

                        // if pokemon clicked, go to detail
                        composable(
                            "pokemon_detail_screen/{dominantColor}/{pokemonName}",  // also need to pass some argument, need it later
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("pokemonName") {
                                type = NavType.StringType
                            }
                        )) {
                            val dominantColor = remember {
                                val color = it.arguments?.getInt("dominantColor")
                                color?.let { Color(it) ?: Color.White}
                            }
                            val pokemonName = remember {
                                it.arguments?.getString("pokemonName")
                            }
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokemonAppTheme {

    }
}