package com.knoldus
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import org.slf4j.LoggerFactory

class SSE extends Simulation{

  val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("http://demo.howopensource.com/")
// we have used HTTP since SSE also works over it

  val scene = scenario("sse_demo")
    .exec(sse("sse_req").connect("sse/stocks.php")
    .await(50 )(sse.checkMessage("check_connect")
      .check(regex(".*.*").saveAs("myresponse"))))
    // custom check for checking the response
    .exec(session =>{
      // created the session for printing the message received and type-casted it to String
      val a = session("myresponse").as[String]
      print(a)
      session})
      .pause(5)
      .exec(sse("sse_close").close())
//terminating the current connection

  setUp(scene.inject(atOnceUsers(1)).protocols(httpProtocol))
// injecting the user profile to simulate the scenario
}
