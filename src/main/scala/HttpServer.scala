package com.inno.trainee

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import route.ServerRoutes
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder

object HttpServer extends IOApp {

  private val serviceBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO]
    .bindHttp(8083, "localhost")
    .withHttpApp(ServerRoutes.service)

  def run(args: List[String]): IO[ExitCode] = {
    serviceBuilder.resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
