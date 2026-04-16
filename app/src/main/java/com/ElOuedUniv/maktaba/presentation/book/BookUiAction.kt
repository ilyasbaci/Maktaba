package com.ElOuedUniv.maktaba.presentation.book
import com.ElOuedUniv.maktaba.data.model.Book
sealed interface BookUiAction {

    object OnAddBookClick : BookUiAction

    object OnDismissAddBook : BookUiAction

    data class OnAddBookConfirm(val book: Book) : BookUiAction
}