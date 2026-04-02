package org.testing.PlantDataModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FilterCalendarUser {

    public static String getCalendarAsPerRequirement(WebDriverWait wait, WebDriver driver, int rangeValue)
    {
        String rangeStringValue="";
        WebElement rangeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reportrange")));
        ButtonClickClass.clickButton(driver,rangeButton);
        WebElement selectPreviousMonth = driver.findElement(By.xpath("//div[@class='daterangepicker dropdown-menu ltr opensleft']/div[2]/ul/li[last()-5]"));
        ButtonClickClass.clickButton(driver,selectPreviousMonth);
        ButtonClickClass.clickButton(driver,rangeButton);
        WebElement selectCalendar = driver.findElement(By.xpath("//div[@class='daterangepicker dropdown-menu ltr opensleft']/div[2]/ul/li[last()]"));
        ButtonClickClass.clickButton(driver,selectCalendar);
        String currentDate = "";
        try
        {
            currentDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='today weekend available']")).getText();
        }
        catch (Exception e)
        {
            currentDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='today available']")).getText();
        }
        int rangeIntValue = Integer.parseInt(currentDate);
        if(rangeIntValue <= 3 )
        {
            String endMonthDate="";
            try
            {
                endMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[last()]/td[@class='active end-date in-range available']")).getText();
            }
            catch (Exception e) {
                try
                {
                    endMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[last()]/td[@class='end-date in-range available']")).getText();
                } catch (Exception ex) {
                    try
                    {
                        endMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[last()]/td[@class='in-range available']")).getText();
                    } catch (Exception exc) {
                        endMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[last()]/td[@class='weekend in-range available']")).getText();
                    }
                }
            }
            rangeIntValue += Integer.parseInt(endMonthDate);
            rangeIntValue = rangeIntValue - rangeValue;
            WebElement previousMonthDate;
            try
            {
                previousMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[1]/td[@class='weekend off available']"));
            } catch (Exception e) {
                previousMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr[1]/td[@class='off available']"));
            }
            System.out.println("Try previousMonthDate is "+previousMonthDate.getText());
            ButtonClickClass.clickButton(driver, previousMonthDate);
            WebElement currentMonthDate;
            if(rangeIntValue > Integer.parseInt(endMonthDate))
            {
                rangeIntValue -= Integer.parseInt(endMonthDate);
                rangeStringValue = String.valueOf(rangeIntValue);
                try {
                    currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='off available' and contains(text(),'" + rangeStringValue + "')]"));
                } catch (Exception e) {
                    currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='weekend off available' and contains(text(),'" + rangeStringValue + "')]"));
                }
                ButtonClickClass.clickButton(driver, currentMonthDate);
            }
            else
            {
                rangeStringValue = String.valueOf(rangeIntValue);
                try {
                    currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='available' and contains(text(),'" + rangeStringValue + "')]"));
                } catch (Exception e) {
                    currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='weekend available' and contains(text(),'" + rangeStringValue + "')]"));
                }
                ButtonClickClass.clickButton(driver, currentMonthDate);
            }

        }
        else
        {
            rangeIntValue -= rangeValue;
            rangeStringValue = String.valueOf(rangeIntValue);
            WebElement previousMonthDate = driver.findElement(By.xpath("//div[@class='calendar left']/div/table/tbody/tr/td[contains(text(),'" + rangeStringValue + "')]"));
            System.out.println("Catch previousMonthDate is "+previousMonthDate.getText());
            ButtonClickClass.clickButton(driver, previousMonthDate);
            WebElement currentMonthDate;
            try {
                currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='available' and contains(text(),'" + rangeStringValue + "')]"));
            } catch (Exception e) {
                currentMonthDate = driver.findElement(By.xpath("//div[@class='calendar right']/div/table/tbody/tr/td[@class='weekend available' and contains(text(),'" + rangeStringValue + "')]"));
            }
            ButtonClickClass.clickButton(driver, currentMonthDate);
        }

        WebElement submitButton = driver.findElement(By.xpath("//div[@class='ranges']/div[last()]/button[contains(text(),'Submit')]"));
        ButtonClickClass.clickButton(driver, submitButton);
        return rangeStringValue;
    }
}
