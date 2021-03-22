package com.example.rajajainofficalproject.ModelClass;

public class SearchDetailsPOJO {

    private String Title;
    private String Description;
    private String URL;
    private String searchURL;

    public SearchDetailsPOJO(String title, String description, String URL, String searchURL) {
        Title = title;
        Description = description;
        this.URL = URL;
        this.searchURL = searchURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSearchURL() {
        return searchURL;
    }

    public void setSearchURL(String searchURL) {
        this.searchURL = searchURL;
    }
}
