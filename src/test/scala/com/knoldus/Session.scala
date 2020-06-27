package com.knoldus

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Session extends Simulation{

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/")

  val scene = scenario("Session")
    .exec(http("fake api")
    .get("users?page=2")
    .check(jsonPath("$.data..email").findAll.saveAs("email")))
      .exec(session =>
      {
        val attribute = session("email").as[Vector[String]]
        val first = attribute(0)
        val second = attribute(1)

        session.set("First",first).set("Second",second)
        println(first)
        println(second)
        //println(attribute.as[String])
        //println(attribute.validate[String])
        session
      })

  setUp(scene.inject(atOnceUsers(1)).protocols(httpProtocol))
}
