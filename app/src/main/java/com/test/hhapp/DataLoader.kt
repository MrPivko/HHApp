package com.test.hhapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

data class ResponseData(
    val offers: List<Offer>,
    val vacancies: List<Vacancy>
)

fun loadJsonFromAssets(context: Context, fileName: String): ResponseData {
    val inputStream = context.assets.open(fileName)
    val reader = InputStreamReader(inputStream)
    val type = object : TypeToken<ResponseData>() {}.type
    return Gson().fromJson(reader, type)
}

data class Vacancy(
    val id: String?,
    val title: String?,
    val address: Address?,
    val company: String?,
    val experience: Experience?,
    val publishedDate: String?,
    val isFavorite: Boolean,
    val salary: Salary?,
    val lookingNumber: Int?
)

data class Address(
    val town: String?,
    val street: String?,
    val house: String?
)

data class Experience(
    val previewText: String?,
    val text: String?
)

data class Salary(
    val full: String?,
    val short: String?
)