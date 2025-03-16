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

        // TODO: Navigation host
    }
}
