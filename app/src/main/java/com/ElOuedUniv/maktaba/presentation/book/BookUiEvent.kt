package com.ElOuedUniv.maktaba.presentation.book

sealed interface BookUiEvent {

    data class ShowToast(val message: String) : BookUiEvent
}