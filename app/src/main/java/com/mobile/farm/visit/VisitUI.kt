package com.mobile.farm.visit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VisitsScreen(
    visits: List<Visit>,
    onNavigateBack: () -> Unit,
    onVisitClick: (Visit) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f).padding(top = 20.dp)) {
            items(items = visits, key = {visit -> visit.date}) { visit ->
                Text(
                    text = "ID: ${visit.date}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onVisitClick(visit) }
                        .padding(16.dp)
                )
                HorizontalDivider()
            }
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth().padding(40.dp)
        ) {
            Text("Back")
        }
    }
}
