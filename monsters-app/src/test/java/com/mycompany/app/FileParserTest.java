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
public class FileParserTest 
    extends TestCase
{
    private final PrintStream stdout = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FileParserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static junit.framework.Test suite()
    {
        return new TestSuite( FileParserTest.class );
    }

    public void testDefaultConstructor()
    {
        FileParser fp = new FileParser();
        assertTrue(fp != null);
        assertTrue(fp.filename.equals(fp.default_file_name));
    }

    public void testNonDefaultConstructor()
    {
        String filename = "/xxx.txt";
        FileParser fp = new FileParser(filename);
        assertTrue(fp != null);
        assertTrue(fp.filename.equals(filename));
    }

    public void testNonDefaultConstructorNullParam()
    {
        FileParser fp = new FileParser(null);
        assertTrue(fp != null);
        assertTrue(fp.filename != null);
        assertTrue(fp.filename.equals(fp.default_file_name));
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(stdout);
    }

    @Test
    public void testParseLineNoParam()
    {
        FileParser fp = new FileParser("/short_map.txt");
        fp.parse_line(null);
        assertEquals("", outContent.toString());
    }

    public void testReadInput()
    {
        FileParser fp = new FileParser("/short_map.txt");
        FileParser spy = spy(fp);

        spy.read_input();
        String line = "A1 north=A2 south=A3 east=A4 west=A5";
        verify(spy).parse_line(line);
    }
}
