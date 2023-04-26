package com.inno.trainee
package model

import util.JsonUtils

import org.json4s.jackson.Json

import java.time.LocalDateTime

case class CountryPeriod(
    countryName: String,
    dateFrom: LocalDateTime,
    dateTo: LocalDateTime
)

