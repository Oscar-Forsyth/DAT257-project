package com.example.application;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls one instance of a sport in the AvailableSports section.
 * A sport has a name, a description of what the sport is about and an URL to the logo of the sport
 * from the CIS website.
 */
public class Sport {
    private String name;
    private String description;
    private String logo;
    private List<Tag> tags;

    public  Sport(){ tags = new ArrayList<>(); }
    public Sport(String name,String description,String logo, List<Tag> tags){
        this.name = name;
        this.description= description;
        this.logo = logo;
        this.tags = tags;
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

    public List<Tag> getTags() {return tags; }

    /**
     * Adds a Tag to the list of tags.
     * @param tag Must match an Enum Tag.
     */
    public void addTag(Tag tag) { tags.add(tag); }
}

