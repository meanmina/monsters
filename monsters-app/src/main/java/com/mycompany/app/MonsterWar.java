package com.mycompany.app;

import java.util.*;

public class MonsterWar
{
    // each monster has to move at least 10000 times unless all cities destroyed
    int minimum_moves = 10000;
    Random randomGenerator;
    // keep track of monster occupancy
    String[] city_monsters;

    // we could have a Monster class to keep track of deleted status and min_moves made
    int min_monster_moves;
    int num_monsters;
    Boolean[] deleted_monsters;

    MonsterWar() {
        min_monster_moves = 0;
        randomGenerator = new Random();
    }

    public void run() {
        run(null);
    }

    public void run(String filename) {
        MapParser fp = new MapParser(filename);
        // read map.txt
        fp.read_input();
        this.num_monsters = 0;

        while (this.num_monsters <= 0) {
            //read N from commandline
            // N monsters with random positions on map
            // limited by size of int
            // error handling?
            System.out.println("Input N:");
            Scanner scanner = new Scanner(System.in);
            try {
                this.num_monsters = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException ex) {
                System.out.println("Bad input try again by inputting an integer.");
            }
        }
        int total_monsters = num_monsters;
        // keep track of moves per monster
        int[] monster_moves = new int[total_monsters];
        this.city_monsters = new String[total_monsters];
        this.deleted_monsters = new Boolean[total_monsters];

        this.num_monsters = set_monsters(monster_moves);

        // this loop will take a long time especially for an odd number of monsters
        while (this.num_monsters > 0 && this.min_monster_moves < minimum_moves) {
            System.out.println("min_moves " + this.min_monster_moves + "num_monsters " + this.num_monsters);
            move_all_monsters(monster_moves, total_monsters);
        }

        // print remaining road map
        fp.print_road_map();
    }

    public int set_monsters(int[] monster_moves) {
        if (monster_moves == null)
            return 0;
        // during setting of monsters we can have some wars so num_monsters can change
        int new_num_monsters = this.num_monsters;
        List<City> cities = City.get_all_cities();
        for (int i = 0; i < this.num_monsters; i++) {
            // set all monster_moves to 0
            monster_moves[i] = 0;
            // set all monsters to not deleted
            this.deleted_monsters[i] = false;
            // place monster randomly on a city
            City city = cities.get(randomGenerator.nextInt(cities.size()));
            int count = 0;
            while (city.deleted && count < cities.size()) {
                city = cities.get(randomGenerator.nextInt(cities.size()));
                count++;
            }
            this.city_monsters[i] = city.name;
            // mark city as occupied by monster
            new_num_monsters = occupy_city(city, i);
        }
        return new_num_monsters;
    }

    // make a random move for all monsters
    public void move_all_monsters(int[] monster_moves, int total_monsters) {
        if (monster_moves == null) return;

        // if only one monster then there's no point in moving it around
        // if we have an odd number of monsters we will end up with one moving around a lot at the end
        if (this.num_monsters <= 1) return;

        int min_moves = this.minimum_moves;

        for (int i = 0; i < total_monsters; i++) {
            // if monster was destroyed in a fight then don't even look at it
            // should improve performance since we don't retrieve city info anymore
            //      for a deleted monster
            if (this.deleted_monsters[i]) continue;

            City city = City.get_city_from_monster(i);
            if (city == null) {
                continue;
            }

            if (city.deleted)
                continue;

            Boolean done = move_monster(i, city);
            if (done == true) {
                monster_moves[i]++;
                if (monster_moves[i] < min_moves)
                    min_moves = monster_moves[i];
            }
        }
        this.min_monster_moves = min_moves;
    }

    public Boolean move_monster(int monster, City city) {
        if (monster < 0 || monster >= this.num_monsters) return false;
        if (city == null) return false;
        // see how many moves we can make from this city
        City new_city = pick_new_city(monster, city);
        if (new_city != null) {
            int new_num_monsters = occupy_city(new_city, monster);
            if (new_num_monsters >= 0) {
                // free up old city
                city.occupying_monster = -1;
                this.num_monsters = new_num_monsters;
                return true;
            }
        }
        return false;
    }

    public City pick_new_city(int monster, City city) {
        if (city == null) return null;
        if (monster < 0 || monster >= this.num_monsters) return null;

        List<City> directions = new ArrayList<City>();
        for (int j = 0; j < city.roads.length; j++) {
            // an undefined road it will have the value ""
            if (city.roads[j].length() <= 0) continue;
            // if there's a city in that direction
            City city_road = City.get_city_from_name(city.roads[j], false);

            // if city has not been destroyed
            if (city_road != null && !city_road.deleted) {
                directions.add(city_road);
            }
        }
        if (directions.size() == 0) {
            // if monster is blocked (i.e. no roads from it)
            // mark as deleted to avoid attempts at moving it
            // should improve performance
            this.deleted_monsters[monster] = true;
            return null;
        }
        // pick a random direction
        int index = 0;
        if (directions.size() > 1)
            randomGenerator.nextInt(directions.size());
        return directions.get(index);
    }

    public int occupy_city(City new_city, int monster) {
        int monster_count = this.num_monsters;

        if (new_city == null) return monster_count;
        if (monster < 0 || monster >= this.num_monsters) return monster_count;

        this.city_monsters[monster] = new_city.name;
        // if city occupied by monster then fight
        if (new_city.occupying_monster > -1) {
            monster_count = fight_monsters(new_city, monster);
        } else {
            new_city.occupying_monster = monster;
        }
        return monster_count;
    }

    public int fight_monsters(City city, int monster) {
        if (city == null) return this.num_monsters;
        if (monster < 0 || monster >= this.num_monsters) return this.num_monsters;

        System.out.println(city.name + " has been destroyed by monster " + monster + " and monster " + city.occupying_monster + "!");
        city.deleted = true;
        this.deleted_monsters[monster] = true;
        this.deleted_monsters[city.occupying_monster] = true;
        return this.num_monsters - 2;
    }
}