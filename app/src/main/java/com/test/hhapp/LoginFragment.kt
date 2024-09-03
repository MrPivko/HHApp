package com.test.hhapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.util.Patterns
import com.test.hhapp.R

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.email_edit_text)
        val clearTextButton = view.findViewById<ImageView>(R.id.clear_text_button)
        val continueButton = view.findViewById<Button>(R.id.continue_button)
        val errorMessage = view.findViewById<TextView>(R.id.error_message)

        // Показываем крестик, когда текст вводится
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearTextButton.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    emailEditText.background = resources.getDrawable(R.drawable.edit_text_background, null)
                } else {
                    clearTextButton.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Очистка текста при нажатии на крестик
        clearTextButton.setOnClickListener {
            emailEditText.text.clear()
        }

        // Проверка email на корректность
        continueButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (isValidEmail(email)) {
                // Переход к фрагменту подтверждения
                val verificationFragment = VerificationFragment()
                val args = Bundle().apply {
                    putString("email", email)
                }
                verificationFragment.arguments = args

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, verificationFragment)
                    .addToBackStack(null) // Добавление в BackStack, чтобы можно было вернуться
                    .commit()
            } else {
                errorMessage.visibility = View.VISIBLE
                emailEditText.background = resources.getDrawable(R.drawable.edit_text_error_background, null)
            }
        }
    }

    // Функция для проверки формата email
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}