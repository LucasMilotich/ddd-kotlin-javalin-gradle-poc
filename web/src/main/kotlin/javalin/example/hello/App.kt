/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package javalin.example.hello

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.plugin.metrics.MicrometerPlugin
import io.prometheus.client.exporter.common.TextFormat
import javalin.example.hello.config.Config
import javalin.example.hello.controller.HelloWorldController
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.koin.core.context.GlobalContext.get


fun main() {

    Config.configDependencies()
    Config.configJackson()
    val registry = Config.configMetrics()

    val app = Javalin
            .create { config ->
                config.registerPlugin(MicrometerPlugin(registry))
                config.showJavalinBanner = false
                config.enableDevLogging()
            }.apply {
                Server(QueuedThreadPool(200,8,120_200)).apply {
                    connectors = arrayOf(ServerConnector(server).apply {
                        port = 7000
                        idleTimeout = 120_000
                    })
                }

            }

            .start()



    app.get("/") { ctx ->
        get().get(HelloWorldController::class).handle(ctx)
    }
    app.get("/metrics") { ctx: Context -> ctx.contentType(TextFormat.CONTENT_TYPE_004).result(registry.scrape()) }


}


