package com.ElOuedUniv.maktaba.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ElOuedUniv.maktaba.data.model.Book
import com.ElOuedUniv.maktaba.presentation.book.BookUiAction
import com.ElOuedUniv.maktaba.presentation.book.BookUiEvent
import com.ElOuedUniv.maktaba.presentation.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListView(
    viewModel: BookViewModel,
    onCategoriesClick: () -> Unit = {}
) {

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is BookUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maktaba - My Library") },
                actions = {
                    IconButton(onClick = onCategoriesClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Categories"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAction(BookUiAction.OnAddBookClick)
                }
            ) {
                Text("+")
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {

                if (state.books.isEmpty()) {
                    EmptyBooksMessage(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    BookList(
                        books = state.books,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    if (state.isAddingBook) {
        AddBookDialog(
            onDismiss = {
                viewModel.onAction(BookUiAction.OnDismissAddBook)
            },
            onConfirm = { book ->
                viewModel.onAction(
                    BookUiAction.OnAddBookConfirm(book)
                )
            }
        )
    }
}

@Composable
fun BookList(
    books: List<Book>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { book ->
            BookItem(book)
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text("ISBN:")
                    Text(if (book.isbn.isEmpty()) "Not set" else book.isbn)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Pages:")
                    Text(if (book.nbPages == 0) "Not set" else "${book.nbPages}")
                }
            }
        }
    }
}

@Composable
fun EmptyBooksMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📚", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No books in your library")
    }
}

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (Book) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var pages by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val book = Book(
                        title = title,
                        isbn = isbn,
                        nbPages = pages.toIntOrNull() ?: 0
                    )
                    onConfirm(book)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Add Book")
        },
        text = {
            Column {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pages,
                    onValueChange = { pages = it },
                    label = { Text("Pages") }
                )
            }
        }
    )
}