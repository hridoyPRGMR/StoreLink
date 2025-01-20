package com.storelink.pages;

import java.util.List;

public class Menu {

    private int id;
    private String name;
    private String url;
    private List<Menu> subMenus;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }
}