package com.inno.trainee
package uri

import org.http4s.Uri
import org.http4s.implicits.uri

object UriMapping {
  private val covid19WebApiServiceUri = "https://api.covid19api.com/"
  val remoteApiCountryStatsUri: String = covid19WebApiServiceUri + "country"
  val remoteApiWorldStatsUri: String = covid19WebApiServiceUri + "world"
  val worldStatsUri:String = "world"
  val countryStatsUri:String = "country"
}
