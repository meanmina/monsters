package com.mycompany.app;

import java.util.*;

public class App
{
    public static void main( String[] args )
    {
        MonsterWar war = new MonsterWar();
        // war.run("filename.txt");
        // to run app with dif input map file
        war.run();
    }
    // to package:
    // mvn package

    // to run tests:
    // mvn test
    // (mvn package also runs tests)

    // to run app with map.txt as input
    // java -cp target/monsters-app-1.0-SNAPSHOT.jar com.mycompany.app.App
}
