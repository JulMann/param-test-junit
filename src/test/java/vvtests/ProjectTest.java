package vvtests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import vvtests.data.Cards;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverConditions.urlStartingWith;

public class ProjectTest extends TestBase {

    @DisplayName("Тесты для карточек проектов на странице Наши проекты")

    @ValueSource(strings = {"Подарочные карты", "ВкусВилл Бизнес", "Гайд: пищевые добавки"})
    @ParameterizedTest(name = "Карточка проекта {0} отображается на странице проектов")
    @Tag("main-card")
    void shouldShowMainProjectCards(String projectCardsNames) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(projectCardsNames)).should(exist);

    }

    @EnumSource(Cards.class)
    @ParameterizedTest(name = "Карточка проекта имеет ожидаемое название {0}")
    @Tag("main-card")
    void mainProjectCardsShouldHaveExpectedTitle(Cards cards) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(cards.title)).should(exist);

        var actualTitle = $$(".VV23_Projects_Item").findBy(text(cards.title))
                .$(".VV23_Projects_Item__Title").getText();

        Assertions.assertEquals(cards.title, actualTitle);
    }

    @EnumSource(Cards.class)
    @ParameterizedTest(name = "Карточка проекта {0} имеет актуальную ссылку")
    @Tag("main-card")
    void mainProjectCardsShouldHaveExpectedLink(Cards cards) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(cards.title)).should(exist);

        var actualLink = $$(".VV23_Projects_Item").findBy(text(cards.title))
                .$("a").getAttribute(("href"));

        Assertions.assertEquals(cards.link, actualLink);
    }

    @CsvFileSource(resources = "/test-data/projectCards.csv")
    @ParameterizedTest(name = "Карточка проекта {0} имеет верный заголовок")
    @Tag("all-card")
    void allProjectCardsShouldHaveExpectedTitle(String expectedTitle) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(expectedTitle)).should(exist);

        var actualTitle = $$(".VV23_Projects_Item").findBy(text(expectedTitle))
                .$(".VV23_Projects_Item__Title").getText();

        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @CsvFileSource(resources = "/test-data/projectCards.csv")
    @ParameterizedTest(name = "Карточка проекта {0} имеет актуальную ссылку")
    @Tag("all-card")
    void allProjectCardsShouldHaveExpectedLink(String title, String expectedLink) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(title)).should(exist);

        var actualLink = $$(".VV23_Projects_Item").findBy(text(title))
                .$("a").getAttribute(("href"));

        Assertions.assertEquals(expectedLink, actualLink);
    }

    static Stream<Arguments> clickProjectCardsNavigatesToExpectedPage() {
        return Stream.of(
                Arguments.of("Подарочные карты", "/cards/"),
                Arguments.of("ВкусВилл Бизнес", "/b2b/"),
                Arguments.of("Гайд: пищевые добавки", "/dobavki/")

        );
    }

    @ParameterizedTest(name = "Клик по карточке проекта {0} ведет на нужную страницу {1}")
    @MethodSource("clickProjectCardsNavigatesToExpectedPage")
    @Tag("top-card")
    void clickProjectCardsNavigatesToExpectedPage(String title, String link) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(title)).$(".VV23_Projects_Item__Button").click();

        Selenide.webdriver().shouldHave(urlStartingWith("https://vkusvill.ru" + link));

    }

}
