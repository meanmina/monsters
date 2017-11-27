package com.mycompany.app;

import java.util.*;

public class City 
{
    public final String name;
    public Boolean deleted;
    public int occupying_monster;
    public String[] roads;

    public static List<City> instances = new ArrayList<City>();

    public City(String name) {
        this.name = name;
        this.deleted = false;
        this.occupying_monster = -1;
        this.roads = new String[4];
        Arrays.fill(this.roads, "");

        instances.add(this);
    }

    // class method
    static public City get_city_from_monster(int monster) {
        for (City city : instances) {
            if (city.occupying_monster == monster)
                return city;
        }
        return null;
    }

    // class method
    // get city if existing or make new one
    static public City get_city_from_name(String name, Boolean make_new) {
        if (name == null || name.length() == 0) return null;
        for (City city : instances) {
            if (city.name != null && city.name.equals(name))
                return city;
        }
        if (make_new)
            return new City(name);

        return null;
    }

    // class method
    // override with one less argument
    static public City get_city_from_name(String name) {
        return get_city_from_name(name, true);
    }

    // class method
    static public List<City> get_all_cities() {
        return instances;
    }

    // class method
    static public List<City> get_deleted_cities() {
        List<City> deleted_cities = new ArrayList<City>();
        for (City city : instances) {
            if (city.deleted) deleted_cities.add(city);
        }
        return deleted_cities;
    }

    // class method
    static public void delete_all_cities() {
        instances.clear();
    }
}