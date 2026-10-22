package com.mobile.farm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import kotlinx.serialization.Serializable

import com.mobile.farm.ui.theme.FarmTheme
import com.mobile.farm.tree.TreesList
import com.mobile.farm.tree.TreeDetails
import com.mobile.farm.tree.Tree

@Serializable
object FarmHome
@Serializable
object VisitsList

@Serializable
object FruitsList

@Serializable
data class VisitDetails(val date: Int)

val trees = listOf(
    Tree(id = 1),
    Tree(id = 2),
    Tree(id = 3),
    Tree(id = 4),
    Tree(id = 5)
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FarmNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FarmNavHost(modifier: Modifier) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = FarmHome) {
        composable<FarmHome> {
            FarmScreen(
                onVisitsClick = {navController.navigate(VisitsList)},
                onTreesClick = {navController.navigate(TreesList)},
                onFruitsClick = {navController.navigate((FruitsList))}
            )
        }

        composable<VisitsList> {
            VisitsScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }

        composable<TreesList> {
            TreesScreen(
                onNavigateBack = {navController.popBackStack()},
                onTreeClick = { tree ->
                    navController.navigate(TreeDetails(treeId = tree.id))
                }
            )
        }

        composable<FruitsList> {
            FruitsScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }

        composable<TreeDetails> { backStackEntry ->
            val details : TreeDetails = backStackEntry.toRoute()
            Text("${details.treeId}")
        }
    }
}

// TO DO: move code below to another file
@Composable
fun FarmScreen(
    onVisitsClick: () -> Unit,
    onTreesClick: () -> Unit,
    onFruitsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Farm")

        Button(onClick = onVisitsClick) {
            Text("Visits")
        }

        Button(onClick = onTreesClick) {
            Text("Trees")
        }

        Button(onClick = onFruitsClick) {
            Text("Fruits")
        }
    }
}

@Composable
fun VisitsScreen(onNavigateBack: () -> Unit) {
    Text("Visits will be shown here")

    Button(onClick = onNavigateBack) {
        Text("Back")
    }
}

@Composable
fun TreesScreen(
    onNavigateBack: () -> Unit,
    onTreeClick: (Tree) -> Unit
) {
    Column() {
        Text("Trees will be shown here")

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

@Composable
fun FruitsScreen(onNavigateBack: () -> Unit) {
    Text("Fruits will be shown here")

    Button(onClick = onNavigateBack) {
        Text("Back")
    }
}