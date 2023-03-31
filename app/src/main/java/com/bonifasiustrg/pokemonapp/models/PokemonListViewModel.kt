package com.bonifasiustrg.pokemonapp.models

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.bonifasiustrg.pokemonapp.repository.PokemonRepository
import com.bonifasiustrg.pokemonapp.util.Constants.PAGE_SIZE
import com.bonifasiustrg.pokemonapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    private var currentPage = 0 //first page
    var pokemonList = mutableStateOf<List<PokemonListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }
    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) { // to ensure loading the result from api id successfull or not
                //log
                is Resource.Success -> {
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count!!
                    val pokemonEntries = result.data.results!!.mapIndexed { index, entry ->
                        val number = if (entry!!.url!!.endsWith("/")) {
                            entry.url!!.dropLast(1).takeLastWhile {
                                it.isDigit()
                            }
                        } else {
                            entry.url!!.takeLastWhile {
                                it.isDigit()
                            }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        Log.e("TES", "load pagination")
                        PokemonListEntry(entry.name!!.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, url, number.toInt())
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonEntries
                }
                is Resource.Error -> {
                    loadError.value = result.messege!!
                    isLoading.value = false
                }
                else -> {

                }
            }
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap  //convert drawable to bitmap bcs pallete api only support bitmpa
            .copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate {palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->  // get the dominant color
                onFinish(Color(colorValue))
            }
        }
    }

}