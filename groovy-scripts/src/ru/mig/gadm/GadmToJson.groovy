package ru.mig.gadm

import groovy.grape.Grape
import groovy.json.JsonSlurper
import groovy.sql.Sql

class GadmToJson {
	def geos = [];

	public void init() {
		loadDbDriver();
	}

	def executeTestSql() {
		def db = getConnection()
		def rs = db.firstRow("select ST_AsGeoJSON(ST_FlipCoordinates(st_geometryn(geom, 1))) from RUS_ADM2 where gid = 1764");
		def json = new JsonSlurper().parseText(rs.st_asgeojson)
		println json.coordinates;
	}

	def getConnection() {
		return Sql.newInstance( 'jdbc:postgresql://localhost/gadm', 'gadm', '123', 'org.postgresql.Driver' )
	}

	private void loadDbDriver() {
		def classLoader = this.getClass().getClassLoader();
		while (!classLoader.getClass().getName().equals("org.codehaus.groovy.tools.RootLoader")) {
			classLoader = classLoader.getParent()
		}
		Grape.grab(group:'postgresql', module:'postgresql', version:'9.1-901.jdbc4',classLoader: classLoader)
	}
}
