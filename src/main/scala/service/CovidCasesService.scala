package com.inno.trainee
package service

import model.{CountryPeriod, CovidStats}
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

    val covid19apiResponse = HttpServer
      .getCountryStats(countryPeriod).flatMap {
      case None => IO.pure("")
      case Some(str) => IO.pure(str)
    }

    if (covid19apiResponse.unsafeRunSync().isEmpty) {
      return None
    }

    val jsonResponse: String = covid19apiResponse.unsafeRunSync()
    val json = parse(jsonResponse)

    json match {
      case JNothing                   => None // пустой JSON-ответ
      case JString("")                => None // пустая строка
      case JArray(arr) if arr.isEmpty => None // пустой массив
      case JObject(fields)
          if fields
            .find(_._1 == "message")
            .exists(
              _._2
                .extract[String]
                .equalsIgnoreCase("Not Found")
            ) =>
        None // JSON-ответ с полем "message" со значением "Not Found"
      case _ =>
        val covidStats = JsonUtils.parseCovidStatsList(jsonResponse)
        Some(getListOfDateAndCases(covidStats))
    }

  }

  private def getListOfDateAndCases(
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
  
}
