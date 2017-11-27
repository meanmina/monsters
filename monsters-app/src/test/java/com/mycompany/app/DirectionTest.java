package com.mycompany.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DirectionTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DirectionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DirectionTest.class );
    }

    public void testGetDirectionByName()
    {
        String test_name = "north";
        Direction dir = Direction.get_direction_by_name(test_name);
        assertTrue(dir != null);
        assertTrue(dir.name.equals(test_name));
    }

    public void testGetDirectionByNameNoName()
    {
        Direction dir = Direction.get_direction_by_name(null);
        assertTrue(dir == null);
    }

    public void testGetDirectionByNameWrongName()
    {
        String test_name = "xxx";
        Direction dir = Direction.get_direction_by_name(test_name);
        assertTrue(dir == null);
    }

    public void testGetDirectionByValue()
    {
        int test_value = 2;
        Direction dir = Direction.get_direction_by_value(test_value);
        assertTrue(dir != null);
        assertTrue(dir.value == test_value);
    }

    public void testGetDirectionByValueWrongValue()
    {
        int test_value = 6;
        Direction dir = Direction.get_direction_by_value(test_value);
        assertTrue(dir == null);
    }
}
