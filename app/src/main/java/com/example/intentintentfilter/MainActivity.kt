package com.example.intentintentfilter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.example.intentintentfilter.ui.theme.IntentIntentFilterTheme
import java.util.Currency
import java.util.Locale

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
                    TextField(
                        label = { Text("Enter Country Name") },
                        value = changedString,
                        onValueChange = { changedString = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    Button(onClick = {
                        getLocaleByCountry(changedString)?.let {
                            val currency = Currency.getInstance(it)
                            val currencyCode: String = currency.currencyCode
                            Toast.makeText(applicationContext, currencyCode, Toast.LENGTH_LONG)
                                .show()
                        } ?: run {
                            Toast.makeText(
                                applicationContext,
                                "Country not found",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }) {
                        Text(text = "Click me to get Country Code")
                    }

                }
            }
        }
    }

    private fun getLocaleByCountry(countryName: String): Locale? {
        return Locale.getAvailableLocales()
            .find { it.displayCountry.equals(countryName, ignoreCase = false) }
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
