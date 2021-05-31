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
    private String logo;
    private String link;
    private String email; // Needed to filter activities
    private boolean isExpanded;
    private final List<Tag> tags;


    public Sport(){
        tags = new ArrayList<>();
    }

    /** Gets the Sport's name.
     * @return A string representing the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the sport.
     * @param name A string with the name of the sport.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the sport.
     * @return A string with a short description of the sport.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the sports description.
     * @param description A string containing a short description of the sport.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the logo logo for the sport.
     * @return A URI-path that specifies the location of the logo.jpg.
     */
    public Uri getLogo() {
        return Uri.parse(logo);
    }

    /**
     * Sets the logo for the sport.
     * @param logo A string with the name of the logo.jpg
     */
    public void setLogo(String logo) {
        this.logo = "android.resource://" + BuildConfig.APPLICATION_ID + logo;
        System.out.println(this.logo);
    }

    /**
     * Gets the URL to the website of the sport.
     * @return A string representing the URL of the sport's website.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link to the sports website.
     * @param link A string representing the URL to the sports website.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return whether or not the card is expanded
     */
    public boolean isExpanded() {
        return !isExpanded;
    }

    /**
     * Sets the state of the card's expansion.
     * @param expanded Is true if the card is expanded.
     */
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    /**
     * Gets the tags defining the sport.
     * @return A List containing all the sport's tags.
     */
    public List<Tag> getTags() {
        return tags;
    }


    /**
     * Adds a Tag to the list of tags.
     * @param tag Must match an Enum Tag.
     */
    public void addTag(Tag tag) { tags.add(tag); }

    /**
     * Sets the email of the sport's administrator.
     * @param email A string representing the email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the email of the sport's administrator.
     * @return A string representing the email.
     */
    public String getEmail() {
        return email;
    }


}

