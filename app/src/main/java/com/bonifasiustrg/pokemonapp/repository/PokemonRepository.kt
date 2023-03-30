package com.bonifasiustrg.pokemonapp.repository

import com.bonifasiustrg.pokemonapp.data.remote.PokemonAPI
import com.bonifasiustrg.pokemonapp.data.remote.response.Pokemon
import com.bonifasiustrg.pokemonapp.data.remote.response.PokemonList
import com.bonifasiustrg.pokemonapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

// this repository will inject to View Model Class

@ActivityScoped // part of hilt digger, it will live as long as the activity does
class PokemonRepository @Inject constructor(
    private val api: PokemonAPI //get access to pokemon api
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)

            // Try network request so can know if there is a error, such as no internet, etc
            // can implement util.Resource messege to tell the status
        } catch (e: java.lang.Exception) {
            return Resource.Error("An unknown error occured")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)

            // Try network request so can know if there is a error, such as no internet, etc
            // can implement util.Resource messege to tell the status
        } catch (e: java.lang.Exception) {
            return Resource.Error("An unknown error occured")
        }
        return Resource.Success(response)
    }


}