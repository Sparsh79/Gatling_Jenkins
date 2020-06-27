package com.knoldus

import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.slf4j.LoggerFactory

class Course extends Simulation {

  val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://api.openbrewerydb.org")
  val state = csv("/home/knoldus/GatlingCourse/src/test/resources/data/state.csv").circular
  val scn = scenario("Course")
    .feed(state)
    .exec(http("Course Request")
    //.get("/breweries?by_state=$(state)")
      .get("/breweries")
    .check(bodyString.saveAs("my Response")))
      .exec{session =>
        val a = session("my Response").as[String]
  print(a)
  session}

setUp(scn.inject(atOnceUsers(4)).protocols(httpProtocol))
}
