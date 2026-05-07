package com.ElOuedUniv.maktaba.presentation.book.add

import androidx.lifecycle.ViewModel
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: AddBookUiAction) {
        when (action) {
            is AddBookUiAction.OnTitleChange ->
            { _uiState.update { it.copy(title = action.title) }; validateInputs() }
            is AddBookUiAction.OnIsbnChange ->
            { _uiState.update { it.copy(isbn = action.isbn) }; validateInputs() }
            is AddBookUiAction.OnPagesChange ->
            { _uiState.update { it.copy(nbPages = action.pages) }; validateInputs() }
            is AddBookUiAction.OnImageUriChange ->
                _uiState.update { it.copy(imageUri = action.uri) }
            AddBookUiAction.OnAddClick ->
                if (_uiState.value.isFormValid) addBook()
        }
    }

    private fun validateInputs() {
        val s = _uiState.value
        val titleError = if (s.title.isBlank()) "Title cannot be empty" else null
        val isbnError = when {
            s.isbn.isBlank()             -> "ISBN cannot be empty"
            !s.isbn.all { it.isDigit() } -> "ISBN must contain only digits"
            s.isbn.length != 13          -> "ISBN must be exactly 13 digits"
            else                         -> null
        }
        val pagesError = when {
            s.nbPages.isBlank()                 -> "Pages cannot be empty"
            s.nbPages.toIntOrNull() == null      -> "Pages must be a number"
            (s.nbPages.toIntOrNull() ?: 0) <= 0 -> "Pages must be positive"
            else                                 -> null
        }
        _uiState.update {
            it.copy(
                titleError = titleError,
                isbnError = isbnError,
                pagesError = pagesError,
                isFormValid = titleError == null && isbnError == null && pagesError == null
            )
        }
    }

    private fun addBook() {
        val s = _uiState.value
        addBookUseCase(Book(
            isbn = s.isbn,
            title = s.title,
            nbPages = s.nbPages.toIntOrNull() ?: 0,
            imageUri = s.imageUri
        ))
        _uiState.update { it.copy(isSuccess = true) }
    }
}