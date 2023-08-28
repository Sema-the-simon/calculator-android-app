package com.example.calculatorasfirstapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorasfirstapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //clear text fields
        binding.etExprassion.setText("")
        binding.tvResult.text = ""

        //numbers
        binding.btnZero.setOnClickListener(onNumberClickListener)
        binding.btnOne.setOnClickListener(onNumberClickListener)
        binding.btnTwo.setOnClickListener(onNumberClickListener)
        binding.btnThree.setOnClickListener(onNumberClickListener)
        binding.btnFour.setOnClickListener(onNumberClickListener)
        binding.btnFive.setOnClickListener(onNumberClickListener)
        binding.btnSix.setOnClickListener(onNumberClickListener)
        binding.btnSeven.setOnClickListener(onNumberClickListener)
        binding.btnEight.setOnClickListener(onNumberClickListener)
        binding.btnNine.setOnClickListener(onNumberClickListener)

        //simbols
        binding.btnPlus.setOnClickListener(onMathSymbolsClickListener)
        binding.btnMinus.setOnClickListener(onMathSymbolsClickListener)
        binding.btnMultiply.setOnClickListener(onMathSymbolsClickListener)
        binding.btnDevide.setOnClickListener(onMathSymbolsClickListener)

    }

    private val onNumberClickListener = View.OnClickListener {
        val btn = it as Button
        var expr = binding.etExprassion.text.toString()

        if (btn.text.toString() != "0") {
            if (expr.isNotEmpty() && expr.last() == '0' &&
                (expr.length == 1 || expr[expr.length - 2].toString() in arrayListOf(
                    "+",
                    "-",
                    "*",
                    "/"
                ))
            )
                expr = expr.dropLast(1)
        }

        val newText = expr + btn.text.toString()
        binding.etExprassion.apply {
            setText(newText)
            setSelection(newText.lastIndex + 1)
        }


    }

    private val onMathSymbolsClickListener = View.OnClickListener {
        val btn = it as Button
        val expr = binding.etExprassion.text.toString()
        if (expr.length != 0) {
            val newText = expr + btn.text.toString()
            binding.etExprassion.apply {
                setText(newText)
                setSelection(newText.lastIndex + 1)
            }
        }
    }
}