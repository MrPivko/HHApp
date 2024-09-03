package com.test.hhapp

data class Offer(
    val id: String,
    val title: String,
    val button: ButtonData,
    val link: String
)

data class ButtonData(
    val text: String
)

