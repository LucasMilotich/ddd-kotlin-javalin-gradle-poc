package controller

import usecase.HelloWorldUseCase

class HelloWorldDeliveryImpl(private val helloWorldUseCase: HelloWorldUseCase) : HelloWorldDelivery {
    override fun deliver(): Map<String, String> {
        return helloWorldUseCase.sayHi()
    }
}