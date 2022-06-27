package me.epic.kattbot.suggestions;

public class SuggestionData {

    private final int id;
    private final String name;
    private final String avatarUrl;
    private final String suggestion;

    public SuggestionData(int id, String name, String avatarUrl, String suggestion) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.suggestion = suggestion;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public int getId() {
        return id;
    }

}