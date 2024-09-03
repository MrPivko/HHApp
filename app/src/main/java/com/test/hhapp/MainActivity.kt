package com.test.hhapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.test.hhapp.ui.theme.HHAppTheme

class MainActivity : AppCompatActivity() {

    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Изначально показываем экран входа
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (!isLoggedIn) {
                // Если пользователь не вошёл в систему, ничего не делаем
                return@setOnItemSelectedListener false
            }

            when (item.itemId) {
                R.id.nav_search -> {
                    openFragment(SearchFragment())
                    true
                }
                R.id.nav_favorites -> {
                    openFragment(FavoritesFragment())
                    true
                }
                R.id.nav_responses, R.id.nav_messages, R.id.nav_profile -> {
                    openFragment(PlaceholderFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun onLoginSuccess() {
        isLoggedIn = true
        // Обновление состояния кнопок навигации
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.menu.findItem(R.id.nav_search).isEnabled = true
        bottomNavigationView.menu.findItem(R.id.nav_favorites).isEnabled = true
        bottomNavigationView.menu.findItem(R.id.nav_responses).isEnabled = true
        bottomNavigationView.menu.findItem(R.id.nav_messages).isEnabled = true
        bottomNavigationView.menu.findItem(R.id.nav_profile).isEnabled = true
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HHAppTheme {
        Greeting("Android")
    }
}