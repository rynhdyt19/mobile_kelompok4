package com.example.final_mobile_klp4.data.forecastModels

import com.example.final_mobile.data_klp4.forecastModels.Sys

data class ForecastData(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)