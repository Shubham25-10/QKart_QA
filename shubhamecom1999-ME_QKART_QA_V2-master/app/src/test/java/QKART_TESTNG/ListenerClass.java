package QKART_TESTNG;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener{
    @Override
    public void onTestStart(ITestResult result){
        //ITestListener.super.onTestStart(result);
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "OnTestStart", result.getName());
    }
    @Override
    public void onTestSuccess(ITestResult result){
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestSuccess", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result){
        //ITestListener.super.onTestFailure(result);
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestFailure", result.getName());
    }

}