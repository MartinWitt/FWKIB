package keksdose.fwkib.bot.model;

public class User {
    private String name;
    private int number;

    public User(String name, String number) {
        this.name = name;
        this.number = Integer.parseInt(number);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }
}