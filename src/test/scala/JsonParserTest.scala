package com.inno.trainee

import model.CovidStats
import util.JsonUtils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class JsonParserTest extends AnyFlatSpec with Matchers {

  val jsonOneDayStat1 =
    """{
        "Country": "Belarus",
        "CountryCode": "BY",
        "Province": "",
        "City": "",
        "CityCode": "",
        "Lat": "53.71",
        "Lon": "27.95",
        "Cases": 702374,
        "Status": "confirmed",
        "Date": "2022-01-04T00:00:00Z"
      }"""

  val jsonOneDayStat2 =
    """{
                  "Country": "South Africa",
                  "CountryCode": "ZA",
                  "Province": "",
                  "City": "",
                  "CityCode": "",
                  "Lat": "-30.56",
                  "Lon": "22.94",
                  "Cases": 0,
                  "Status": "recovered",
                  "Date": "2020-03-01T00:00:00Z"
              }"""

  val jsonOneDayStat3 =
    """{"Country": "Angola", "CountryCode": "AO",  "Province": "",  "City": "",  "CityCode": "",
       "Lat": "-11.2",  "Lon": "17.87",  "Cases": 0,  "Status": "deaths",
         "Date": "2020-03-01T00:00:00Z"
        }"""

  val jsonFewDaysInput =
    """[
      {
          "Country": "Belarus",
          "CountryCode": "BY",
          "Province": "",
          "City": "",
          "CityCode": "",
          "Lat": "53.71",
          "Lon": "27.95",
          "Cases": 700421,
          "Status": "confirmed",
          "Date": "2022-01-01T00:00:00Z"
      },
      {
          "Country": "Belarus",
          "CountryCode": "BY",
          "Province": "",
          "City": "",
          "CityCode": "",
          "Lat": "53.71",
          "Lon": "27.95",
          "Cases": 701192,
          "Status": "confirmed",
          "Date": "2022-01-02T00:00:00Z"
      },
      {
          "Country": "Belarus",
          "CountryCode": "BY",
          "Province": "",
          "City": "",
          "CityCode": "",
          "Lat": "53.71",
          "Lon": "27.95",
          "Cases": 701699,
          "Status": "confirmed",
          "Date": "2022-01-03T00:00:00Z"
      },
      {
          "Country": "Belarus",
          "CountryCode": "BY",
          "Province": "",
          "City": "",
          "CityCode": "",
          "Lat": "53.71",
          "Lon": "27.95",
          "Cases": 702374,
          "Status": "confirmed",
          "Date": "2022-01-04T00:00:00Z"
      }
  ]"""

  val oneDayStat1: CovidStats = CovidStats(
    "Belarus",
    "BY",
    "",
    "",
    "",
    "53.71",
    "27.95",
    702374,
    "confirmed",
    "2022-01-04T00:00:00Z"
  )
  val oneDayStat2: CovidStats = CovidStats(
    "South Africa",
    "ZA",
    "",
    "",
    "",
    "-30.56",
    "22.94",
    0,
    "recovered",
    "2020-03-01T00:00:00Z"
  )
  val oneDayStat3: CovidStats = CovidStats(
    "Angola",
    "AO",
    "",
    "",
    "",
    "-11.2",
    "17.87",
    0,
    "deaths",
    "2020-03-01T00:00:00Z"
  )

  val fewDaysStatDay1: CovidStats = CovidStats(
    "Belarus",
    "BY",
    "",
    "",
    "",
    "53.71",
    "27.95",
    700421,
    "confirmed",
    "2022-01-01T00:00:00Z"
  )
  val fewDaysStatDay2: CovidStats = CovidStats(
    "Belarus",
    "BY",
    "",
    "",
    "",
    "53.71",
    "27.95",
    701192,
    "confirmed",
    "2022-01-02T00:00:00Z"
  )
  val fewDaysStatDay3: CovidStats = CovidStats(
    "Belarus",
    "BY",
    "",
    "",
    "",
    "53.71",
    "27.95",
    701699,
    "confirmed",
    "2022-01-03T00:00:00Z"
  )
  val fewDaysStatDay4: CovidStats = CovidStats(
    "Belarus",
    "BY",
    "",
    "",
    "",
    "53.71",
    "27.95",
    702374,
    "confirmed",
    "2022-01-04T00:00:00Z"
  )
  
  val fewDaysList: List[CovidStats] = List(fewDaysStatDay1, fewDaysStatDay2, fewDaysStatDay3, fewDaysStatDay4)
  val parsedFewDaysStats: List[CovidStats] = JsonUtils.parseCovidStatsList(jsonFewDaysInput)

  val parsedStat1: CovidStats = JsonUtils.parseCovidStats(jsonOneDayStat1).orNull
  val parsedStat2: CovidStats = JsonUtils.parseCovidStats(jsonOneDayStat2).orNull
  val parsedStat3: CovidStats = JsonUtils.parseCovidStats(jsonOneDayStat3).orNull

  "Parsed stat in many lines without spaces" should "be the same" in {
    parsedStat1 shouldEqual oneDayStat1
  }

  "Parsed stat in many lines with spaces" should "be the same" in {
    parsedStat2 shouldEqual oneDayStat2
  }

  "Parsed stat in long line" should "be the same" in {
    parsedStat3 shouldEqual oneDayStat3
  }

  "Parsed stats of few days in JSON" should "be the same" in {
    parsedFewDaysStats shouldEqual fewDaysList
  }
}
