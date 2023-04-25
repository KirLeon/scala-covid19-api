package com.inno.trainee
package route

import cats.data.Kleisli
import cats.effect.IO
import util.JsonUtils

import com.inno.trainee.uri.UriMapping
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.headers.`Content-Type`
import org.http4s.MediaType
import org.json4s.DefaultFormats
import org.json4s.jackson.Json
import org.json4s.jackson.JsonMethods.*

object ServerRoutes {

  implicit val entityEncoder: EntityEncoder[IO, Map[String, String]] = JsonUtils.mapEncoder
  implicit val formats: DefaultFormats.type = DefaultFormats

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / UriMapping.getCountriesUri =>
        val responseBody = Map("message" -> "Hello, World with Get!")
        Ok(responseBody).map {
          _.withContentType(`Content-Type`(MediaType.application.json))
        }
      case POST -> Root / UriMapping.`worldStatsUri` =>
        val responseBody = Map("message" -> "Hello, World with Post!")
        Ok(responseBody).map {
          _.withContentType(`Content-Type`(MediaType.application.json))
        }
      case POST -> Root / UriMapping.`countryStatsUri` =>
        val responseBody = Map("message" -> "Hello, Country with Post!")
        Ok(responseBody).map {
          _.withContentType(`Content-Type`(MediaType.application.json))
        }
    }
    .orNotFound
}