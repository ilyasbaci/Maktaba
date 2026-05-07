package com.ElOuedUniv.maktaba.presentation.book.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailView(
    onBackClick: () -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.book?.title ?: "Book Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center))
                uiState.book != null -> {
                    val book = uiState.book!!
                    Column(modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())) {

                        Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                            if (book.imageUri != null) {
                                AsyncImage(
                                    model = book.imageUri,
                                    contentDescription = book.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(modifier = Modifier.fillMaxSize().background(
                                    Brush.verticalGradient(listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.secondaryContainer
                                    ))
                                )) {
                                    Text("📖", style = MaterialTheme.typography.displayLarge,
                                        modifier = Modifier.align(Alignment.Center))
                                }
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(100.dp)
                                .align(Alignment.BottomCenter)
                                .background(Brush.verticalGradient(listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background
                                ))))
                        }

                        Column(modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp).padding(bottom = 24.dp)) {

                            Text(book.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(20.dp))

                            Card(modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Reading Progress",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LinearProgressIndicator(
                                        progress = { if (book.nbPages > 0) 0.4f else 0f },
                                        modifier = Modifier.fillMaxWidth().height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(if (book.nbPages > 0) "In Progress" else "Not Started",
                                        style = MaterialTheme.typography.labelMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                MetadataChip("Pages", "${book.nbPages}", Modifier.weight(1f))
                                MetadataChip("ISBN", book.isbn, Modifier.weight(1f))
                            }
                        }
                    }
                }
                uiState.errorMessage != null ->
                    Text(uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp))
                else -> Text("Book not found",
                    modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun MetadataChip(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold)
        }
    }
}