package com.inno.trainee
package server

import exception.InvalidUriException
import model.CountryPeriod
import route.ServerRoutes
import server.HttpServer.client
import uri.UriMapping

import cats.data.Kleisli
import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import java.time.LocalDateTime

object HttpServer extends IOApp {

  private val serviceBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO]
    .bindHttp(8083, "localhost")
    .withHttpApp(ServerRoutes.service)

  val client: Resource[IO, Client[IO]] =
    BlazeClientBuilder[IO](scala.concurrent.ExecutionContext.global).resource

  def run(args: List[String]): IO[ExitCode] = {
    serviceBuilder.resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

  def getCountryStats(countryPeriod: CountryPeriod): IO[Option[String]] = {

    val uri = Uri.fromString(
      s"${UriMapping.remoteApiCountryStatsUri}/${countryPeriod.countryName}/" +
        s"status/confirmed?from=${countryPeriod.dateFrom}Z&to=${countryPeriod.dateTo}Z"
    )

    try {
      val request: Request[IO] =
        Request[IO](
          Method.GET,
          uri.getOrElse(throw new InvalidUriException("Invalid URI"))
        )
        
      client.use { client =>
        client.run(request).use { response =>
          response.bodyText.compile.string.map(Option.apply)
        }
      }
    } catch {
      case _: InvalidUriException => IO.pure(None)
    }

  }

}
