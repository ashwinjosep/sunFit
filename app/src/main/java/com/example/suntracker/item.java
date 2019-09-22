package com.example.suntracker;

public class item {
    private String itemName;
    private String itemCounter;
    private int itemCalorie;

    public item(String a, String b, int val)
    {
        this.itemName=a;
        this.itemCounter=b;
        this.itemCalorie=val;
    }

    public String getItem()
    {
        return itemName;
    }

    public String getValue()
    {
        return String.valueOf(itemCalorie);
    }

    public String getItemCounter()
    {
        return itemCounter;
    }
}
