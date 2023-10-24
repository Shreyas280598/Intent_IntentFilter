package com.example.intentintentfilter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.intentintentfilter.ui.theme.IntentIntentFilterTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ImageViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntentIntentFilterTheme {
                var changedString by remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    TextField(value = changedString, onValueChange = {
                        changedString = it
                    })
                    Button(onClick = {
//                        Intent(applicationContext, SecondActivity::class.java).also {
//                            startActivity(it)
//                        }
//                        try {
//                            Intent(Intent.ACTION_MAIN).also {
//                                it.`package` = "com.google.android.youtube"
//                                startActivity(it)
//                            }
//                        } catch (e: ActivityNotFoundException){
//                            e.printStackTrace()
//                        }
                        //Implicit Intent

//                        val intent = Intent(Intent.ACTION_SEND).apply {
//                            type = "text/plain"
//                            putExtra("plainText", "this is a plain text test from main Activity")
//                            putExtra(Intent.EXTRA_SUBJECT, "This is subject")
//                            putExtra(Intent.EXTRA_TEXT, "This is a body of the email")
//                        }
//                        try {
//                            startActivity(intent)
//                        } catch (e: ActivityNotFoundException){
//                            e.printStackTrace()
//                        }

                        val intent = Intent(applicationContext, SecondActivity::class.java).apply {
                            type = "image/*"
                            putExtra("plainImage", viewModel.uri)
                        }
                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }

                    }) {
                        Text(text = "Click me")
                    }

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        viewModel.updateUri(uri)
    }
}
