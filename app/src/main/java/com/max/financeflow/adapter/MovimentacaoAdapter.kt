package com.max.financeflow.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale
import com.max.financeflow.R
import com.max.financeflow.entity.Movimentacao

class MovimentacaoAdapter(
    private val context: Context,
    private val lista: List<Movimentacao>
) : BaseAdapter() {

    override fun getCount(): Int {
        return lista.size
    }

    override fun getItem(position: Int): Any {
        return lista[position]
    }

    override fun getItemId(position: Int): Long {
        return lista[position].id.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val view = LayoutInflater
            .from(context)
            .inflate(
                R.layout.item_movimentacao,
                parent,
                false
            )

        val descricao =
            view.findViewById<TextView>(R.id.tvDescricao)

        val data =
            view.findViewById<TextView>(R.id.tvData)

        val valor =
            view.findViewById<TextView>(R.id.tvValor)

        val movimentacao = lista[position]

        val formatador = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

        descricao.text = movimentacao.descricao

        data.text = movimentacao.data

        valor.text = formatador.format(movimentacao.valor)

        if (movimentacao.tipo == "Receita") {

            valor.setTextColor(Color.GREEN)

        } else {

            valor.setTextColor(Color.RED)

        }

        return view

    }

}