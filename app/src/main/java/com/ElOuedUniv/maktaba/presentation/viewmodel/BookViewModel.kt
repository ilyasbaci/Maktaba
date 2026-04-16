package com.ElOuedUniv.maktaba.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.usecase.GetBooksUseCase
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import com.ElOuedUniv.maktaba.presentation.book.BookUiState
import com.ElOuedUniv.maktaba.presentation.book.BookUiAction
import com.ElOuedUniv.maktaba.presentation.book.BookUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BookUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadBooks()
    }

    private fun loadBooks() {

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                getBooksUseCase().collect { books ->

                    _uiState.update {
                        it.copy(
                            books = books,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "error"
                    )
                }
            }
        }
    }

    fun refreshBooks() {
        loadBooks()
    }

    fun onAction(action: BookUiAction) {
        when (action) {

            BookUiAction.OnAddBookClick -> {
                _uiState.update { it.copy(isAddingBook = true) }
            }

            BookUiAction.OnDismissAddBook -> {
                _uiState.update { it.copy(isAddingBook = false) }
            }

            is BookUiAction.OnAddBookConfirm -> {
                viewModelScope.launch {

                    addBookUseCase(action.book)

                    _uiState.update { it.copy(isAddingBook = false) }

                    _uiEvent.emit(
                        BookUiEvent.ShowToast("Book added successfully")
                    )
                }
            }
        }
    }
}