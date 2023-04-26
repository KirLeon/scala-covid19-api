package com.inno.trainee
package route

import service.CovidCasesService
import uri.UriMapping
import util.JsonUtils

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.headers.`Content-Type`
import org.http4s.implicits.*
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods.*
import org.json4s.{DefaultFormats, jvalue2extractable}

object ServerRoutes {

  implicit val entityEncoder: EntityEncoder[IO, Map[String, String]] =
    JsonUtils.mapEncoder

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {

      case req@POST -> Root / UriMapping.worldStatsUri =>

        val responseBody = Map("message" -> "Hello, World with Post!")
        Ok(responseBody).map {
          _.withContentType(`Content-Type`(MediaType.application.json))
        }

      case req@POST -> Root / UriMapping.countryStatsUri =>
        req.as[String].flatMap { body =>

          val jsonBody = parse(body)
          val inputCountryPeriod = JsonUtils.parseCountryPeriod(body)

          inputCountryPeriod match

            case Some(countryPeriod) =>
              val stats = CovidCasesService.getCountryStats(countryPeriod)
              stats match {
                case Some(data) if data.nonEmpty =>
                  val response = data.toString
                  Ok(response).map(_.withContentType(`Content-Type`(MediaType.application.json)))
                case _ =>
                  BadRequest("Invalid data").map(_.withContentType(`Content-Type`(MediaType.text.plain)))
              }

            case None =>
              val response = s"Incorrect input: $jsonBody found"
              Ok(response).map {
                _.withContentType(`Content-Type`(MediaType.application.json))
              }
        }
    }
    .orNotFound
}

//req.as[String].flatMap { body =>
//  val json = parse(body)
//  val map = json.extract[Map[String, String]]
//  Ok(map.toString)
//}