package duyndph34554.fpoly.bai_4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import duyndph34554.fpoly.bai_4.database.COLUMN_DATE
import duyndph34554.fpoly.bai_4.database.COLUMN_ID
import duyndph34554.fpoly.bai_4.database.COLUMN_NAME
import duyndph34554.fpoly.bai_4.database.TodoContentProvider
import duyndph34554.fpoly.bai_4.ui.theme.Bai_4Theme

data class Task(var id: Long, var name: String, var date: String)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskListScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskListScreen() {
    val context = LocalContext.current
    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    LaunchedEffect(Unit) {
        taskList = loadTask(context)
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {
                Text(text = "Danh sach cong viec")
            })
        },
        content = {
            LazyColumn (
                modifier = Modifier.padding(top = 100.dp)
            ) {
                items(taskList) { task ->
                    TaskItem(task = task)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddTaskActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = null)
            }
        }
    )
}

@Composable
fun TaskItem(task: Task) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "Name: "+task.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Date: "+task.date, style = MaterialTheme.typography.bodyLarge)
        }

        Column {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.Blue
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}

@SuppressLint("Recycle")
fun loadTask(context: Context): List<Task> {
    val tasks = mutableListOf<Task>()
    val uri: Uri = TodoContentProvider.CONTENT_URI
    val cursor = context.contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        val idIndex = it.getColumnIndex(COLUMN_ID)
        val nameIndex = it.getColumnIndex(COLUMN_NAME)
        val dateIndex = it.getColumnIndex(COLUMN_DATE)

        if (idIndex == -1 || nameIndex == -1 || dateIndex == -1) {
            throw IllegalArgumentException("Column not found: idIndex=$idIndex, nameIndex=$nameIndex, dateIndex=$dateIndex")
        }

        while (it.moveToNext()) {
            val id = it.getLong(idIndex)
            val name = it.getString(nameIndex)
            val date = it.getString(dateIndex)
            tasks.add(Task(id, name, date))
        }
    }
    return tasks
}