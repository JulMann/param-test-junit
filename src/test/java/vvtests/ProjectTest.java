package vvtests;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import vvtests.data.Cards;

import java.util.Set;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;


@DisplayName("Тесты для карточек проектов на странице Наши проекты")
public class ProjectTest extends TestBase {

    @ValueSource(strings = {"Подарочные карты", "ВкусВилл Бизнес", "Гайд: пищевые добавки"})
    @ParameterizedTest(name = "Карточка проекта {0} отображается на странице проектов")
    @Tag("main-card")
    void shouldShowMainProjectCardsTest(String projectCardsNames) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(projectCardsNames)).should(exist);
    }

    @EnumSource(Cards.class)
    @ParameterizedTest(name = "Карточка проекта имеет ожидаемое название {0}")
    @Tag("main-card")
    void mainProjectCardsShouldHaveExpectedTitleTest(Cards cards) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(cards.title)).should(exist);

        var actualTitle = $$(".VV23_Projects_Item").findBy(text(cards.title))
                .$(".VV23_Projects_Item__Title").getText();

        Assertions.assertEquals(cards.title, actualTitle);
    }

    @EnumSource(Cards.class)
    @ParameterizedTest(name = "Карточка проекта {0} имеет актуальную ссылку")
    @Tag("main-card")
    void mainProjectCardsShouldHaveExpectedLinkTest(Cards cards) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(cards.title)).should(exist);

        var actualLink = $$(".VV23_Projects_Item").findBy(text(cards.title))
                .$("a").getAttribute(("href"));

        Assertions.assertEquals(cards.link, actualLink);
    }

    @CsvFileSource(resources = "/test-data/projectCards.csv")
    @ParameterizedTest(name = "Карточка проекта {0} имеет верный заголовок")
    @Tag("all-card")
    void allProjectCardsShouldHaveExpectedTitleTest(String expectedTitle) {
        open("/projects/");
        $$(".VV23_Projects_Item").findBy(text(expectedTitle)).should(exist);

        var actualTitle = $$(".VV23_Projects_Item").findBy(text(expectedTitle))
                .$(".VV23_Projects_Item__Title").getText();

        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @CsvFileSource(resources = "/test-data/projectCards.csv")
    @ParameterizedTest(name = "Карточка проекта {0} имеет актуальную ссылку")
    @Tag("all-card")
    void allProjectCardsShouldHaveExpectedLinkTest(String title, String expectedLink) {
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
                Arguments.of("Гайд: пищевые добавки", "/dobavki/"),
                Arguments.of("ВкусМил", "/ratsiony-na-kazhdyy-den/"),
                Arguments.of("Экология", "/ecology/"),
                Arguments.of("Благотворительность", "/charity/"),
                Arguments.of("Микромаркеты", "/micro/"),
                Arguments.of("Вендинги", "/media/journal/vending-vkusvill-svezhest-i-kachestvo.html"),
                Arguments.of("Торты на заказ", "/goods/tort/"),
                Arguments.of("ВкусВилл Праздник", "/prazdnik.vkusvill.ru/"),
                Arguments.of("Детский праздник", "/goods/detskiy-prazdnik/"),
                Arguments.of("ВкусВилл Вода", "/voda/"),
                Arguments.of("НИИ Еды", "/food_institute/"),
                Arguments.of("Рекомендуйте и зарабатывайте!", "/partners.vkusvill.ru/")
        );
    }

    @ParameterizedTest(name = "Проект {0} открывается в текущей вкладке")
    @MethodSource("clickProjectCardsNavigatesToExpectedPage")
    @Tag("top-card")
    void clickProjectCardsNavigatesToExpectedPageTest(String title, String link) {
        open("/projects/");
        Set<String> handlesBefore = WebDriverRunner.getWebDriver().getWindowHandles();
        $$(".VV23_Projects_Item").findBy(text(title)).$(".VV23_Projects_Item__Url").click();
        Set<String> handlesAfter = WebDriverRunner.getWebDriver().getWindowHandles();
        webdriver().shouldHave(urlContaining(link));

        Assertions.assertEquals(handlesBefore.size(), handlesAfter.size(),
                "Открылась новая вкладка, а ожидался переход в текущей");
    }
}
