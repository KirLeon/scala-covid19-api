package com.inno.trainee
package route

import cats.data.Kleisli
import cats.effect.IO
import util.JsonUtils
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.json4s.jackson.Json
import org.http4s.EntityEncoder
import org.http4s.MediaType
import org.http4s.headers.`Content-Type`
import org.json4s.DefaultFormats
object ServerRoutes {

  implicit val entityEncoder: EntityEncoder[IO, Map[String, String]] = JsonUtils.mapEncoder

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "covid19" =>
        val responseBody = Map("message" -> "Hello, World!")
        Ok(responseBody).map {
          _.withContentType(`Content-Type`(MediaType.application.json))
        }
    }
    .orNotFound
}