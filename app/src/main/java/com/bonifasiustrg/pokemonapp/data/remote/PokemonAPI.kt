package com.bonifasiustrg.pokemonapp.data.remote

import com.bonifasiustrg.pokemonapp.data.remote.response.Pokemon
import com.bonifasiustrg.pokemonapp.data.remote.response.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    // request list pokemon
    @GET("pokemon")  // to tell retrofit to which route this request will go and what kina request is this
    suspend fun getPokemonList(
        @Query("limit") limit: Int,  // load size every page
        @Query("offset") offset: Int,
    ): PokemonList

    // request pokemon detail
    @GET("pokemon/{pokemonName}")
    suspend fun getPokemonInfo(
        @Path("pokemonName") pokemonName: String

    ): Pokemon
}