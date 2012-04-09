/*
 *  SimpleGPS2PostGIS | Imports GPS Points into PostGIS
 *  Copyright (C) 2012  Skye Book
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.skyebook.simplegps2postgis;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

/**
 *
 * @author skye
 */
public class GPS2Post {

    // Database connection
    private static Connection connection;
    // What to divide the raw data by
    private static final double factor = Math.pow(10, 7);
    // The file to read the data from
    private static File file;
    // Buffer to write data to before putting into database
    private static StringBuilder buffer;
    // How many pieces of data to hold in memory before putting into database
    private static int bufferSizeLimit = 5000;
    private static int currentBufferSize = 0;
    // PostgreSQL COPY functionality
    private static CopyManager copy;

    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        // TODO: Add command line options
        readPlainArgs(args);

        processAndImport();
    }

    private static void readPlainArgs(String[] args) throws SQLException {

        file = new File(args[0]);
        String user = args[1];
        String pass = args[2];
        String host = args[3];
        String database = args[4];

        String url = "jdbc:postgresql://" + host + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        connection = DriverManager.getConnection(url, props);
        copy = ((PGConnection)connection).getCopyAPI();
    }

    private static void processAndImport() throws FileNotFoundException, IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        while (reader.ready()) {
            
            // If the buffer size limit has been reached, write to the database
            if(bufferSizeLimit<currentBufferSize){
                writeToDatabase();
            }
            
            String line = reader.readLine();

            // Split the line based on comma and get the raw data
            String[] splits = line.split(",");
            double rawLat = Double.parseDouble(splits[0]);
            double rawLon = Double.parseDouble(splits[1]);

            // convert the raw data into useful lat/lon coordinates
            double lat = rawLat / factor;
            double lon = rawLon / factor;
            
            buffer.append(lat).append(",").append(lon).append("\n");

            // increment the buffer size
            currentBufferSize++;
        }
        
        // flush anything in the buffer to the database
        writeToDatabase();
    }
    
    private static void writeToDatabase() throws SQLException, IOException{
        // Write to the database
        copy.copyIn("COPY point FROM STDIN WITH DELIMITER ','", new ByteArrayInputStream(buffer.toString().getBytes("UTF-8")));
        
        // Reset the buffer and its counter
        buffer = new StringBuilder();
        currentBufferSize = 0;
    }
}
