package com.test.hhapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers: StateFlow<List<Offer>> = _offers

    private val _vacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val vacancies: StateFlow<List<Vacancy>> = _vacancies

    fun loadOffers(offers: List<Offer>) {
        _offers.value = offers
    }

    fun loadVacancies(vacancies: List<Vacancy>) {
        _vacancies.value = vacancies
    }

    fun loadMoreVacancies() {
        // Логика загрузки дополнительных вакансий
        // Например, можете сделать дополнительный запрос или добавить больше данных
    }
}

