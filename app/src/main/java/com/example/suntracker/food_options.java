package com.example.suntracker;

import java.util.ArrayList;

public class food_options {

    private static Integer id=0;
    private String option_name;
    private Integer option_id;
    private Integer calorie;
    private ArrayList<item> itemList = new ArrayList<item>();

    public String getOption()
    {
        return option_name;
    }

    public String getCalorie()
    {
        return calorie.toString();
    }

    public Integer getOption_id()
    {
        return option_id;
    }

    public food_options(String opt_name, Integer cal, ArrayList<item> items)
    {
        option_id = id;
        id++;
        option_name = opt_name;
        calorie = cal;
        this.itemList = items;
    }

    public String getDetail(){
        StringBuilder result = new StringBuilder();
        for(item i :this.itemList)
        {
            result.append(i.getItem()+" ("+i.getItemCounter()+") ("+i.getValue()+" Cal)\n\n");
        }
        return result.toString();
    }
}
