package com.mycompany.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

public class FileParser 
{
    String filename;
    String default_file_name = "/map.txt";

    FileParser() {
        filename = default_file_name;
    }

    FileParser(String filename) {
        if (filename == null || filename.length() == 0)
            this.filename = default_file_name;
        else
            this.filename = filename;
    }

    public void read_input() {
        try
        {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)))) {

                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    parse_line(line);
                }
            }
            catch (IOException ex) {
                System.out.println("Bad input file.");
            }
        }
        catch (NullPointerException e) {
            System.out.println("Bad input file.");
        }
    }

    public void parse_line(String line) {
        System.out.println(line);
    }
}