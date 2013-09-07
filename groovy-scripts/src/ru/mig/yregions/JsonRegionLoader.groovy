package ru.mig.yregions

import groovy.json.JsonSlurper

class JsonYRegionLoader {
	def regions = [];

	JsonYRegionLoader loadData(String fileName) {
		if (regions.empty) {
			_loadData(fileName)
		}
		return this;
	}

	def getRegions() {
		return regions
	}

	private void _loadData(String fileName) {
		def result = new JsonSlurper().parse(new File(fileName).newReader())
		result.regions.each {it ->
			regions.add([
				osmId: it.osmId,
				name: it.name
			]);
		}
	}

}
