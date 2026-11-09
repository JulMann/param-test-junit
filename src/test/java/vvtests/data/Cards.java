package vvtests.data;

public enum Cards {
    CARDS("Подарочные карты", "https://vkusvill.ru/cards/"),
    BUSINESS("ВкусВилл Бизнес", "https://vkusvill.ru/b2b/"),
    ADDITIVES("Гайд: пищевые добавки", "https://vkusvill.ru/dobavki/");
    public final String title;
    public final String link;

    Cards(String title, String link) {
        this.title = title;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
   }
}
