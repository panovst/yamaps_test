package ru.mig.gadm

import groovy.grape.Grape
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.sql.Sql

class GadmToJson {
	def geos = [];

	public void init() {
		loadDbDriver();
	}

	def executeTestSql() {
		def db = getConnection()
		def rs = db.eachRow("select gid, nl_name_2 as name, ST_AsGeoJSON(ST_FlipCoordinates(st_geometryn(geom, 1)))  as coordinatesObj, name_2 as name_2 from (select * from RUS_ADM2 where id_1 = 65 and type_2 = 'Raion' and ST_NumGeometries(geom) = 1 and nl_name_2 is not null and nl_name_2 != '') as tmp;"){
			geos.add([
				gid: it.gid,
				name: it.name,
				name_2: it.name_2,
				coordinates: new JsonSlurper().parseText(it.coordinatesObj).coordinates
			]);
		}
		println geos.size();
		println  new JsonBuilder(geos).toPrettyString();
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
