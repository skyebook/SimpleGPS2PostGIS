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

import java.util.Properties;

/**
 *
 * @author skye
 */
public class GPS2Post {
    
    private static Properties dbProps;

    public static void main(String[] args) {
        // TODO: Add command line options
        
        
    }
    
    private static void readPlainArgs(String[] args){
        dbProps = new Properties();
    }
}
