package com.example.financeapp

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.sql.SQLException
import java.util.concurrent.Executors

private const val DATABASE_LOG_TAG = "TransactionDatabase"

internal class TransactionRepository(context: Context) {
    private val database = TransactionDatabase.open(context)
    private val dao = database.transactionDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun load(onResult: (List<Transaction>?) -> Unit) {
        executor.execute {
            val transaction = try {
                dao.readAll().map(TransactionEntity::toTransaction)
            } catch (error: SQLException) {
                Log.e(DATABASE_LOG_TAG, "Could not load transactions", error)
                null
            } catch (error: IllegalStateException) {
                Log.e(DATABASE_LOG_TAG, "Could not load transactions", error)
                null
            }
            mainHandler.post { onResult(transaction) }
        }
    }

    fun save(transaction: Transaction, onResult: (Boolean) -> Unit) {
        executor.execute {
            val saved = try {
                dao.insert(TransactionEntity.fromTransaction(transaction))
                true
            } catch (error: SQLException) {
                Log.e(DATABASE_LOG_TAG, "Could not save transactions", error)
                false
            } catch (error: IllegalStateException) {
                Log.e(DATABASE_LOG_TAG, "Could not save transactions", error)
                false
            }
            mainHandler.post { onResult(saved) }
        }
    }

    fun close() {
        executor.execute(database::close)
        executor.shutdown()
    }
}
