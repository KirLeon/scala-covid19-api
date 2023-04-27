package com.inno.trainee
package service

import model.{CountryPeriod, CovidStats, WorldStats}
import server.HttpServer
import util.JsonUtils

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.syntax.header.http4sHeaderSyntax
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods.parse
import org.json4s.{
  DefaultFormats,
  JArray,
  JNothing,
  JObject,
  JString,
  jvalue2extractable,
  jvalue2monadic
}

object CovidCasesService {

  implicit val formats: DefaultFormats.type = DefaultFormats
  def getCountryStats(
      countryPeriod: CountryPeriod
  ): Option[Map[String, Int]] = {

    if (countryPeriod.countryName.isEmpty) {
      return None
    }

    val covid19apiResponse = HttpServer
      .getCountryStats(countryPeriod)
      .flatMap {
        case None      => IO.pure("")
        case Some(str) => IO.pure(str)
      }

    if (parseServerRequest(covid19apiResponse)) {
      val covidStats =
        JsonUtils.parseCovidStatsList(covid19apiResponse.unsafeRunSync())
      Some(getCountryDateCasesList(covidStats))
    } else {
      None
    }
  }

  def getWorldStats(worldPeriod: CountryPeriod): Option[Map[String, Int]] = {
    if (
      worldPeriod.countryName.isEmpty || worldPeriod.countryName.isBlank ||
      worldPeriod.countryName.toLowerCase().contains("world")
    ) {

      val covid19apiResponse = HttpServer
        .getWorldStats(worldPeriod)
        .flatMap {
          case None      => IO.pure("")
          case Some(str) => IO.pure(str)
        }

      if (parseServerRequest(covid19apiResponse)) {
        val worldStats =
          JsonUtils.parseWorldStatsList(covid19apiResponse.unsafeRunSync())
        Some(getWorldDateCasesList(worldStats))
      } else {
        None
      }
    } else {
      None
    }
  }

  private def parseServerRequest(response: IO[String]): Boolean = {

    if (response.unsafeRunSync().isEmpty) {
      return false
    }

    val jsonResponse: String = response.unsafeRunSync()
    val json = parse(jsonResponse)

    json match {
      case JNothing                   => false // пустой JSON-ответ
      case JString("")                => false // пустая строка
      case JArray(arr) if arr.isEmpty => false // пустой массив
      case JObject(fields)
          if fields
            .find(_._1 == "message")
            .exists(
              _._2
                .extract[String]
                .equalsIgnoreCase("Not Found")
            ) =>
        false
      case _ =>
        true
    }
  }

  private def getCountryDateCasesList(
      stats: List[CovidStats]
  ): Map[String, Int] = {
    stats
      .map(stat => (stat.date.split("T")(0), stat.cases))
      .drop(1)
      .zip(stats.map(_.cases))
      .map { case ((date, currCases), prevCases) =>
        (date, currCases - prevCases)
      }
      .sortBy { case (date, _) => date }
      .toMap
  }

  private def getWorldDateCasesList(
      stats: List[WorldStats]
  ): Map[String, Int] = {
    stats
      .map(stat => (stat.date.split("T")(0), stat.newConfirmed))
      .drop(1)
      .sortBy { case (date, _) => date }
      .toMap
  }
}
