package com.ElOuedUniv.maktaba.presentation.book

import com.ElOuedUniv.maktaba.data.model.Book

data class BookUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isAddingBook: Boolean = false,
    val errorMessage: String? = null
)