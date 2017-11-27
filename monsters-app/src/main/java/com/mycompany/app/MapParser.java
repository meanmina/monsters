package com.mycompany.app;

import java.util.*;

public class MapParser extends FileParser
{
    MapParser() {
    }

    MapParser(String filename) {
        super(filename);
    }

    public void parse_line(String line) {
        if (line == null || line.length() == 0) return;
        // assume only one city per line
        //read line - city and directions(<direction>=<city>)
        String[] items = line.split(" ");

        // assume first element is the city name
        // and city names are always strings
        // city will be created if non existent
        City city = City.get_city_from_name(items[0]);

        // loop through the items in the line
        for (int i = 1; i < items.length; i++) {
            String[] road = parse_road_definition(items[i]);
            if (road == null) continue;

            Direction direction = Direction.get_direction_by_name(road[0]);
            if (direction == null) {
                System.out.println("Direction not found for name " + road[0]);
                continue;
            }

            // city will be created if non existent
            City road_city = City.get_city_from_name(road[1]);
            city.roads[direction.value] = road_city.name;
        }
    }

    public String[] parse_road_definition(String def) {
        if (def == null || !def.contains("=")) {
            System.out.println("Expected road definition to contain '=' but didn't. Def.: " + def);
            return null;
        }

        String[] road = def.split("=");
        if (road.length > 2 || road.length <= 0) {
            // if '=' contained more than once then road definition is wrong
            System.out.println("Bad road definition: " + def);
            return null;
        }
        return road;
    }

    public void print_road_map() {
        for (City city : City.get_all_cities()) {
            // don't print deleted cities
            if (city.deleted) continue;

            Boolean print_line = false;
            String output_line = city.name;
            for (int i = 0; i < city.roads.length; i++) {
                String city_road = city.roads[i];
                if (city_road.length() <= 0) continue;

                City city2 = City.get_city_from_name(city_road, false);
                if (city2 != null && !city2.deleted) {
                    output_line += " " + Direction.get_direction_by_value(i).name + "=" + city_road;
                    print_line = true;
                }
            }
            if (print_line)
                System.out.println(output_line);
        }
    }
}