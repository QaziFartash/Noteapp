package com.example.notetest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.notetest.R
import com.example.notetest.data.model.Note
import com.example.notetest.ui.viewmodel.SharedViewModel
import kotlin.random.Random



@Composable
fun EditorScreen(
    navController: NavHostController,
    viewModel: SharedViewModel,
    noteId: Int? // Updated to Int? to match the SharedViewModel and Note
) {
    // Retrieve the existing note (if editing)
    val existingNote = viewModel.getNoteById(noteId ?: 0)
    val isEditMode = existingNote != null

    // Manage the state for title, content, and color
    var title by remember { mutableStateOf(existingNote?.title ?: "") }
    var content by remember { mutableStateOf(existingNote?.content ?: "") }
//    var selectedColor by remember { mutableStateOf(existingNote?.color ?: Color.White) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Row of icons at the top of the screen
        IconsRow(
            navController = navController,
            isEditMode = isEditMode,
            onSave ={ randomColor ->
                viewModel.saveOrUpdateNote(
                    id = existingNote?.id ?: 0,
                    title = title,
                    content = content,
                    color = randomColor // Use the passed color
                )
                navController.navigate("home")
            },

            onDelete = {
                if (isEditMode) {
                    viewModel.deleteNoteById(noteId ?: 0)
                }
                navController.navigate("home")
            },
            onBackPress = { showDialog = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input area for note title and content
        ContentArea(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it }
        )
    }

    // Dialog for discarding changes
    if (showDialog) {
        DiscardChangesDialog(
            onDismiss = { showDialog = false },
            onDiscard = { navController.navigate("home") },
            onSave = {
//                val randomColor = Color(Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt()))
//                val hexColor = "#${Integer.toHexString(randomColor.toArgb()).uppercase()}"
                val randomColor = String.format("#%06X", (0xFFFFFF and Random.nextInt()))
                val note = Note(
                    id = existingNote?.id ?: 0,
                    title = title,
                    content = content,
                    color = randomColor
                )
                viewModel.saveOrUpdateNote(
                    id = existingNote?.id ?: 0, // Use 0 for new notes
                    title = title,
                    content = content,
                    color = randomColor
                )
                navController.navigate("home")
            }
        )
    }
}




@Composable
fun IconsRow(
    navController: NavHostController,
    isEditMode: Boolean,
    onSave: (String) -> Unit, // Accepts a String parameter
    onDelete: () -> Unit,
    onBackPress: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(45.dp)
            .background(Color.Black)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onBackPress() },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3B3B3B))
                .size(48.dp)
        ) {
            Icon(
                modifier = Modifier.size(23.dp),
                painter = painterResource(id = R.drawable.vector), // Replace with your back icon
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onDelete() },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3B3B3B))
                .size(50.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.visibility), // Replace with delete icon
                contentDescription = "Visible",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(
            onClick = {
                val randomColor = String.format("#%06X", (0xFFFFFF and Random.nextInt()))
                onSave(randomColor) // Pass the random color
            },
            modifier = Modifier
                .padding(end = 2.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3B3B3B))
                .size(50.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.save), // Replace with save icon
                contentDescription = "Save",
                tint = Color.White
            )
        }
    }
}


@Composable
fun ContentArea(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(text = "Title",
                color = Color.Gray,
                style = TextStyle(
                    fontSize = 48.sp, // Increase this value for larger text
                    fontFamily = FontFamily(Font(R.font.nunitos)), // Optional: match font with your app theme
                    fontWeight = FontWeight.Normal)) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Thin,
                fontFamily = FontFamily(Font(R.font.nunitos))
                ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = {
                Text(text = "Type something...",
                    color = Color.Gray,
                    style = TextStyle(
                        fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.nunitos)),
                    fontWeight = FontWeight.Normal))
                          },
            modifier = Modifier.fillMaxSize(),
            textStyle = TextStyle(
                fontSize = 23.sp, color = Color.White, fontWeight = FontWeight.Light,
                fontFamily = FontFamily(Font(R.font.nunitos))
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            )
        )
    }
}

@Composable
fun DiscardChangesDialog(
    onDismiss: () -> Unit,
    onDiscard: () -> Unit,
    onSave: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(330.dp)
                .height(236.dp)
                .clip(RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(16.dp),
            color = Color.Black
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "info",
                    tint = Color.Gray,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Are you sure you want" +
                                "\ndiscard your changes ?",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontFamily = FontFamily(Font(R.font.nunitos)),
                        fontWeight = FontWeight.W800,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDiscard() },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color(0xffff0000)),
                        modifier = Modifier.size(width = 112.dp , height = 39.dp)
                    ) {
                        Text("Discard",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nunitos)),
                            fontWeight = FontWeight.W400
                            )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { onSave() },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color(0xFF30BE71)),
                        modifier = Modifier.size(width = 112.dp , height = 39.dp)
                    ) {
                        Text("Keep",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.nunitos)),
                            fontWeight = FontWeight.W400
                            )
                    }
                }
            }
            }
        }
    }
