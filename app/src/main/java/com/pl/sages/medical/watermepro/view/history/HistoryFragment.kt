package com.pl.sages.medical.watermepro.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.color.MaterialColors
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.domains.water.WaterIntake
import kotlinx.coroutines.flow.Flow

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Tutaj następuje integracja Jetpack Compose z Fragmentem
        return ComposeView(requireContext()).apply {
            setContent {
                // Funkcja kompozycyjna WaterHistoryScreen jest odpowiedzialna za wyświetlenie historii spożycia wody
                WaterHistoryScreen(WaterHistoryViewModel())
            }
        }
    }
}

@Composable
fun WaterHistoryScreen(viewModel: WaterHistoryViewModel) {
    // Ustanawiamy połączenie Flow z producentem tych danych -> repozytorium
    // Flow nie będzie działał dopóki nie jest subskrybowany
    // collectAsState - subskrybuje Flow i zwraca jego najnowszą wartość
    val waterHistory by viewModel.waterIntakes.collectAsState(initial = emptyList())

    // Funkcja Column jest odpowiedzialna za ułożenie elementów w kolumnie
    Column(
        // modyfikatory - odpowiadają za różne rzeczy związane z rozłożeniem tej funkcji na ekranie i interakcjami z innymi elementami

        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            .background(Color.White)
    ) {
        // Dwie funkcje text - odpowiedniki TextView w XMLu
        Text(
            text = "Water intake history",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Here you can evaluate your water intake history",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(40.dp))
        // LazyColumn - odpowiednik RecyclerView w XMLu
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            userScrollEnabled = true
        ) {
            // waterHistory - dane które wyświetla LazyColumn
            items(waterHistory) { waterIntake ->
                // z każdego jednego obiektu w liście waterHistory tworzymy element WaterHistoryItem i go wyświetlamy
                WaterHistoryItem(waterIntake)
            }
        }
    }
}

// Definicja komponentu WaterHistoryItem
@Composable
fun WaterHistoryItem(waterIntake: WaterIntake) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(horizontal = 16.dp),
    ) {
        Text(waterIntake.date)
        Spacer(modifier = Modifier.weight(1f))
        Text(waterIntake.waterIntake.toString())
    }
}

// Podgląd komponentu WaterHistoryItem
@Preview
@Composable
fun SimpleComposablePreview() {
    WaterHistoryScreen(WaterHistoryViewModel())
}

class WaterHistoryViewModel() {

    private val repository = Container.provideWaterRepository()

    val waterIntakes: Flow<List<WaterIntake>> = repository.getWaterIntakeHistoryFlow()
}