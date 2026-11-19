package com.mobile.farm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.mobile.farm.tree.TreesScreen
import com.mobile.farm.tree.TreesDatabase
import com.mobile.farm.tree.TreeDao
import com.mobile.farm.tree.TreeDetailsScreen
import com.mobile.farm.visit.VisitsList
import com.mobile.farm.visit.VisitDetails
import com.mobile.farm.visit.Visit
import com.mobile.farm.visit.VisitsScreen
import com.mobile.farm.visit.VisitsDatabase
import com.mobile.farm.visit.VisitDao
import com.mobile.farm.fruit.FruitsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Serializable
object FarmHome

@Serializable
object FruitsList

val seedTrees = listOf(
    Tree(id = 1, type = "Apple"),
    Tree(id = 2, type = "Banana"),
    Tree(id = 3, type = "Cherry"),
    Tree(id = 4, type = "Durian"),
    Tree(id = 5, type = "Elm")
)

val seedVisits = listOf(
    Visit(date = 123),
    Visit(date = 278),
    Visit(date = 99)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val treeDb = TreesDatabase.getInstance(applicationContext)
        val treeDao = treeDb.treeDao()
        val visitDb = VisitsDatabase.getInstance(applicationContext)
        val visitDao = visitDb.visitDao()

        enableEdgeToEdge()
        setContent {
            FarmTheme {
                Scaffold { innerPadding ->
                    FarmNavHost(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        treeDao = treeDao,
                        visitDao = visitDao
                    )
                }
            }
        }
    }
}

@Composable
fun FarmNavHost(
    modifier: Modifier,
    treeDao: TreeDao,
    visitDao: VisitDao
) {
    //Remove later
    LaunchedEffect(Unit) {
        if (treeDao.count() == 0) { treeDao.insertAll(seedTrees) }
        if (visitDao.count() == 0) { visitDao.insertAll(seedVisits) }
    }
    //Remove later

    val navController: NavHostController = rememberNavController()
    val allTrees by treeDao.getAll().collectAsState(initial = emptyList())
    val allVisits by visitDao.getAll().collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = FarmHome) {
        composable<FarmHome> {
            FarmScreen(
                onVisitsClick = {navController.navigate(VisitsList)},
                onTreesClick = {navController.navigate(TreesList)},
                onFruitsClick = {navController.navigate((FruitsList))},
                modifier = modifier
            )
        }

        composable<VisitsList> {
            VisitsScreen(
                visits = allVisits,
                onNavigateBack = {navController.popBackStack()},
                onVisitClick = { visit ->
                    navController.navigate(VisitDetails(visitDate = visit.date))
                }
            )
        }

        composable<TreesList> {
            TreesScreen(
                trees = allTrees,
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
            val tree = allTrees.first { it.id == details.treeId }
            TreeDetailsScreen(
                tree = tree,
                onNavigateBack = {navController.popBackStack()}
            )
        }

        composable<VisitDetails> { backStackEntry ->
            val details : VisitDetails = backStackEntry.toRoute()
            Text("${details.visitDate}")
        }

    }
}

@Composable
fun FarmScreen(
    onVisitsClick: () -> Unit,
    onTreesClick: () -> Unit,
    onFruitsClick: () -> Unit,
    modifier: Modifier
) {
    var temperature by remember { mutableStateOf("Loading ... ") }
    LaunchedEffect(Unit) { temperature = fetchTemperature() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Farm Main Menu",
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )

        Text(
            text = "Location:  ",
            modifier = Modifier.align(Alignment.Start)
                .padding(start = 30.dp, end = 30.dp, bottom = 10.dp)
        )

        Text(
            text = "Temperature: $temperature",
            modifier = Modifier.align(Alignment.Start)
                .padding(start = 30.dp, end = 30.dp, bottom = 15.dp)
            )

        Button(
            onClick = onVisitsClick,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(10.dp).width(160.dp)
        ) {
            Text("Visits")
        }

        Button(
            onClick = onTreesClick,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(10.dp).width(160.dp)
        ) {
            Text("Trees")
        }

        Button(
            onClick = onFruitsClick,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(10.dp).width(160.dp)
            ) {
            Text("Fruits")
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.padding(10.dp).width(160.dp)
        ) {
            Text("Settings")
        }
    }
}

suspend fun fetchTemperature(): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = "https://api.open-meteo.com/v1/forecast?latitude=11.13278&longitude=106.60833&current=temperature_2m"

            // Read the JSON response from the URL
            val response = URL(url).readText()

            // Parse the JSON string
            val jsonObject = JSONObject(response)
            val currentObj = jsonObject.getJSONObject("current")
            val temp = currentObj.getDouble("temperature_2m")

            "$temp °C"
        } catch (e: Exception) {
            "Error fetching data: ${e.localizedMessage}"
        }
    }
}