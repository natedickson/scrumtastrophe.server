package models.Cards;

public class BugCard implements Card {
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

    public BugCard(String name, String description) {
        this.name = name;
        this.description = description;
    }
}