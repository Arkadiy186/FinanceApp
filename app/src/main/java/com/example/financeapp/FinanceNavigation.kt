package com.example.financeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private const val TRANSACTIONS_ROUTE = "transactions"
private const val SETTINGS_ROUTE = "settings"

@Composable
@Suppress("FunctionNaming")
internal fun FinanceNavigation() {
    val navController = rememberNavController()
    val transactionViewModel: TransactionViewModel = viewModel()
    var showBalance by rememberSaveable { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = TRANSACTIONS_ROUTE) {
        composable(TRANSACTIONS_ROUTE) {
            TransactionScreen(
                viewModel = transactionViewModel,
                showBalance = showBalance,
                onOpenSettings = {
                    navController.navigate(SETTINGS_ROUTE) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen(
                showBalance = showBalance,
                onShowBalanceChange = { showBalance = it },
                onBack = { navController.navigateUp() },
            )
        }
    }
}
