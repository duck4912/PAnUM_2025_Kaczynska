package com.example.bieganie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RunningCalculator()
                }
            }
        }
    }
}

@Composable
fun RunningCalculator() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Z Tempa", "Z Prędkości", "Tempo i Prędkość")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Kalkulator Biegacza",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (selectedTab) {
                0 -> PaceToOthers()
                1 -> SpeedToOthers()
                2 -> DistanceAndTimeToPaceSpeed()
            }
        }
    }
}

@Composable
fun PaceToOthers() {
    var paceMinStr by remember { mutableStateOf("") }
    var paceSecStr by remember { mutableStateOf("") }
    var customDistanceStr by remember { mutableStateOf("") }

    Text("Podaj tempo biegu (min/km):", style = MaterialTheme.typography.titleMedium)
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = paceMinStr,
            onValueChange = { paceMinStr = it },
            label = { Text("Min") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = paceSecStr,
            onValueChange = { paceSecStr = it },
            label = { Text("Sek") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = customDistanceStr,
        onValueChange = { customDistanceStr = it },
        label = { Text("Własny dystans (km)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    val pMin = paceMinStr.toDoubleOrNull() ?: 0.0
    val pSec = paceSecStr.toDoubleOrNull() ?: 0.0
    val pace = pMin + pSec / 60.0

    if (pace > 0) {
        val speed = 60.0 / pace
        ResultsSection(speed, pace, customDistanceStr.toDoubleOrNull())
    }
}

@Composable
fun SpeedToOthers() {
    var speedStr by remember { mutableStateOf("") }
    var customDistanceStr by remember { mutableStateOf("") }

    Text("Podaj prędkość biegu (km/h):", style = MaterialTheme.typography.titleMedium)
    OutlinedTextField(
        value = speedStr,
        onValueChange = { speedStr = it },
        label = { Text("Prędkość (km/h)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = customDistanceStr,
        onValueChange = { customDistanceStr = it },
        label = { Text("Własny dystans (km)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    val speed = speedStr.toDoubleOrNull()
    if (speed != null && speed > 0) {
        val pace = 60.0 / speed
        ResultsSection(speed, pace, customDistanceStr.toDoubleOrNull())
    }
}

@Composable
fun DistanceAndTimeToPaceSpeed() {
    var distanceStr by remember { mutableStateOf("") }
    var timeHoursStr by remember { mutableStateOf("") }
    var timeMinutesStr by remember { mutableStateOf("") }
    var timeSecondsStr by remember { mutableStateOf("") }

    Text("Podaj dystans i czas:", style = MaterialTheme.typography.titleMedium)
    OutlinedTextField(
        value = distanceStr,
        onValueChange = { distanceStr = it },
        label = { Text("Dystans (km)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = timeHoursStr,
            onValueChange = { timeHoursStr = it },
            label = { Text("Godz") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
            value = timeMinutesStr,
            onValueChange = { timeMinutesStr = it },
            label = { Text("Min") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(4.dp))
        OutlinedTextField(
            value = timeSecondsStr,
            onValueChange = { timeSecondsStr = it },
            label = { Text("Sek") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }

    val distance = distanceStr.toDoubleOrNull()
    val hours = timeHoursStr.toDoubleOrNull() ?: 0.0
    val minutes = timeMinutesStr.toDoubleOrNull() ?: 0.0
    val seconds = timeSecondsStr.toDoubleOrNull() ?: 0.0
    
    val totalMinutes = hours * 60 + minutes + seconds / 60.0

    if (distance != null && distance > 0 && totalMinutes > 0) {
        val pace = totalMinutes / distance
        val speed = 60.0 / pace
        
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Wyniki:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Wymagane tempo: ${formatPace(pace)} min/km", fontSize = 18.sp)
                Text("Wymagana prędkość: ${"%.2f".format(speed)} km/h", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ResultsSection(speed: Double, pace: Double, customDist: Double?) {
    Spacer(modifier = Modifier.height(24.dp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Wyniki:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Prędkość: ${"%.2f".format(speed)} km/h", fontSize = 18.sp)
            Text("Tempo: ${formatPace(pace)} min/km", fontSize = 18.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Przewidywane czasy:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            
            Text("Maraton (42.195 km):", fontWeight = FontWeight.SemiBold)
            Text(formatTime(42.195 * pace))
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Półmaraton (21.0975 km):", fontWeight = FontWeight.SemiBold)
            Text(formatTime(21.0975 * pace))
            
            if (customDist != null && customDist > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dystans $customDist km:", fontWeight = FontWeight.SemiBold)
                Text(formatTime(customDist * pace))
            }
        }
    }
}

fun formatTime(totalMinutes: Double): String {
    val totalSeconds = (totalMinutes * 60).roundToInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return buildString {
        if (hours > 0) append("$hours godz. ")
        append("$minutes min. ")
        append("$seconds sek.")
    }
}

fun formatPace(paceInMinutes: Double): String {
    val minutes = paceInMinutes.toInt()
    val seconds = ((paceInMinutes - minutes) * 60).roundToInt()
    return if (seconds >= 60) {
        "${minutes + 1}:00"
    } else {
        "${minutes}:${"%02d".format(seconds)}"
    }
}
