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
		def classLoader = this.getClass().getClassLoader();
		while (!classLoader.getClass().getName().equals("org.codehaus.groovy.tools.RootLoader")) {
			classLoader = classLoader.getParent()
		}
		Grape.grab(group:'postgresql', module:'postgresql', version:'9.1-901.jdbc4',classLoader: classLoader)
		def db = Sql.newInstance( 'jdbc:postgresql://localhost/geonames', 'geonames', '123', 'org.postgresql.Driver' )
		db.eachRow( "select g.geonameid, g.population, alternatename, latitude, longitude from geoname g inner join alternatename names on g.geonameid = names.geonameid where 	isolanguage = 'ru' and fcode = 'ADM1' and names.isshortname is not true	and names.ispreferredname is true" ) {
			geos.add([
			    geonameId: it.geonameid,
				name: it.alternatename,
				population: it.population,
				lat: it.latitude,
				lng: it.longitude
			]);
		}
	}

}
