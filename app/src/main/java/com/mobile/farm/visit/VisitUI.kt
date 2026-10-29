package com.mobile.farm.visit

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun VisitsScreen(onNavigateBack: () -> Unit) {
    Text("Visits will be shown here")

    Button(onClick = onNavigateBack) {
        Text("Back")
    }
}
