package ru.mig

import groovy.json.JsonBuilder

class OsmGeonamesMerge {
	def geos = [];
	def matched = [];
	def unmatched = [];

	OsmGeonamesMerge(geos) {
		this.geos = geos
	}

	void merge(yRegion) {
		boolean found = false;
		geos.each {it ->
			if (yRegion.name == it.name) {
				matched.add([
						osmId: yRegion.osmId,
						geonameId: it.geonameId,
						name: it.name,
						population: it.population,
						lat: it.lat,
						lng: it.lng
				])
				found = true
			}
		}
		if (!found) {
			unmatched.add([
					osmId: yRegion.osmId,
					name: yRegion.name
			])
		}
	}

	def getResult() {
		return new JsonBuilder([
				matchedTotal: matched.size(),
				matched: matched,
				unmatchedTotal: unmatched.size(),
				unmatched: unmatched
		]).toPrettyString()

	}
}
