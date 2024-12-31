package com.example.intentintentfilter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.intentintentfilter.ui.theme.IntentIntentFilterTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


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
                var data by remember {
                    mutableStateOf(null as TestClass?)
                }
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
                        apiCall(changedString) {
                            data = it
                        }
                    }) {
                        Text(text = "Click to get Currency Code")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = data?.data?.result?.result.toString(),
                        fontSize = 24.sp,
                        modifier = Modifier
                    )

                }
            }
        }
    }

    private fun apiCall(
        changedString: String,
        onDataChange: (TestClass) -> Unit,
    ) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.apiverve.com/v1/unitconverter?value=${changedString}&from=cm&to=m")
            .addHeader("x-api-key", "28cf7f77-3c23-436c-8f3b-de745609c843")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) throw IOException("Unexpected code $response")
                    val resStr = response.body!!.string()
                    val json = JSONObject(resStr)
                    val result = json.getJSONObject("data").getString("result")
                    val jsonObject = JSONObject(result)
                    json.keys().forEach { key ->
                        if (key == "data") {
                            val data = TestClass(
                                code = json.getInt("code"),
                                data = Data(
                                    result = Result(
                                        from = jsonObject.getString("from"),
                                        to = jsonObject.getString("to"),
                                        result = jsonObject.getInt("result")
                                    )
                                ),
                            )
                            onDataChange(data)
                        }
                    }
                }
            }
        })
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
