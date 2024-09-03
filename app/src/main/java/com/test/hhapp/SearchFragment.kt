package com.test.hhapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextButton
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import java.io.InputStreamReader
import com.google.gson.Gson
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        lifecycleScope.launch {
            try {
                val json = InputStreamReader(requireContext().assets.open("мок.json")).readText()
                val responseData = Gson().fromJson(json, ResponseData::class.java)

                val offers = responseData.offers
                val vacancies = responseData.vacancies

                viewModel.loadOffers(offers) // Загрузка офферов в ViewModel
                viewModel.loadVacancies(vacancies) // Загрузка вакансий в ViewModel

            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

            val composeView = view.findViewById<ComposeView>(R.id.composeView)
            composeView.setContent {
                val offersState by viewModel.offers.collectAsState()
                val vacanciesState by viewModel.vacancies.collectAsState()

                SearchScreenContent(
                    offers = offersState,
                    vacancies = vacanciesState,
                    onLoadMoreVacancies = {
                        viewModel.loadMoreVacancies()
                    }
                )
            }
        }
    }
}

@Composable
fun OfferCard(offer: Offer) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(180.dp)  // Задаем фиксированную ширину карточки
            .height(120.dp) // Задаем фиксированную высоту карточки
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Отображение заголовка, если он существует
            offer.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Отображение кнопки-текста, если она существует
            offer.button?.text?.let { buttonText ->
                TextButton(onClick = { /* Обработка нажатия */ }) {
                    Text(text = buttonText, color = Color.Blue)
                }
            }
        }
    }
}

@Composable
fun OffersSection(offers: List<Offer>) {
    if (offers.isNotEmpty()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(offers) { offer ->
                OfferCard(offer = offer)
            }
        }
    } else {
        // Добавьте заглушку или ничего не делайте
    }
}

@Composable
fun VacancyCard(vacancy: Vacancy) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Отображаем число людей, просматривающих вакансию
        Text(
            text = "Сейчас просматривает ${vacancy.lookingNumber} ${pluralize(vacancy.lookingNumber ?: 0)}",
            color = Color.Green,
            fontWeight = FontWeight.Bold
        )

        // Кнопка "Избранное"
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorites),
                contentDescription = "Избранное",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* Обработка добавления в избранное */ }
            )
        }

        // Название вакансии
        Text(
            text = vacancy.title ?: "Без названия",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Зарплата
        Text(
            text = vacancy.salary?.short ?: "Не указана",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Город
        Text(
            text = vacancy.address?.town ?: "Не указан",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Название компании
        Text(
            text = vacancy.company ?: "Не указана",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Предварительный текст опыта
        Text(
            text = vacancy.experience?.previewText ?: "Не указан",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Дата публикации
        Text(
            text = vacancy.publishedDate?.let { "Опубликовано: ${formatDate(it)}" } ?: "Дата не указана",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


// Пример функции для форматирования даты
fun formatDate(date: String): String {
    // Логика форматирования даты
    return date
}

// Пример функции для склонения слова "вакансия"
fun pluralize(number: Int): String {
    return when {
        number % 10 == 1 && number % 100 != 11 -> "вакансия"
        number % 10 in 2..4 && (number % 100 !in 12..14) -> "вакансии"
        else -> "вакансий"
    }
}



@Composable
fun VacanciesSection(
    vacancies: List<Vacancy>,
    onLoadMore: () -> Unit
) {
    Column {
        // Отображаем вакансии
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(vacancies.take(3)) { vacancy ->
                VacancyCard(vacancy = vacancy)
            }
        }
    }
}

@Composable
fun SearchScreenContent(
    offers: List<Offer>,
    vacancies: List<Vacancy>,
    onLoadMoreVacancies: () -> Unit
) {
    Column {
        OffersSection(offers = offers)

        // Вакансии
        VacanciesSection(
            vacancies = vacancies,
            onLoadMore = onLoadMoreVacancies
        )

        // Всегда показываем кнопку "Ещё вакансии"
        Button(
            onClick = onLoadMoreVacancies,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Ещё вакансии", color = Color.White)
        }
    }
}

