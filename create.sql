-- Creates a simple table for storing GPS Points
-- By Skye Book <skye.book@gmail.com>

-- Create the basic table
CREATE TABLE point(
	lat double precision,
	lon double precision
);

-- Create the geometry column (named "geom")
SELECT AddGeometryColumn('public', 'point', 'geom', 4326, 'POINT', 2);

-- Create a GiST index on the geometry data
CREATE INDEX point_geom_index ON point USING GIST(geom);

-- Once the data is loaded, you will need to run the following command.  Note that the Java application already runs this command for you!
-- UPDATE point SET geom = ST_SetSRID(ST_Point(lon, lat), 4326);
