/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray


import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.OrderViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum

enum class LunchTrayScreens(@StringRes val title: Int)
{
    Start(R.string.start_order),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}

// TODO: AppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreens,
    canNavigateUp: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
)
{
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        modifier = modifier,
        navigationIcon = {
            if(canNavigateUp)
            {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
private fun cancelNavigation(navController: NavController)
{
    navController.navigate(route = LunchTrayScreens.Start.name)
}
@Composable
fun LunchTrayApp() {
    // TODO: Create Controller and initialization
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LunchTrayScreens.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreens.Entree.name
    )

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreen = LunchTrayScreens.Start,
                canNavigateUp = false,
                navigateUp = {}
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreens.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = LunchTrayScreens.Start.name){

                StartOrderScreen(onStartOrderButtonClicked = {
                    navController.navigate(route = LunchTrayScreens.Entree.name)
                }
                )
           }

            composable(route = LunchTrayScreens.Entree.name){
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { cancelNavigation(navController) },
                    onSelectionChanged = { viewModel.updateEntree(it)},
                    onNextButtonClicked = {navController.navigate(LunchTrayScreens.SideDish.name)}
                )
            }
            composable(route = LunchTrayScreens.SideDish.name){
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { cancelNavigation(navController) },
                    onSelectionChanged = {viewModel.updateSideDish(it)},
                    onNextButtonClicked = {navController.navigate(LunchTrayScreens.Accompaniment.name)}
                )
            }
            composable(route = LunchTrayScreens.Accompaniment.name){
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { cancelNavigation(navController) },
                    onSelectionChanged = {viewModel.updateAccompaniment(it)},
                    onNextButtonClicked = {navController.navigate(LunchTrayScreens.Checkout.name)}
                )
            }

            composable(route = LunchTrayScreens.Checkout.name){
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { cancelNavigation(navController) },
                    onCancelButtonClicked = { cancelNavigation(navController) }
                )
            }

        }

        // TODO: Navigation host
    }
}
