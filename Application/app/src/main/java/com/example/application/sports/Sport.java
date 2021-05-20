package com.example.application.sports;

import android.net.Uri;

import com.example.application.BuildConfig;
import com.example.application.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls one instance of a sport in the AvailableSports section.
 * A sport has a name, a description of what the sport is about and an URL to the logo of the sport
 * The logo are stored locally.
 */
public class Sport {
    private String name;
    private String description;
    private Uri logo;
    private String link;
    private String email; // Needed to filter activities
    private boolean isExpanded;
    private List<Tag> tags;


    public Sport(){ tags = new ArrayList<>(); }

    //TODO This constructor is never called, this is due to that the sports values are set with the set methods instead, feels like bad practice
    //TODO This is done in QuizRecommended and AvailableSportsActivity
    public Sport(String name,String description,String logo, String link, String email, Boolean isExpanded,  List<Tag> tags){
        this.name = name;
        this.description= description;
        this.logo = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + logo);
        this.link = link;
        this.email = email;
        this.isExpanded = isExpanded;
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

    public Uri getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + logo);
        System.out.println(this.logo);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return whether or not the card is expanded
     */
    public boolean isExpanded() {
        return !isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<Tag> getTags() {
        return tags;
    }

    //TODO vad är det för skillnad på setTag och addTag??? De gör väll samma sak bara att ena tar in en string och den andra en tag. Borde gå att ändra, känns dumt med båda metoderna.
    public void setTag(String tag){
        this.tags.add(Tag.valueOf(tag));
    }

    /**
     * Adds a Tag to the list of tags.
     * @param tag Must match an Enum Tag.
     */
    public void addTag(Tag tag) { tags.add(tag); }

    //TODO Fix JSON
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

