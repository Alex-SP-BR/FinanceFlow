package com.max.financeflow

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import android.app.DatePickerDialog
import java.text.NumberFormat
import java.util.Locale
import java.util.Calendar
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.max.financeflow.database.DatabaseHandler
import com.max.financeflow.databinding.ActivityMainBinding
import com.max.financeflow.entity.Movimentacao

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        banco = DatabaseHandler(this)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.etData.setOnClickListener {

            val calendario = Calendar.getInstance()

            val ano = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, anoSelecionado, mesSelecionado, diaSelecionado ->

                    val data =
                        "$diaSelecionado/${mesSelecionado + 1}/$anoSelecionado"

                    binding.etData.setText(data)

                },
                ano,
                mes,
                dia
            )

            datePicker.show()
        }
        binding.btSalvar.setOnClickListener {

            salvar()

        }

        binding.etValor.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    binding.etValor.removeTextChangedListener(this)

                    val cleanString = s.toString().replace(Regex("[R$,.\\s]"), "")

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble()
                        val formatted = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(parsed / 100)

                        current = formatted
                        binding.etValor.setText(formatted)
                        binding.etValor.setSelection(formatted.length)
                    } else {
                        current = ""
                        binding.etValor.setText("")
                    }

                    binding.etValor.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btExtrato.setOnClickListener {

            val tela = Intent(
                this,
                ExtratoActivity::class.java
            )

            startActivity(tela)

        }
    }
    private fun salvar() {

        if (binding.etValor.text.toString().isEmpty()) {

            Toast.makeText(
                this,
                "Informe o valor.",
                Toast.LENGTH_SHORT
            ).show()

            binding.etValor.requestFocus()
            return
        }

        if (binding.etDescricao.text.toString().isEmpty()) {

            Toast.makeText(
                this,
                "Informe a descrição.",
                Toast.LENGTH_SHORT
            ).show()

            binding.etDescricao.requestFocus()
            return
        }

        if (binding.etData.text.toString().isEmpty()) {

            Toast.makeText(
                this,
                "Selecione a data.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val tipoSelecionado =
            if (binding.rbReceita.isChecked)
                "Receita"
            else
                "Despesa"

        val valorTexto = binding.etValor.text.toString()
            .replace(Regex("[R$,.\\s]"), "")
        val valorNumerico = if (valorTexto.isNotEmpty()) valorTexto.toDouble() / 100 else 0.0

        val movimentacao = Movimentacao(
            0,
            valorNumerico,
            binding.etDescricao.text.toString(),
            binding.etData.text.toString(),
            tipoSelecionado
        )

        banco.incluir(movimentacao)

        Toast.makeText(
            this,
            "Movimentação salva com sucesso!",
            Toast.LENGTH_LONG
        ).show()

        binding.etValor.text.clear()
        binding.etDescricao.text.clear()
        binding.etData.text.clear()

        binding.rbReceita.isChecked = true

        binding.etValor.requestFocus()

    }
}