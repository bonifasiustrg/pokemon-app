package com.bonifasiustrg.pokemonapp.models

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.bonifasiustrg.pokemonapp.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

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