package ru.mig

import ru.mig.geonames.DBGeonamesLoader
import ru.mig.yregions.JsonYRegionLoader


def yRegionsLoader = new JsonYRegionLoader().loadData("yandex-regions.json")

def mergeTool = new OsmGeonamesMerge(new DBGeonamesLoader().loadData())

yRegionsLoader.getRegions().each {it ->
	mergeTool.merge(it)
}

println(mergeTool.getResult())
