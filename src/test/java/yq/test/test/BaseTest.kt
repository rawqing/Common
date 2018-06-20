package yq.test.test

import org.testng.annotations.BeforeSuite
import yq.test.constant.ConfSet
import yq.test.constant.Environment
import yq.test.parameters.annotations.RunTimeEnv

open class BaseTest {
    @RunTimeEnv
    val env: Environment = EnvTest.getE()

    @BeforeSuite
    fun bs(){
        ConfSet.ymlRootDir = "hello"
    }
}