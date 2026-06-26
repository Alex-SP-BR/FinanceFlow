package com.max.financeflow

import android.view.View
import android.os.Bundle
import java.text.NumberFormat
import java.util.Locale
import com.max.financeflow.adapter.MovimentacaoAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.max.financeflow.database.DatabaseHandler
import com.max.financeflow.databinding.ActivityExtratoBinding

class ExtratoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExtratoBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExtratoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler(this)
        listar()

        binding.btVoltar.setOnClickListener {

            finish()

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun listar() {

        val registros = banco.listar()

        if (registros.isEmpty()) {

            binding.tvMensagem.visibility = View.VISIBLE

        } else {

            binding.tvMensagem.visibility = View.GONE

        }

        var saldo = 0.0

        registros.forEach { movimentacao ->

            if (movimentacao.tipo == "Receita") {
                saldo += movimentacao.valor
            } else {
                saldo -= movimentacao.valor
            }

        }

        val adapter = MovimentacaoAdapter(
            this,
            registros
        )

        binding.lvMovimentacoes.adapter = adapter

        val formatador = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))
        binding.tvSaldo.text = "Saldo Atual: ${formatador.format(saldo)}"

    }
}