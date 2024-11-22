package com.example.notetest.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notetest.R
import com.example.notetest.data.model.Note
import com.example.notetest.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController, viewModel: SharedViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ){
            TopRow()

            Spacer(modifier = Modifier.height(16.dp))

            Block(viewModel, navController)
        }

        AddButton(navController, viewModel)
        }
    }
@Composable
fun TopRow() {
    Row(
        modifier = Modifier
            .padding(50.dp)
            .background(Color.Black),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "Notes",
            fontSize = 43.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.nunitos)),
            modifier = Modifier.weight(1f),
            color = Color.White
        )
        IconButtonRow()
    }
}

@Composable
fun IconButtonRow() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(6.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {

            },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3B3B3B))
                .size(50.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        IconButton(
            onClick = {

            },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3B3B3B))
                .size(50.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.info_outline),
                contentDescription = "Info",
                tint = Color.White
            )
        }
    }
}


@Composable
fun Block(
    viewModel: SharedViewModel,
    navController: NavHostController
) {
    val notes = viewModel.notes.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (notes.value.isEmpty()) {
            item {

                EmptyNotesPlaceholder()
            }
        }
        else {
                    items(notes.value.size) { index ->
                        val note = notes.value[index]
                        NoteItem(
                            note = note,
                            onEdit = {
                                navController.navigate("editor/${note.id}")
                            },
                            onDelete = {
                                coroutineScope.launch {
                                    viewModel.deleteNoteById(note.id)
                                }
                            }
                        )

                    }
            }

    }
}

@Composable
fun AddButton(navController: NavHostController, viewModel: SharedViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(52.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        IconButton(
            onClick = {
                navController.navigate("editor/{noteId}")
            },
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFF3B3B3B))
                .size(60.dp)
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
fun EmptyNotesPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.rafiki),
            contentDescription = "No notes",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Create your First Note!",
            fontFamily = FontFamily(Font(R.font.nunitos)),
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // State to track if the note is in "delete mode"
    var isDeleteMode by remember { mutableStateOf(false) }

    // Parse the color string from the `Note` model
    val noteColor = if (isDeleteMode) Color.Red else try {
        Color(android.graphics.Color.parseColor(note.color)) // Convert HEX string to Color
    } catch (e: Exception) {
        Color.DarkGray // Fallback color
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                        onEdit() // Navigate to the editor screen for editing
                },
                onLongClick = {
                    isDeleteMode = true // Enable "delete mode"
                }
            ),
        colors = CardDefaults.cardColors(containerColor = noteColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isDeleteMode) {
                // Show delete icon in the center when in delete mode
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White
                    )
                }
            } else {
                // Normal mode: Display note content
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = note.title,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = note.content,
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}






