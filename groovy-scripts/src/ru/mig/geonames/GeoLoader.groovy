package ru.mig.geonames

import groovy.grape.Grape
import groovy.sql.Sql

class DBGeonamesLoader {
	def geos = [];

	DBGeonamesLoader loadData() {
		if (geos.empty) {
			_loadData();
		}
		return this;
	}

	def getGeos() {
		return geos;
	}
	private void _loadData() {
		def db = getConnection()
		db.eachRow( "select g.geonameid, g.population, alternatename, latitude, longitude from geoname g inner join alternatename names on g.geonameid = names.geonameid where 	isolanguage = 'ru' and fcode = 'ADM1' and names.isshortname is not true	and names.ispreferredname is true" ) {
			geos.add([
					geonameId: it.geonameid,
					name: it.alternatename,
					population: it.population,
					lat: it.latitude,
					lng: it.longitude
			])
		}
	}

	def loadById(geonameId) {
		def found = getConnection().firstRow( "select geonameid, population, latitude, longitude from geoname g where geonameid = $geonameId")
		return [
				geonameId: found.geonameid,
				population: found.population,
				lat: found.latitude,
				lng: found.longitude
		]
	}

	def getConnection() {
		def classLoader = this.getClass().getClassLoader();
		while (!classLoader.getClass().getName().equals("org.codehaus.groovy.tools.RootLoader")) {
			classLoader = classLoader.getParent()
		}
		Grape.grab(group:'postgresql', module:'postgresql', version:'9.1-901.jdbc4',classLoader: classLoader)
		return Sql.newInstance( 'jdbc:postgresql://localhost/geonames', 'geonames', '123', 'org.postgresql.Driver' )
	}
}
