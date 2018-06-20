package yq.test.listener.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import yq.test.exception.AcceptableException;

public class Retry implements IRetryAnalyzer {
    private Logger log = LoggerFactory.getLogger(Retry.class);
    private int retryCount = 1;
    private int maxRetryCount = 3;

    @Override
    public boolean retry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        if (throwable instanceof AcceptableException) {
            return false;
        }
        if (retryCount <= maxRetryCount) {
            log.info("Retrying test '{}' with status '{}' for the {} time(s)",
                    result.getName(),
                    statusName(result.getStatus()),
                    retryCount);
            retryCount++;
            return true;
        }
        return false;
    }

    private String statusName(int s) {
        switch (s) {
            case 1:
                return "SUCCESS";
            case 2:
                return "FAILURE";
            case 3:
                return "SKIP";
            case 4:
                return "SUCCESS_PERCENTAGE_FAILURE";
            case 16:
                return "STARTED";
            default:
                return "NONE";
        }
    }
}
