package com.test.hhapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class VerificationFragment : Fragment() {

    private lateinit var codeDigit1: EditText
    private lateinit var codeDigit2: EditText
    private lateinit var codeDigit3: EditText
    private lateinit var codeDigit4: EditText
    private lateinit var confirmButton: Button
    private lateinit var confirmationText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeDigit1 = view.findViewById(R.id.code_digit_1)
        codeDigit2 = view.findViewById(R.id.code_digit_2)
        codeDigit3 = view.findViewById(R.id.code_digit_3)
        codeDigit4 = view.findViewById(R.id.code_digit_4)
        confirmButton = view.findViewById(R.id.confirm_button)
        confirmationText = view.findViewById(R.id.confirmation_text)

        // Получение email из аргументов фрагмента
        val email = arguments?.getString("email")
        confirmationText.text = "Отправили код на $email"

        // Установка слушателей для полей ввода кода
        setupCodeInputListeners()

        // Установка слушателя для кнопки подтверждения
        confirmButton.setOnClickListener {
            // Успешный вход, обновление состояния в MainActivity
            (activity as? MainActivity)?.onLoginSuccess()
            // Переход к главному экрану
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupCodeInputListeners() {
        val digitViews = arrayOf(codeDigit1, codeDigit2, codeDigit3, codeDigit4)

        for (i in digitViews.indices) {
            digitViews[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        // Переключение фокуса на следующую ячейку
                        if (i < digitViews.size - 1) {
                            digitViews[i + 1].requestFocus()
                        }
                    }
                    // Проверка, активна ли кнопка подтверждения
                    confirmButton.isEnabled = digitViews.all { editText -> editText.text.length == 1 }
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
}
