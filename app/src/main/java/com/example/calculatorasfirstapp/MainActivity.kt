package com.example.calculatorasfirstapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorasfirstapp.databinding.ActivityMainBinding
import com.notkamui.keval.KevalException
import com.notkamui.keval.keval

class MainActivity : AppCompatActivity() {
    val MATH_SYMBOLS = arrayListOf("+", "-", "*", "/")

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //disable keyboard
        binding.etExprassion.showSoftInputOnFocus = false

        //clear text fields
        binding.etExprassion.setText("")
        binding.tvResult.text = ""

        //numbers
        binding.apply {
            btnZero.setOnClickListener(onNumberClickListener)
            btnOne.setOnClickListener(onNumberClickListener)
            btnTwo.setOnClickListener(onNumberClickListener)
            btnThree.setOnClickListener(onNumberClickListener)
            btnFour.setOnClickListener(onNumberClickListener)
            btnFive.setOnClickListener(onNumberClickListener)
            btnSix.setOnClickListener(onNumberClickListener)
            btnSeven.setOnClickListener(onNumberClickListener)
            btnEight.setOnClickListener(onNumberClickListener)
            btnNine.setOnClickListener(onNumberClickListener)
        }

        //math
        binding.apply {
            btnPlus.setOnClickListener(onMathSymbolsClickListener)
            btnMinus.setOnClickListener(onMathSymbolsClickListener)
            btnMultiply.setOnClickListener(onMathSymbolsClickListener)
            btnDevide.setOnClickListener(onMathSymbolsClickListener)
        }

        //others
        binding.apply {
            btnEqual.setOnClickListener {
                etExprassion.setText(tvResult.text)
                tvResult.text = ""
            }

            btnClear.setOnClickListener {
                binding.etExprassion.setText("")
                binding.tvResult.text = ""
            }

            btnBackspace.setOnClickListener {
                val expression = binding.etExprassion.text.toString()
                val position = binding.etExprassion.selectionStart
                val leftPart = if (expression.isEmpty()) "" else expression.substring(0 until position -1)
                val res = leftPart + expression.substring(position)
                binding.etExprassion.apply {
                    setText(res)
                    setSelection(leftPart.length)
                    eval()
                }
            }
        }


    }

    private fun eval() {
        val expression = binding.etExprassion.text.toString()
        try {
            val res = expression.keval()
            binding.tvResult.text = if (res % 1 == 0.0)
                res.toInt().toString()
            else
                res.toString()
        } catch (e: KevalException) {
            binding.tvResult.text = ""
        }


    }

    private val onNumberClickListener = View.OnClickListener {
        val btn = it as Button
        val expression = binding.etExprassion.text.toString()
        val position = binding.etExprassion.selectionStart
        var leftPart = if (expression.isEmpty()) "" else expression.substring(0 until position)
        var rightPart = expression.substring(position)
        var newTextToSet = ""

        //  |123                - not 0
        //  123 + |123456 + 123 - not 0
        //  123 + 123|456 + 123 - all pass
        //  123 + 123456| + 123 - all pass
        //                 123| - all pass
        if (rightPart.isNotEmpty() && rightPart.first().isDigit()
            && (leftPart.isEmpty() || leftPart.last().toString() in MATH_SYMBOLS)
            && btn.text.toString() == "0"
        )
            newTextToSet = leftPart + rightPart


        //  123 + 0| + 123 - change 0 to entered digit
        //              0| - change 0 to entered digit
        else {
            if ((rightPart.isEmpty() || rightPart.first().toString() in MATH_SYMBOLS)
                && leftPart.isNotEmpty() && leftPart.last() == '0'
                && (leftPart.length == 1 || leftPart[leftPart.length - 2].toString() in MATH_SYMBOLS)
            )
                leftPart = leftPart.dropLast(1)
            leftPart += btn.text.toString()
            newTextToSet = leftPart + rightPart
        }

        binding.etExprassion.apply {
            setText(newTextToSet)
            setSelection(leftPart.length)
            eval()
        }
    }

    private val onMathSymbolsClickListener = View.OnClickListener {
        val btn = it as Button
        val expression = binding.etExprassion.text.toString()
        val position = binding.etExprassion.selectionStart
        var leftPart = if (expression.isEmpty()) "" else expression.substring(0 until position)
        var rightPart = expression.substring(position)
        var newTextToSet = ""


        //  |123 + 123 -> no effect
        //  123 |+ 123 -> change symbols
        //  123 +| 123 -> change symbols
        //  123 + 1|23 -> just paste
        //  123 + 123| -> just paste

        if (leftPart.isNotEmpty()) {
            if (leftPart.last().toString() in MATH_SYMBOLS)
                leftPart = leftPart.dropLast(1)
            if (rightPart.isNotEmpty() && rightPart.first().toString() in MATH_SYMBOLS)
                rightPart = rightPart.drop(1)

            leftPart += btn.text.toString()
            newTextToSet = leftPart + rightPart
            binding.etExprassion.apply {
                setText(newTextToSet)
                setSelection(leftPart.length)
                eval()
            }
        }
    }
}