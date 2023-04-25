package com.inno.trainee
package util

import cats.effect.IO
import org.http4s.{EntityEncoder, MediaType}
import org.json4s.DefaultFormats
import org.json4s.jackson.Json
import org.http4s.headers.`Content-Type`

object JsonUtils {
  
  implicit val mapEncoder: EntityEncoder[IO, Map[String, String]] =
    EntityEncoder.stringEncoder[IO]
      .contramap[Map[String, String]] { map =>
        Json(DefaultFormats).write(map)
      }
      .withContentType(`Content-Type`(MediaType.application.json))
}
