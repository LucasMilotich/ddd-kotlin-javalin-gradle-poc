package javalin.example.hello.controller

import controller.HelloWorldDelivery
import io.javalin.http.Context
import io.javalin.http.Handler

class HelloWorldController(private val helloWorldDelivery: HelloWorldDelivery) : Handler {
    override fun handle(ctx: Context) {
        ctx
                .json(helloWorldDelivery.deliver())
                .status(200)
    }
}