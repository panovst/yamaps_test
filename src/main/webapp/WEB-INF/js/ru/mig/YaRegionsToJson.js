/**
 * Трансформатор регионов Яндекса в JSON
 */
function YaRegionsToJson() {
	var regions = [];

	this.addRegion = function (region) {
		regions.push({
			osmId: region.properties.get('osmId'),
			name: region.properties.get('name')
		});
	};

	this.regionToJSONString = function() {
		return JSON.stringify({
			total: regions.length,
			regions: regions
		});
	};

}