package com.ElOuedUniv.maktaba.domain.repository

import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBooks(): Flow<List<Book>>

    suspend fun addBook(book: Book)
}