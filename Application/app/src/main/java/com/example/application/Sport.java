package com.example.application;

/**
 * Controls one instance of a sport in the AvailableSports section.
 * A sport has a name, a description of what the sport is about and an URL to the logo of the sport
 * from the CIS website.
 */
public class Sport {
    private String name;
    private String description;
    private String logo;  //If the logos are moved to the application, this should be a int. To reference the correct bytes.
    private String link;
    private boolean isExpanded;

    public  Sport(){}
    public Sport(String name,String description,String logo, String link, Boolean isExpanded){
        this.name = name;
        this.description= description;
        this.logo = logo;
        this.link = link;
        this.isExpanded = isExpanded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

