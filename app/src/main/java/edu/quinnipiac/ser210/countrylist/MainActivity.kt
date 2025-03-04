package edu.quinnipiac.ser210.countrylist

import CountryList
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.quinnipiac.ser210.countrylist.ui.theme.CountryListTheme


@Composable
fun CountryElement(
    @DrawableRes drawable: Int,
    name: String,
    currency: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(50.dp).padding(4.dp)
        )
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Country: $name",
                modifier = Modifier.paddingFromBaseline(top = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleSmall)
            Text(
                text = "Currency: $currency",
                modifier = Modifier,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun CountryColumn(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(innerPadding)
    ) {
        items(CountryList) { item ->
            CountryElement(
                drawable = item.flag,
                name = item.name,
                currency = item.currency,
                onClick = {
                    navController.navigate("country_details/${item.name}/${item.flag}/${item.fact}")
                })
        }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetails(
    @DrawableRes drawable: Int,
    name: String,
    fact: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Country Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "null"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(200.dp).padding(10.dp)
            )
            Text(
                text = "$name Fun Fact:",
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = fact,
                modifier = Modifier,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun NavigationSetup() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "country_column") {
        composable("country_column") {
            CountryColumn(navController = navController)
        }
        composable(
            "country_details/{name}/{drawable}/{fact}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("drawable") { type = NavType.IntType },
                navArgument("fact") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val drawable = backStackEntry.arguments?.getInt("drawable") ?: 0
            val fact = backStackEntry.arguments?.getString("fact") ?: ""
            CountryDetails(drawable = drawable, name = name, fact = fact, navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryElementPreview() {
    CountryListTheme {
        CountryElement(
            drawable = R.drawable.india,
            name = CountryList[0].name,
            currency = CountryList[0].currency,
            onClick = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CountryColumnPreview() {
    CountryListTheme { NavigationSetup() }
}

@Preview(showBackground = true)
@Composable
fun CountryDetailsPreview() {
    CountryListTheme {
        CountryDetails(
            drawable = R.drawable.japan_map,
            name = CountryList[7].name,
            fact = CountryList[7].fact,
            navController = rememberNavController(),
            modifier = Modifier
        ) }
}
