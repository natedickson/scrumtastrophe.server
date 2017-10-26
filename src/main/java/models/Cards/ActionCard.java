package models.Cards;

public class ActionCard implements Card {
    private String name;
    @Override
    public String getName() {
        return name;
    }
    private String description;
    @Override
    public String getDescription() {
        return description;
    }

    public ActionCard(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
