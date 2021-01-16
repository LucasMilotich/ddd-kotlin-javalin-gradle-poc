package javalin.example.hello.config

import com.fasterxml.jackson.databind.ObjectMapper
import controller.HelloWorldDelivery
import controller.HelloWorldDeliveryImpl
import io.javalin.plugin.json.JavalinJackson
import io.micrometer.core.instrument.binder.jvm.*
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import javalin.example.hello.controller.HelloWorldController
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import usecase.HelloWorldUseCase
import java.io.File

object Config {
     fun configDependencies() {
        // just declare it
        val myModule = module {
            single { HelloWorldController(get()) }
            single { HelloWorldDeliveryImpl(get()) } bind HelloWorldDelivery::class
            single { HelloWorldUseCase() }
        }

        startKoin {
            modules(myModule)
        }
    }

     fun configMetrics(): PrometheusMeterRegistry {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        registry.config().namingConvention()
        // add a tag to all reported values to simplify filtering in large installations:
        registry.config().commonTags("application", "My-Application")

        ClassLoaderMetrics().bindTo(registry)
        JvmMemoryMetrics().bindTo(registry)
        JvmGcMetrics().bindTo(registry)
        JvmThreadMetrics().bindTo(registry)
        UptimeMetrics().bindTo(registry)
        ProcessorMetrics().bindTo(registry)
        DiskSpaceMetrics(File(System.getProperty("user.dir"))).bindTo(registry)
        return registry
    }

     fun configJackson() {
        val objectMapper = ObjectMapper()
        JavalinJackson.configure(objectMapper)
    }
}