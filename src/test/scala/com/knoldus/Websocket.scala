package com.knoldus

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Websocket extends Simulation {

  //configuration
  //"http://demos.kaazing.com"
  //ws://demos.kaazing.com/echo?.k1=Y
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://demos.kaazing.com").wsBaseUrl("wss://echo.websocket.org")

  val scene = scenario("testWebSocket")
    .exec(http("firstRequest")
    .get("/"))
    .exec(ws("openSocket").connect("/echo")
      .onConnected(exec(ws("sendMessage").sendText("knoldus")
        .await(20)(ws.checkTextMessage("check1").check(regex(".*knoldus.*")
.saveAs("myMessage"))))))
    .exec(session => session{
      println(session("myMessage").as[String])
      session
    })


    .exec(ws("closeConnection").close)

  setUp(scene.inject(atOnceUsers(1)).protocols(httpProtocol))


}
