package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.repository.BookRepository

class AddBookUseCase(
    private val repository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        repository.addBook(book)
    }
}