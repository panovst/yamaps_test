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

	def executeSql() {
		executeBase();
		executeExceptions();
		println geos.size();
		println  new JsonBuilder(geos).toString()
	}

	private void executeBase() {
		def db = getConnection()
		def rs = db.eachRow("select gid, nl_name_2 as name, ST_AsGeoJSON(ST_FlipCoordinates(st_geometryn(geom, 1)))  as coordinatesObj, name_2 as name_2 from (select * from RUS_ADM2 where id_1 = 65 and type_2 = 'Raion' and ST_NumGeometries(geom) = 1) as tmp;"){
			def dataSource = SakhaRegions.exceptions[it.gid];
			if (dataSource == null) {
				dataSource = it;
			}
			geos.add([
					gid: dataSource.gid,
					name: dataSource.name,
					name_2: dataSource.name_2,
					coordinates: new JsonSlurper().parseText(dataSource.coordinatesObj).coordinates
			]);
		}
	}

	private void executeExceptions() {
		def db = getConnection()
		def rs = db.eachRow("select gid, nl_name_2 as name, ST_AsGeoJSON(ST_FlipCoordinates(st_geometryn(geom, 1)))  as coordinatesObj, name_2 as name_2 from (select * from RUS_ADM2 where id_1 = 65 and type_2 = 'Raion' and ST_NumGeometries(geom) > 1) as tmp;"){
			def dataSource = SakhaRegions.exceptions[it.gid];
			if (dataSource != null) {
				geos.add([
						gid: dataSource.gid,
						name: dataSource.name,
						name_2: dataSource.name_2,
						coordinates: new JsonSlurper().parseText(dataSource.coordinatesObj).coordinates
				]);
			}
		}
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
