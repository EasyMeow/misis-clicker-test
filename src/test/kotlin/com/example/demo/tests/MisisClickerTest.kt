package com.example.demo.tests

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class MisisClickerTest {


    lateinit var driver: WebDriver
    val homeUrl = "http://isemi.ru/"
    val url = "https://docs.google.com/spreadsheets/u/1/d/1NlUU1ulotC5Kjiz-ctVhzwyAcyEWbK2ZY5eA4Z2PKoQ/htmlview"

    @BeforeAll
    fun setupAll() {
        WebDriverManager.chromedriver().setup()
    }

    @BeforeEach
    fun setup() {
        driver = ChromeDriver()
    }

    @AfterEach
    fun teardown() {
        driver.quit()
    }

    @Test
    fun test() {
        // Open "http://isemi.ru/"
        driver.get(homeUrl)
        Thread.sleep(5000)

        val wait = WebDriverWait(driver, Duration.ofSeconds(10))
        val  buttonElement = driver.findElement(By.xpath("//*[@id=\"clicker-button\"]")) // find click button
        wait.until(ExpectedConditions.visibilityOf(buttonElement)) // wait until element wil be present (page will be loaded)


        val inputElement = driver.findElement(By.xpath("//*[@id=\"clicker-input\"]")) // Find input element
        inputElement.sendKeys(url) // fill input element with big link
        buttonElement.click() // click on button
        Thread.sleep(2000)

        val shortURLElement = driver.findElement(By.xpath("//*[@id=\"short-url\"]"))
        wait.until(ExpectedConditions.visibilityOf(shortURLElement)) // wait until span with short link will apeared
        val shortUrl = shortURLElement.text; // copy short url

        shortURLElement.click() // click to short url and navigate

        Assertions.assertEquals(driver.currentUrl, url); // check if current url equal to long url
        Thread.sleep(2000)

        driver.navigate().to(homeUrl) // return back to "http://isemi.ru/"
        Thread.sleep(2000)

        driver.navigate().to(shortUrl) // navigate using short url with browser input
        Thread.sleep(2000)
        Assertions.assertEquals(driver.currentUrl, url); // check if current url equal to long url
    }
}