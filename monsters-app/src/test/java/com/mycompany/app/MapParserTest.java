package com.mycompany.app;

import org.junit.*;
import static org.mockito.Mockito.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

/**
 * Unit test for simple App.
 */
public class MapParserTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MapParserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite()
    {
        return new TestSuite( MapParserTest.class );
    }

    public void testDefaultConstructor()
    {
        MapParser mp = new MapParser();
        assertTrue(mp != null);
        assertTrue(mp.filename.equals(mp.default_file_name));
    }

    public void testNonDefaultConstructor()
    {
        String filename = "/xxx.txt";
        MapParser mp = new MapParser(filename);
        assertTrue(mp != null);
        assertTrue(mp.filename.equals(filename));
    }

    public void testParseLine()
    {
        MapParser mp = new MapParser();
        mp.parse_line("test north=test1");
        City city = City.get_city_from_name("test");
        assertTrue(city != null);
        assertTrue(city.roads[0].equals("test1"));
        assertTrue(city.roads[1].equals(""));
        assertTrue(city.roads[2].equals(""));
        assertTrue(city.roads[3].equals(""));
        city = City.get_city_from_name("test1");
        assertTrue(city != null);
        City.delete_all_cities();
    }

    public void testParseLineNoRoads()
    {
        MapParser mp = new MapParser();
        mp.parse_line("test");
        City city = City.get_city_from_name("test");
        assertTrue(city != null);
        assertTrue(city.roads[0].equals(""));
    }

    public void testParseLineNullParam()
    {
        MapParser mp = new MapParser();
        mp.parse_line(null);
        City city = City.get_city_from_name(null);
        assertTrue(city == null);
    }

    public void testParseLineEmptyParam()
    {
        MapParser mp = new MapParser();
        mp.parse_line("");
        City city = City.get_city_from_name("");
        assertTrue(city == null);
    }

    public void testParseRoadDefinition()
    {
        MapParser mp = new MapParser();
        String[] str = mp.parse_road_definition("north=test");
        assertTrue(str[0].equals("north"));
        assertTrue(str[1].equals("test"));
    }

    public void testParseRoadDefinitionNullParam()
    {
        MapParser mp = new MapParser();
        assertTrue(mp.parse_road_definition(null) == null);
    }

    public void testParseRoadDefinitionNoParam()
    {
        MapParser mp = new MapParser();
        assertTrue(mp.parse_road_definition("") == null);
    }

    public void testParseRoadDefinitionNoEqual()
    {
        MapParser mp = new MapParser();
        assertTrue(mp.parse_road_definition("north") == null);
    }

    public void testParseRoadDefinitionMultipleEqual()
    {
        MapParser mp = new MapParser();
        assertTrue(mp.parse_road_definition("north=test=test1") == null);
    }

    public void testPrintRoadMap()
    {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MapParser mp = new MapParser();
        City city = new City("A");
        city = new City("B");
        city = new City("test");

        city.roads[0] = "A";
        city.roads[2] = "B";
        mp.print_road_map();
        System.setOut(stdout);
        String content = outContent.toString();
        String test_input = "test north=A east=B";
        assertTrue(content.contains(test_input));
        City.delete_all_cities();
    }

    public void testPrintRoadMapNeighbourCitiesNotSetup()
    {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MapParser mp = new MapParser();
        City city = new City("test");
        city.roads[0] = "A";
        city.roads[2] = "B";
        mp.print_road_map();
        System.setOut(stdout);
        assertTrue(outContent.toString().equals(""));
        City.delete_all_cities();
    }

    public void testPrintRoadMapNoCities()
    {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MapParser mp = new MapParser();
        mp.print_road_map();
        assertTrue( outContent.toString().equals(""));

        System.setOut(stdout);
    }
}
