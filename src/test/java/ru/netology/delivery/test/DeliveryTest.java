package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.delivery.data.DataGenerator.Registration.generateUser;
import static ru.netology.delivery.data.DataGenerator.generateName;
import static ru.netology.delivery.data.DataGenerator.generatePhone;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest(){
        var daysToAddForFirstMeeting = 5;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        //form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$(".form-field .button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!")).shouldBe(Condition.visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
    }
    @Test
    void shouldSubmitRequestWhithDashedName(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue("Петр Иванов-Петров");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$(".form-field .button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!")).shouldBe(Condition.visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$(".form-field .button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!")).shouldBe(Condition.visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(Condition.visible);
        $("[data-test-id=success-notification] .notification__closer").click();
        //sleep(1000);
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(secondMeetingDate);
        $$(".form-field .button").find(exactText("Запланировать")).click();
        $("[data-test-id=replan-notification] .notification__title").shouldHave(exactText("Необходимо подтверждение")).shouldBe(Condition.visible);
        $("[data-test-id=replan-notification] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$("[data-test-id=replan-notification] button .button__text").find(exactText("Перепланировать")).click();
        $("[data-test-id=success-notification] .notification__title").shouldHave(exactText("Успешно!")).shouldBe(Condition.visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(Condition.visible);
    }

    @Test
    void emptyCityField(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void notDictionaryCity(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Клин");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }
    @Test
    void setEmptyDateDelivery(){
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }
    @Test
    void setErrorDateDelivery(){
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue("19.08.2022");
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }
    @Test
    void setEmptyName(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    @Test
    void setNotRussianName(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(generateName("en"));
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void setNameWithSpacialChars(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue("иван% иванов");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
    @Test
    void setEmptyPhone(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
    /*@Test
    //в этой версии приложения отсутствует проверка на корректность телефона
    void setErrorPhoneFormat(){
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue("1234567");
        form.$("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }*/
    @Test
    void notCheckAgreements(){
        var daysToAddForFirstMeeting = 8;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        DataGenerator.UserInfo validUser = generateUser("ru");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
    @Test
    void notFilled(){
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
