package com.bonifasiustrg.pokemonapp.di

import com.bonifasiustrg.pokemonapp.data.remote.PokemonAPI
import com.bonifasiustrg.pokemonapp.repository.PokemonRepository
import com.bonifasiustrg.pokemonapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //the depedencies in module will live as long as application still does
object AppModule {

    //Provide api in the module after setup API network and request query for it
    //this module will be injected to other class later
    @Singleton @Provides
    fun providePokemonRepository ( //provide (menyediakan) repository pokemonapi
        api: PokemonAPI
    ): PokemonRepository {
        return PokemonRepository(api)
    }

    //provide the actual api. a retrfit instance
    @Singleton @Provides
    fun providePokemonApi(): PokemonAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL) //get from util.COnstant
            .build()
            .create(PokemonAPI::class.java)
    }

}