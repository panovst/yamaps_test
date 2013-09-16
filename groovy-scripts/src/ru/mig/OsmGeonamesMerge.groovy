package ru.mig

import groovy.json.JsonBuilder
import ru.mig.geonames.DBGeonamesLoader

class OsmGeonamesMerge {
	DBGeonamesLoader geonamesLoader
	def matched = [];
	def unmatched = [];
	def extMatched = [
			[osmId:151234, name:"Республика Саха", geonameId: 2013162],
			[osmId:140296, name:"Ханты-Мансийский автономный округ - Югра", geonameId: 1503773],
			[osmId:145729, name:"Бурятия", geonameId: 2050915],
			[osmId:145730, name:"Забайкальский край", geonameId: 7779061],
			[osmId:393980, name:"Республика Карелия", geonameId: 552548],
			[osmId:145195, name:"Тыва", geonameId: 1488873],
			[osmId:115135, name:"Пермский край", geonameId: 511180],
			[osmId:77677, name:"Башкортостан", geonameId: 578853],
			[osmId:72193, name:"Саратовская область", geonameId: 498671],
			[osmId:108083, name:"Республика Калмыкия", geonameId: 553972],
			[osmId:79374, name:"Татарстан", geonameId: 484048],
			[osmId:145194, name:"Алтай", geonameId: 1506272],
			[osmId:190911, name:"Республика Хакасия", geonameId: 1503834],
			[osmId:109876, name:"Дагестан", geonameId: 567293],
			[osmId:72196, name:"Республика Мордовия", geonameId: 525369],
			[osmId:115114, name:"Марий Эл", geonameId: 529352],
			[osmId:115134, name:"Удмуртская республика", geonameId: 479613],
			[osmId:81993, name:"Тульская область", geonameId: 480508],
			[osmId:80513, name:"Чувашия", geonameId: 567395],
			[osmId:253256, name:"Адыгея", geonameId: 584222],
			[osmId:109877, name:"Чеченская республика", geonameId: 569665],
			[osmId:109878, name:"Карачаево-Черкесская республика", geonameId: 552927],
			[osmId:109879, name:"Кабардино-Балкарская республика", geonameId: 554667],
			[osmId:110032, name:"Северная Осетия - Алания", geonameId: 519969],
			[osmId:253252, name:"Ингушетия", geonameId: 556349]
	];

	OsmGeonamesMerge(geonamesLoader) {
		this.geonamesLoader = geonamesLoader
	}

	void merge(yRegion) {
		boolean found = false;
		geonamesLoader.getGeos().each {it ->
			if (yRegion.name == it.name) {
				addToMatched(yRegion, it)
				found = true
			}
		}
		if (!found) {
			doExtMatching(yRegion)
		}
	}

	def getResult() {
		Map<String, Object> result = new HashMap<String, Object>();
		matched.each {it->
			result.put(it.osmId, it)
		}
		return new JsonBuilder([
				total: matched.size(),
				matched: result,
		]).toPrettyString()

	}

	private void doExtMatching(yRegion) {
		boolean found = false
		extMatched.each {it ->
			if (yRegion.osmId.toInteger() == it.osmId) {
				def geo = geonamesLoader.loadById(it.geonameId)
				geo.name = yRegion.name
				addToMatched(yRegion, geo)
				found = true
			}
		}
		if (!found) {
			addToUnMatched(yRegion)
		}
	}

	private void addToMatched(yRegion, geoname) {
		matched.add([
				osmId: yRegion.osmId,
				geonameId: geoname.geonameId,
				name: geoname.name,
				population: geoname.population,
				lat: geoname.lat,
				lng: geoname.lng
		])
	}

	private void addToUnMatched(yRegion) {
		println([osmId: yRegion.osmId, name: yRegion.name])
		unmatched.add([
				osmId: yRegion.osmId,
				name: yRegion.name
		])
	}
}