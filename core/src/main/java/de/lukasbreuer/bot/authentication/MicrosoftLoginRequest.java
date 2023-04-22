package de.lukasbreuer.bot.authentication;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "create")
public final class MicrosoftLoginRequest implements AuthenticationRequest<String> {
  private final String email;
  private final String password;

  private static final String MICROSOFT_LOGIN_URL =
    "https://login.live.com/oauth20_authorize.srf?client_id=000000004C12AE6F" +
      "&redirect_uri=https://login.live.com/oauth20_desktop.srf&" +
      "scope=service::user.auth.xboxlive.com::MBI_SSL&display=touch&" +
      "response_type=token&locale=en";

  @Override
  public CompletableFuture<String> send() {
    var completableFuture = new CompletableFuture<String>();
    new Thread(() -> requestAccessToken(completableFuture)).start();
    return completableFuture;
  }

  private void requestAccessToken(CompletableFuture<String> completableFuture) {
    var driver = WebDriverManager.chromedriver();
    driver.setup();
    configureBrowser(driver);
    var browser = driver.create();
    browser.get(MICROSOFT_LOGIN_URL);
    enterEmail(browser);
    enterPassword(browser);
    completableFuture.complete(extractInformationOutOfBody(
      browser.getCurrentUrl(), "access_token=", '&'));
  }

  private void enterEmail(WebDriver browser) {
    browser.findElement(By.id("i0116")).sendKeys(email);
    browser.findElement(By.id("idSIButton9")).click();
    try {
      Thread.sleep(500);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private void enterPassword(WebDriver browser) {
    browser.findElement(By.id("i0118")).sendKeys(password);
    browser.findElement(By.id("idSIButton9")).click();
  }

  private String extractInformationOutOfBody(
    String body, String identifier, char breakingPoint
  ) {
    var startPosition = body.indexOf(identifier) + identifier.length();
    var currentPosition = startPosition;
    while (body.charAt(currentPosition) != breakingPoint) {
      currentPosition++;
    }
    return body.substring(startPosition, currentPosition);
  }

  private static final String BROWSER_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) " +
    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.5359.124 Safari/537.36";

  private void configureBrowser(WebDriverManager driver) {
    var options = new ChromeOptions();
    options.addArguments("--window-size=1920x1080");
    options.addArguments("--disable-extensions");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--no-sandbox");
    options.addArguments("--headless");
    options.addArguments("--user-agent=" + BROWSER_USER_AGENT);
    System.setProperty("webdriver.chrome.silentOutput", "true");
    driver.capabilities(options);
  }
}
