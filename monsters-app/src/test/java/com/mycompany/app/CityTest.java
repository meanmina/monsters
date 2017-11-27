package com.mycompany.app;

import org.junit.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.*;

public class CityTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CityTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite()
    {
        return new TestSuite( CityTest.class );
    }

    @Before
    public void setUpCityList() {
        City.delete_all_cities();
    }

    @Test
    public void testCityCreate()
    {
        String test_name = "test";
        City city = new City(test_name);

        assertTrue(city != null);
        assertEquals(city.name, test_name);
        assertFalse(city.deleted);
        assertEquals(city.occupying_monster, -1);
    }

    @Test
    public void testCityCreateNullParam()
    {
        City city = new City(null);

        assertTrue(city != null);
        assertEquals(city.name, null);
        assertFalse(city.deleted);
        assertEquals(city.occupying_monster, -1);
    }

    @Test
    public void testGetCityFromMonster()
    {
        String test_name = "test";
        int test_monster = 1;
        City city = new City(test_name);
        city.occupying_monster = test_monster;

        City found_city = City.get_city_from_monster(test_monster);
        assertTrue(found_city != null);
        assertEquals(found_city.name, test_name);
        assertEquals(found_city.occupying_monster, test_monster);
    }

    @Test
    public void testGetCityFromWrongMonster()
    {
        String test_name = "test";
        int test_monster = 1;
        City city = new City(test_name);
        city.occupying_monster = test_monster;

        City found_city = City.get_city_from_monster(2);
        assertTrue(found_city == null);

        found_city = City.get_city_from_monster(test_monster);
        assertTrue(found_city != null);
        assertEquals(found_city.name, test_name);
        assertEquals(found_city.occupying_monster, test_monster);
    }

    @Test
    public void testGetCityFromName()
    {
        String test_name = "test";
        City city = new City(test_name);

        City found_city = City.get_city_from_name(test_name);
        assertTrue(found_city != null);
        assertEquals(found_city.name, test_name);
    }

    @Test
    public void testGetCityFromWrongName()
    {
        String test_name = "test";
        City city = new City(test_name);
        String bad_test_name = "badTest";

        City found_city = City.get_city_from_name(bad_test_name);
        // expect new City to have been created
        assertTrue(found_city != null);
        assertEquals(found_city.name, bad_test_name);

        found_city = City.get_city_from_name(test_name);
        assertTrue(found_city != null);
        assertEquals(found_city.name, test_name);
    }

    @Test
    public void testGetCityFromWrongNameNoMakeNew()
    {
        String test_name = "test";
        City city = new City(test_name);
        String bad_test_name = "badTest";

        City found_city = City.get_city_from_name(bad_test_name, false);
        // expect new City to NOT have been created
        assertTrue(found_city == null);

        found_city = City.get_city_from_name(test_name);
        assertTrue(found_city != null);
        assertEquals(found_city.name, test_name);
    }

    @Test
    public void testDeleteAllCities()
    {
        String[] test_names = {"test", "test2"};
        City city = new City(test_names[0]);
        city = new City(test_names[1]);

        City.delete_all_cities();
        List<City>cities = City.get_all_cities();
        assertEquals(cities.size(), 0);
    }

    @Test
    public void testGetAllCities()
    {
        City.delete_all_cities();
        String[] test_names = {"test", "test2"};
        City city = new City(test_names[0]);
        city = new City(test_names[1]);

        List<City> cities = City.get_all_cities();
        assertEquals(cities.size(), 2);
        for (int i = 0; i < cities.size(); i++) {
            assertTrue(cities.get(i).name.equals(test_names[i]));
        }
    }

    @Test
    public void testGetAllCitiesNoCities()
    {
        City.delete_all_cities();
        List<City> cities = City.get_all_cities();
        assertEquals(cities.size(), 0);
    }

    @Test
    public void testGetDeletedCities()
    {
        String[] test_names = {"test", "test2", "test3"};
        City city = new City(test_names[0]);
        city.deleted = true;
        city = new City(test_names[1]);
        city = new City(test_names[2]);
        city.deleted = true;

        List<City> cities = City.get_deleted_cities();
        assertEquals(cities.size(), 2);
        for (int i = 0; i < cities.size(); i++) {
            if (i == 1) continue;
            assertTrue(cities.get(i).name.equals(test_names[i]));
        }
    }

    @Test
    public void testGetDeletedCitiesNoCities()
    {
        List<City> cities = City.get_deleted_cities();
        assertEquals(cities.size(), 0);
    }

    @Test
    public void testGetDeletedCitiesNoDeletedCities()
    {
        String[] test_names = {"test", "test2"};
        City city = new City(test_names[0]);
        city = new City(test_names[1]);

        List<City> cities = City.get_deleted_cities();
        assertEquals(cities.size(), 0);
    }
}
