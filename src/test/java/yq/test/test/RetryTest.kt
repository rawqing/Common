package yq.test.test

import org.testng.annotations.Listeners
import org.testng.annotations.Test
import yq.test.exception.AcceptableException

//@Listeners(yq.test.listener.ParamsListener::class)
class RetryTest {

    @Test
    fun retryTest(){
        println("hello")
        throw AcceptableException("can acceptable.")
    }
}