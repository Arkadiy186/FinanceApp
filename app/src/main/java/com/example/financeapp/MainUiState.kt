package com.example.financeapp

private const val DEFAULT_BALANCE = 25_000

internal data class MainUiState(
    val balance: Int = DEFAULT_BALANCE,
    val transactionCount: Int = 0,
)

internal enum class TransactionType {
    Income,
    Expense,
}

internal data class Transaction(
    val type: TransactionType,
    val amount: Int,
)

internal sealed interface MainUiEvent {
    data class AddTransaction(val transaction: Transaction) : MainUiEvent
}

internal fun reduce(state: MainUiState, event: MainUiEvent): MainUiState =
    when (event) {
    is MainUiEvent.AddTransaction -> {
        val transaction = event.transaction

        require(transaction.amount > 0) { "Amount must be positive" }

        // Вычисляем изменение баланса: + для дохода, - для расхода
        val delta = when (transaction.type) {
            TransactionType.Income -> transaction.amount
            TransactionType.Expense -> -transaction.amount
        }

        state.copy(
            balance = state.balance + delta,
            transactionCount = state.transactionCount + 1,
        )
    }
}
