package com.inno.trainee
package model

case class WorldStats(
    newConfirmed: Int,
    totalConfirmed: Int,
    newDeaths: Int,
    totalDeaths: Int,
    newRecovered: Int,
    totalRecovered: Int,
    date: String
)
