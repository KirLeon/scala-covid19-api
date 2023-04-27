package com.inno.trainee
package route

import model.CountryPeriod
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

import scala.language.postfixOps

object ServerRoutes {

  implicit val entityEncoder: EntityEncoder[IO, Map[String, String]] =
    JsonUtils.mapEncoder

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {

      case req@POST -> Root / UriMapping.countryStatsUri =>
        req.as[String].flatMap { body =>

          val jsonBody = parse(body)
          val inputCountryPeriod = JsonUtils.parseCountryPeriod(body)

          inputCountryPeriod match

            case Some(countryPeriod) =>

              val stats = CovidCasesService.getCountryStats(countryPeriod)
              stats match {
                case Some(data) if data.nonEmpty =>
                  Ok(data.toString()).map(_.withContentType(`Content-Type`(MediaType.application.json)))
                case _ =>
                  BadRequest("Invalid data").map(_.withContentType(`Content-Type`(MediaType.text.plain)))
              }

            case None =>
              val response = s"Invalid data: $jsonBody found"
              BadRequest(response).map(_.withContentType(`Content-Type`(MediaType.application.json)))
        }

      case req@POST -> Root / UriMapping.worldStatsUri =>
        req.as[String].flatMap { body =>

          val jsonBody = parse(body)
          val worldPeriod = JsonUtils.parseCountryPeriod(body)

          worldPeriod match

            case Some(worldStats) =>

              val stats = CovidCasesService.getWorldStats(worldStats)
              stats match {
                case Some(data) if data.nonEmpty =>
                  Ok(data.toString()).map(_.withContentType(`Content-Type`(MediaType.application.json)))
                case _ =>
                  BadRequest("Invalid data").map(_.withContentType(`Content-Type`(MediaType.text.plain)))
              }

            case None =>
              val response = s"Invalid data: $jsonBody found"
              BadRequest(response).map(_.withContentType(`Content-Type`(MediaType.application.json)))

        }

    }
    .orNotFound
}
