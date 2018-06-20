package yq.test.test

import org.testng.annotations.Listeners
import org.testng.annotations.Test
import yq.test.constant.Environment
import yq.test.parameters.annotations.YamlParams

@Listeners(yq.test.listener.ParamsListener::class)
class TestRunTime :BaseTest(){

    @Test
    @YamlParams
    fun tt(){
        println("hello")
        println(env)
    }
}