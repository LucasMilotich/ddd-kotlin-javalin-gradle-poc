package usecase

class HelloWorldUseCase {
    fun sayHi(): Map<String,String> {
        return hashMapOf<String, String>("hello" to " world")
    }
}