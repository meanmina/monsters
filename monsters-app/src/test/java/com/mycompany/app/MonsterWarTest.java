package com.mycompany.app;

import java.util.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MonsterWarTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MonsterWarTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite()
    {
        return new TestSuite( MonsterWarTest.class );
    }

    public void testRun()
    {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        InputStream stdin = System.in;
        String input = "2";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 2;
        mw.run("/short_map.txt");

        System.setOut(stdout);
        System.setIn(stdin);
        List<Integer> possible_results = Arrays.asList(0, 2);
        assertTrue(possible_results.contains(mw.num_monsters));
        String content = outContent.toString();
        String test_input = "Input N:";
        assertTrue(content.contains(test_input));
    }

    public void testRunInputNumMonstersMoveAllMonstersGetsCalled()
    {
        InputStream stdin = System.in;
        String input = "4";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        MonsterWar mw = new MonsterWar();
        MonsterWar spy = spy(mw);
        spy.run("/short_map.txt");

        System.setIn(stdin);

        int total_monsters = 4;
        verify(spy, atLeast(1)).move_all_monsters(any(int[].class), eq(total_monsters));
    }

    // setup is a lot more test specific so decided to do it in test
    // in an industry scenario we would want to abstract away much more
    // @Before
    // public void setUp()
    // {
    //     // not impl
    // }

    @After
    public void cleanUp()
    {
        City.delete_all_cities();
    }

    @Test
    public void testSetMonsters()
    {
        int[] monster_moves = new int[3];
        new City("test1");
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        mw.city_monsters = new String[3];
        mw.deleted_monsters = new Boolean[3];
        List<Integer> possible_results = Arrays.asList(1, 3);
        assertTrue(possible_results.contains(mw.set_monsters(monster_moves)));
    }

    @Test
    public void testSetMonstersNullParam()
    {
        new City("test1");
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 1;
        assertEquals(mw.set_monsters(null), 0);
    }

    @Test
    public void testMoveAllMonsters()
    {
        int[] monster_moves = new int[2];
        int total_monsters = 2;
        new City("test1");
    }

    public void testMoveAllMonstersNullParam1()
    {
        int total_monsters = 2;
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 2;
        mw.move_all_monsters(null, total_monsters);
        assertEquals(mw.min_monster_moves, 0);
    }

    public void testMoveAllMonstersZeroParam2()
    {
        int[] monster_moves = new int[1];
        int total_monsters = 0;
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 1;
        mw.move_all_monsters(monster_moves, total_monsters);
        List<Integer> possible_results = Arrays.asList(0, mw.minimum_moves);
        assertTrue(possible_results.contains(mw.min_monster_moves));
    }

    @Test
    public void testMoveMonster()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        mw.city_monsters = new String[3];
        mw.deleted_monsters = new Boolean[3];
        City city = new City("test1");
        city = new City("test");
        city.roads[0] = "test1";
        assertTrue(mw.move_monster(0, city));
    }

    @Test
    public void testMoveMonsterWrongMonster()
    {
        MonsterWar mw = new MonsterWar();
        City city = new City("test");
        assertFalse(mw.move_monster(-1, city));
    }

    public void testMoveMonsterNullCity()
    {
        MonsterWar mw = new MonsterWar();
        assertFalse(mw.move_monster(0, null));
    }

    @Test
    public void testPickNewCity()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        City city = new City("test1");
        city = new City("test");
        city.roads[0] = "test1";
        City new_city = mw.pick_new_city(0, city);
        assertTrue(new_city != null);
        assertTrue(new_city.name.equals("test1"));
    }

    @Test
    public void testPickNewCityWrongMonster()
    {
        MonsterWar mw = new MonsterWar();
        City city = new City("test");
        assertTrue(mw.pick_new_city(-1, city) == null);
    }

    public void testPickNewCityNullCity()
    {
        MonsterWar mw = new MonsterWar();
        assertTrue(mw.pick_new_city(0, null) == null);
    }

    @Test
    public void testOccupyCity()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 1;
        mw.city_monsters = new String[1];
        mw.deleted_monsters = new Boolean[1];
        City city = new City("test");
        assertEquals(mw.occupy_city(city, 0), 1);
    }

    @Test
    public void testOccupyCityWithOccupyingMonster()
    {
        MonsterWar mw = new MonsterWar();
        MonsterWar spy = spy(mw);
        spy.num_monsters = 3;
        spy.city_monsters = new String[3];
        spy.deleted_monsters = new Boolean[3];
        City city = new City("test");
        city.occupying_monster = 2;
        int result = spy.occupy_city(city, 0);
        assertTrue(spy.city_monsters[0].equals(city.name));
        assertTrue(city.deleted);
        assertEquals(result, 1);
        verify(spy).fight_monsters(city, 0);
    }

    @Test
    public void testOccupyCityWrongMonster()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        City city = new City("test");
        assertEquals(mw.occupy_city(city, -1), 3);
    }

    public void testOccupyCityNullCity()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        assertEquals(mw.occupy_city(null, 0), 3);
    }

    @Test
    public void testFightMonsters()
    {
        PrintStream stdout = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        mw.deleted_monsters = new Boolean[3];
        Arrays.fill(mw.deleted_monsters, false);
        City city = new City("test");
        city.occupying_monster = 1;
        assertEquals(mw.fight_monsters(city, 0), 1);

        System.setOut(stdout);
        String content = outContent.toString();
        String test_input = "test has been destroyed by monster 0 and monster 1!";
        assertTrue(content.contains(test_input));
    }

    @Test
    public void testFightMonstersWrongMonster()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        City city = new City("test");
        assertEquals(mw.fight_monsters(city, -1), 3);
    }

    public void testFightMonstersNullCity()
    {
        MonsterWar mw = new MonsterWar();
        mw.num_monsters = 3;
        assertEquals(mw.fight_monsters(null, 0), 3);
    }
}
