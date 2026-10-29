package com.mobile.farm.tree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TreesScreen(
    trees : List<Tree>,
    onNavigateBack: () -> Unit,
    onTreeClick: (Tree) -> Unit
) {
    Column() {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = trees, key = { tree -> tree.id }) { tree ->
                Text(
                    text = "ID: ${tree.id}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onTreeClick(tree) }
                        .padding(16.dp)
                )
                HorizontalDivider()
            }
        }

        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}