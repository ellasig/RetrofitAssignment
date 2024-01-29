package com.example.w3d1retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.w3d1retrofit.ui.theme.W3d1RetrofitTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            W3d1RetrofitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = remember { MyViewModel() }
                    PresidentsList(viewModel)
                }
            }
        }
    }
}

class WikipediaService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: WikipediaApi = retrofit.create(WikipediaApi::class.java)

    suspend fun getHitCount(presidentName: String): WikipediaResponse {
        return service.getHitCount(presidentName)
    }
}

interface WikipediaApi {
    @GET("w/api.php?action=query&format=json&list=search")
    suspend fun getHitCount(@Query("srsearch") presidentName: String): WikipediaResponse
}

data class WikipediaResponse(
    val query: QueryResponse
)

data class QueryResponse(
    val searchinfo: SearchInfoResponse
)

data class SearchInfoResponse(
    val totalhits: Int
)


class MyViewModel : ViewModel() {
    private val repository: WikiRepository = WikiRepository()
    var wikiUiState: Int by mutableStateOf(0)
        private set

    fun getHits(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = repository.hitCountCheck(name)
            wikiUiState = serverResp.query.searchinfo.totalhits
        }
    }
}

class WikiRepository {
    private val service: WikipediaService = WikipediaService()

    suspend fun hitCountCheck(presidentName: String): WikipediaResponse {
        return service.getHitCount(presidentName)
    }
}


@Composable
fun PresidentsList(viewModel: MyViewModel) {
    var selectedPresident by remember { mutableStateOf<String?>(null) }
    var hits by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        selectedPresident?.let {
            Text(
                text = "$it Wikipedia Hits: $hits",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        DataProvider.presidents.forEach { president ->
            Text(
                text = president.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedPresident = president.name
                        viewModel.getHits(president.name)
                    }
                    .padding(16.dp),
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }

    LaunchedEffect(viewModel.wikiUiState) {
        hits = viewModel.wikiUiState
    }
}


@Preview(showBackground = true)
@Composable
fun PresidentsListPreview() {
    W3d1RetrofitTheme {
        PresidentsList(MyViewModel())
    }
}