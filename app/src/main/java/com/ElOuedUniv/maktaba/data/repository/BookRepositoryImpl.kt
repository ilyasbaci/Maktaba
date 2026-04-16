package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class BookRepositoryImpl : BookRepository {

    private val books = mutableListOf(
        Book(isbn = "11111", title = "Clean Code", nbPages = 10),
        Book(isbn = "", title = "The Pragmatic Programmer", nbPages = 0),
        Book(isbn = "", title = "Design Patterns", nbPages = 0),
        Book(isbn = "", title = "Refactoring", nbPages = 0),
        Book(isbn = "", title = "Head First Design Patterns", nbPages = 0)
    )

    private val _booksFlow = MutableSharedFlow<List<Book>>(replay = 1)
    val booksFlow = _booksFlow.asSharedFlow()

    init {
        _booksFlow.tryEmit(books.toList())
    }

    override fun getBooks(): Flow<List<Book>> {
        return booksFlow
    }

    override suspend fun addBook(book: Book) {
        books.add(book)

        _booksFlow.emit(books.toList())
    }
}