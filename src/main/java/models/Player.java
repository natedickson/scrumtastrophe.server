package models;

public class Player {
    private long id;
    public long getId() {
        return id;
    }

    private String name;
    public String getName() { return name; }

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
