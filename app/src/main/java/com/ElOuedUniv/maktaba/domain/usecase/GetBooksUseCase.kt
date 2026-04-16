package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase(
    private val bookRepository: BookRepository
) {
    operator fun invoke(): Flow<List<Book>> {
        return bookRepository.getBooks()
    }
}