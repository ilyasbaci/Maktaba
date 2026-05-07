package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor() : BookRepository {

    private val _booksList = mutableListOf(
        Book(isbn = "9780132350884", title = "Clean Code", nbPages = 431),
        Book(isbn = "9780135957059", title = "The Pragmatic Programmer", nbPages = 352),
        Book(isbn = "9780201633610", title = "Design Patterns", nbPages = 395),
        Book(isbn = "9780201485677", title = "Refactoring", nbPages = 448),
        Book(isbn = "9780596007126", title = "Head First Design Patterns", nbPages = 694)
    )

    private val booksFlow = MutableSharedFlow<List<Book>>(replay = 1).apply {
        tryEmit(_booksList.toList())
    }

    override fun getAllBooks(): Flow<List<Book>> = flow {
        delay(2000)
        emitAll(booksFlow)
    }

    override fun getBookByIsbn(isbn: String): Book? =
        _booksList.find { it.isbn == isbn }

    override fun addBook(book: Book) {
        _booksList.add(book)
        booksFlow.tryEmit(_booksList.toList())
    }
}