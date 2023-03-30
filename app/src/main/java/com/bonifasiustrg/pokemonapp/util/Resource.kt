package com.bonifasiustrg.pokemonapp.util

//Generics class
sealed class Resource<T>(val data: T? = null, val messege: String? = null) {
    class Success<T>(data: T): Resource<T>(data)  //not nullable bcs we should be had the data if succeed
    class Error<T>(messege: String? ,data: T? = null): Resource<T>(data, messege)  //nullable bcs maybe theres no data that causing error
    class Loading<T>(data: T? = null): Resource<T>(data)

}
