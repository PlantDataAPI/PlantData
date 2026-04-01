package org.testing.PlantDataModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testing.DriverClass.Drivers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;

public class IronAndPacking {
    public static void getIronAndPackingData()
    {
        WebDriver driver = Drivers.openChromeBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        try {
            driver.get("https://subs3.quickdrycleaning.com/Login");
            String userId = System.getenv("USER_ID");
            String userPass = System.getenv("USER_PASS");
            String userCode = System.getenv("USER_CODE");

            WebElement popUpField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Button3")));
            ButtonClickClass.clickButton(driver,popUpField);

            WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtUserId")));
            userField.clear();
            userField.sendKeys(userId);

            WebElement passField = driver.findElement(By.id("txtPassword"));
            passField.clear();
            passField.sendKeys(userPass);

            WebElement codeField = driver.findElement(By.id("txtBranchPin"));
            codeField.clear();
            codeField.sendKeys(userCode);

            WebElement loginBtn = driver.findElement(By.id("btnLogin"));
            ButtonClickClass.clickButton(driver,loginBtn);
            WebElement processButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='card card-sidebar-mobile']/ul/li[3]")));
            ButtonClickClass.clickButton(driver,processButton);
            WebElement ironAndPackingButton = driver.findElement(By.xpath("//div[@class='card card-sidebar-mobile']/ul/li[3]/ul/li[last()-1]/a"));
            ButtonClickClass.clickButton(driver, ironAndPackingButton);
            String currentDateValue = FilterCalendarUser.getCalendarAsPerRequirement(wait, driver, 2);
            System.out.println("Iron and Packing Start Date Value is "+currentDateValue);

            List<Map<String, String>> processingDataJsonList = new ArrayList<>();
            List<String> allowedHeaders = Arrays.asList(
                    "Order",
                    "Due Date",
                    "Barcode",
                    "Customer",
                    "Garment",
                    "Service",
                    "Sent from store"
            );
            WebElement leftTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("grdLeftData")));
            List<WebElement> headerElements = leftTable.findElements(By.xpath(".//tr[1]/th[position() > 1]"));
            Map<Integer, String> colMap = new LinkedHashMap<>();
            for (int i = 0; i < headerElements.size(); i++) {
                String headerText = headerElements.get(i).getText().trim();
                if (allowedHeaders.contains(headerText)) {
                    colMap.put(i, headerText);
                }
            }
            List<WebElement> rows = leftTable.findElements(By.xpath(".//tbody/tr"));
            for(WebElement row : rows)
            {
                List<WebElement> cells = row.findElements(By.xpath(".//td[position() > 1]"));
                if (cells.size() < colMap.size()) continue;
                Map<String, String> jsonObject = new LinkedHashMap<>();
                for (Map.Entry<Integer, String> entry : colMap.entrySet()) {
                    int colIndex = entry.getKey();
                    if (colIndex < cells.size()) {
                        String headerName = entry.getValue();
                        String cellValue = cells.get(colIndex).getAttribute("textContent").replaceAll("\\u00A0", " ").trim();
                        jsonObject.put(headerName, cellValue);
                    }
                }
                processingDataJsonList.add(jsonObject);
            }
			System.out.println("Iron and Packing total data count is "+processingDataJsonList.size());
            WebElement dropDownForLogoutBtn = driver.findElement(By.xpath("//ul[@class='navbar-nav']/li[2]"));
            ButtonClickClass.clickButton(driver,dropDownForLogoutBtn);
            WebElement logoutBtn = driver.findElement(By.xpath("//ul[@class='navbar-nav']/li[2]/div/a[@id='btnLogOut']"));
            ButtonClickClass.clickButton(driver,logoutBtn);

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processingDataJsonList);
            Path path = Paths.get("src/main/resources/Output/PlantDataApi/ironAndPackingApi.json");
            Files.createDirectories(path.getParent());
            Files.write(path, jsonString.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.out.println("Runtime error Occurred"+e);
        }
        finally {
            driver.quit();
        }
    }
}
