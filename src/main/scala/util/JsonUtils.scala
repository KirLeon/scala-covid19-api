package com.inno.trainee
package util

import model.{CountryPeriod, CovidStats, WorldStats}

import cats.effect.IO
import org.http4s.headers.`Content-Type`
import org.http4s.{EntityEncoder, MediaType}
import org.json4s.*
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods.*
import org.json4s.jackson.Serialization.write

import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit

object JsonUtils {

  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val mapEncoder: EntityEncoder[IO, Map[String, String]] =
    EntityEncoder
      .stringEncoder[IO]
      .contramap[Map[String, String]] { map =>
        Json(DefaultFormats).write(map)
      }
      .withContentType(`Content-Type`(MediaType.application.json))

  def parseCountryPeriod(body: String): Option[CountryPeriod] =
    try {
      val jsonBody: JValue = parse(body)
      val jsonMap = jsonBody.extract[Map[String, String]]

      val countryName = jsonMap("countryName")
      val dateFrom = LocalDateTime.parse(jsonMap("dateFrom")).minusDays(1)
      val dateTo = LocalDateTime.parse(jsonMap("dateTo"))

      if (dateFrom.isBefore(dateTo)) {
        Some(CountryPeriod(countryName, dateFrom, dateTo))
      }
      else {
        None
      }
    } catch {
      case _: NoSuchElementException | _: DateTimeParseException =>
        None
    }
  def parseCovidStats(body: String): Option[CovidStats] = {
    try {
      val jsonBody: JValue = parse(body)
      val jsonMap = jsonBody.extract[Map[String, String]]

      Some(
        CovidStats(
          jsonMap("Country"),
          jsonMap("CountryCode"),
          jsonMap("Province"),
          jsonMap("City"),
          jsonMap("CityCode"),
          jsonMap("Lat"),
          jsonMap("Lon"),
          jsonMap("Cases").toInt,
          jsonMap("Status"),
          jsonMap("Date")
        )
      )
    } catch {
      case _: Throwable => None
    }
  }

  def parseWorldStatsList(body: String): List[WorldStats] = {

    try {
      val jsonBody = parse(body)
      val jsonList = jsonBody.extract[List[Map[String, String]]]

      jsonList.map(jsonMap =>
        WorldStats(
          jsonMap("NewConfirmed").toInt,
          jsonMap("TotalConfirmed").toInt,
          jsonMap("NewDeaths").toInt,
          jsonMap("TotalDeaths").toInt,
          jsonMap("NewRecovered").toInt,
          jsonMap("TotalRecovered").toInt,
          jsonMap("Date")
        )
      )
    } catch {
      case _: Throwable => List.empty[WorldStats]
    }
  }

  def parseCovidStatsList(body: String): List[CovidStats] = {
    try {
      val jsonBody = parse(body)
      val jsonList = jsonBody.extract[List[Map[String, String]]]

      jsonList.map(jsonMap =>
        CovidStats(
          jsonMap("Country"),
          jsonMap("CountryCode"),
          jsonMap("Province"),
          jsonMap("City"),
          jsonMap("CityCode"),
          jsonMap("Lat"),
          jsonMap("Lon"),
          jsonMap("Cases").toInt,
          jsonMap("Status"),
          jsonMap("Date")
        )
      )
    } catch {
      case _: Throwable => List.empty[CovidStats]
    }
  }
}
