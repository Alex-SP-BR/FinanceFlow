package com.max.financeflow.database

import android.content.ContentValues
import com.max.financeflow.entity.Movimentacao
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            """
        CREATE TABLE IF NOT EXISTS movimentacao (
            _id INTEGER PRIMARY KEY AUTOINCREMENT,
            valor REAL,
            descricao TEXT,
            data TEXT,
            tipo TEXT
        )
        """.trimIndent()
        )

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(
            "DROP TABLE IF EXISTS movimentacao"
        )

        onCreate(db)

    }

    fun incluir(movimentacao: Movimentacao) {

        val banco = this.writableDatabase

        val registro = ContentValues()

        registro.put(
            "valor",
            movimentacao.valor
        )

        registro.put(
            "descricao",
            movimentacao.descricao
        )

        registro.put(
            "data",
            movimentacao.data
        )

        registro.put(
            "tipo",
            movimentacao.tipo
        )

        banco.insert(
            TABLE_NAME,
            null,
            registro
        )

    }

    fun listar(): MutableList<Movimentacao> {

        val banco = this.writableDatabase

        val registros = banco.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val lista = mutableListOf<Movimentacao>()

        while (registros.moveToNext()) {

            val movimentacao = Movimentacao(
                registros.getInt(0),
                registros.getDouble(1),
                registros.getString(2),
                registros.getString(3),
                registros.getString(4)
            )

            lista.add(movimentacao)

        }

        registros.close()

        return lista

    }
    companion object {

        private const val DATABASE_NAME = "financeflow.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "movimentacao"

    }

}