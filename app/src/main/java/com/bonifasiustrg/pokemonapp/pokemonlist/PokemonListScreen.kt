package com.bonifasiustrg.pokemonapp.pokemonlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.bonifasiustrg.pokemonapp.R
import com.bonifasiustrg.pokemonapp.models.PokemonListEntry
import com.bonifasiustrg.pokemonapp.models.PokemonListViewModel
import com.bonifasiustrg.pokemonapp.ui.theme.RobotoCondensed

//import com.google.accompanist.coil.CoilImage

@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        // implement the custom Theme.kt
        color = MaterialTheme.colors.background,  // compose class that can switch color, whether darkmode and lightmode correspndengly,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = CenterHorizontally)
            )

            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

            }

            Spacer(modifier = Modifier
                .height(16.dp)
                .background(Color.Blue))
            PokemonList(navController =navController)


            Text("TES TSE TES TESSET SETSTES", style = MaterialTheme.typography.h5)
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit ={} //lamda function that will trigger search other compose function when on typing
) {
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, shape = CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused

                }
        )
        if (isHintDisplayed) {
            Text(text = hint, color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }

    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList}
    val endReached by remember { viewModel.endReached}
    val loadError by remember { viewModel.loadError}
    val isLoading by remember { viewModel.isLoading}

    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.background(Color.Red)) {
        val itemCount = if(pokemonList.size % 2 == 0) {
            pokemonList.size / 2 // to knoe how many row needed
        } else {
            pokemonList.size/2 + 1
        }
        Log.e("TES", "lazy column")
//        Text(text ="sdf")
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached) { // THis will tell when we need pagination
                viewModel.loadPokemonPaginated()
            }
            PokemonRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
        Log.e("TES", "lazy column end")
    }
    Text("TES PLPLPLPPLPDLFJDJF")
}


@Composable
fun PokemonEntry(
    entry: PokemonListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
//    viewModel: PokemonListViewModel = hiltViewModel<PokemonListViewModel>()
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Log.e("TES", "pokemon entry")
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }


    Box(
        contentAlignment = Center,
//        modifier = Modifier  // make user use modifier from parameter, if use this, the card only showed 1 column
        modifier = modifier  // make user use modifier from parameter, if use this, the card only showed 2 column, that had add conf such as the weight with context in row. This will called in PokemonRow Func
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(  //gradient from the dominant color
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {  // navigate to pokemon detail screen
                navController.navigate(
                    // bcs we now in navigation compose, dont need tp pass id, instead pass route
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"  // that also called in main activity
                )
            }
    ) {

        Column {
            /*  DEPRECATED COIL IMAGE
            // to load the pokemon image            // LocalContext to get the context in compose
            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)  // create image request
                    .data(entry.imageUrl) // load the image from url
                    .target {
                        viewModel.calculateDominantColor(it) {color ->  // call func that will give the dominant color in the image
                            dominantColor = color // color that have assigned will use for gradient
                        }
                    }.build(),
                contentDescription = entry.pokemonName,
                fadeIn = true,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            ) {
                // using for specify the composoable while tha image is loading
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
                Text(text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())



            }
            */
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.pokemonName,
                loading = {
                    CircularProgressIndicator(
//                        color = MaterialTheme.colorScheme.primary, modifier = Modifier.scale(0.5F)
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                },
                success = { success ->
                    viewModel.calculateDominantColor(success.result.drawable){
                        dominantColor = it
                    }
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )

            Text(text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }

    }
}

@Composable
fun PokemonRow(
    rowIndex: Int,
    entries: List<PokemonListEntry>,
    navController: NavController
) {
    Log.e("TES", "pokemon row")

    Column() {
        Row() {             // 2 pokemon per row
            PokemonEntry(entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))

            if (entries.size >= rowIndex) { // check atleast ther are 2 entries pokemon
                PokemonEntry(entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(Modifier.weight(1f))

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}