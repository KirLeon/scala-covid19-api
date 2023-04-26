package com.inno.trainee
package model

case class CovidStats(
    country: String,
    countryCode: String,
    province: String,
    city: String,
    cityCode: String,
    lat: String,
    lon: String,
    cases: Int,
    status: String,
    date: String
)
