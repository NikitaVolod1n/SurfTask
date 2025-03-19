package com.example.surftask.Retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class BooksResponse(
    @SerializedName("items") val items: List<Item>?
) {
    data class Item(
        @SerializedName("id") val id: String,
        @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
    )

    data class VolumeInfo(
        @SerializedName("title") val title: String?,
        @SerializedName("authors") val authors: List<String>?,
        @SerializedName("imageLinks") val imageLinks: ImageLinks?,
        var isFavorite: Boolean
    )

    data class ImageLinks(
        @SerializedName("thumbnail") val thumbnail: String?
    )
}


interface BooksAPI {
    @GET("volumes")
    suspend fun getBookByName(@Query("q") query: String): BooksResponse
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://www.googleapis.com/books/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

var restBooksAPI = retrofit.create(BooksAPI::class.java)