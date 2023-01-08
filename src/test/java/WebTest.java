import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import quru.qa.data.Locale;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @DisplayName("тупой тест на яндекспоиск")
    @Test
    void yandexSearchTest(){
        open("https://ya.ru/");
        $("#text").setValue("Selenide");
        $("button[type='submit']").click();
        $$("li.serp-item").shouldHave(CollectionCondition.size(10));
    }

    //Параметризованный тест - это такой тест, в котором мы имеем одинаковый сценарий, но разные тестовые данные

    //В этом тесте используем тестовые данные из массива
    //Тестовые данные: ["Selenide", "JUnit"]
    @ValueSource(strings = {"Selenide", "JUnit"}) //добавляем тестовые данные
    //Это аннотация позволяет добавлять тестовые данные ( работает для всех примитиных типов данных
    @ParameterizedTest(name = "Проверка числа в поиске яндекса {0}")
    // [test_data] == (String testData)
    void yandexSearchCommonTest(String testData){
        open("https://ya.ru");
        $("#text").setValue(testData);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(11))
                .first()
                .shouldHave(text(testData));
    }
    @CsvSource(value ={
            "Selenide, Мы с гордостью заявляем, что Selenide настолько прост, что вам не нужно читать тонны документации, чтобы начать с ним работать",
            "JUnit, junit.org"
    })
    @ParameterizedTest(name = "Проверка числа в поиске яндекса {0}")
    void yandexSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText){
        open("https://ya.ru");
        $("#text").setValue(searchQuery);
        $("button[type='submit']").click();
        $$("li.serp-item")
                .shouldHave(CollectionCondition.size(10))
                .first()
                .shouldHave(text(expectedText));
    }
    static Stream<Arguments> selenideSiteButtonsTextDataProvider() {
        return Stream.of(
                Arguments.of(List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes"), Locale.EN),
                Arguments.of(List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"), Locale.RU)
        );
    }
    @MethodSource ("selenideSiteButtonsTextDataProvider")
    @ParameterizedTest ( name = "Проверка отображения названия кнопок для локали: {1}")
    void selenideSiteButtonsText(List<String> buttonsTexts, Locale locale){
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible).shouldHave(CollectionCondition.texts(buttonsTexts));
    }

    @EnumSource(Locale.class)
    @ParameterizedTest
    void checkLocaleTest(Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);
    }
}
