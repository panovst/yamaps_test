/**
 * Класс для работы с API Яндекс карт
 */
function Map() {
	var _map,
		_this = this,
		events = new ymaps.event.Manager();

	this.loadMap = function() {
		_map = new ymaps.Map ("map", {
			center:     getDefaultOptions().defaultCenter,
			zoom: getDefaultOptions().defaultZoom
			/*behaviors: ['scrollZoom']*/
		});

		return _this;
	};

	this.loadRegions = function (options)  {
		var _options = getDefaultRegionOptions();
		if (options) {
			_options = options;
		}
		events.add('regionLoaded', onRegionLoaded);
		ymaps.regions.load(_options.country_iso2, {
			lang: _options.lang,
			quality: _options.quality
		}).then(function (result) {
				attachRegionHandlers(result);
				events.fire('regionLoaded', {
					regions: result.geoObjects
				});
			}, function () {
				console.log('No response !!!!!!!!!!!!!!!!!!!!!!!!!!!');
			});
		return _this;
	};

	/* callback для события 'Регионы загружены' */
	var onRegionLoaded = function (data) {
		_map.geoObjects.add(data.get('regions'));
		renderMask(data.get('regions'));
	};

	/* Отрисовка тумана (т.е. оставить на карте только Россию) */
	var renderMask = function (regions) {
		/* координаты отображаемого многоугольника на карте */
		var coordinates = [];
		regions.each(function (geoObject) {
				coordinates.push.apply(coordinates, geoObject.geometry.getCoordinates());
			});
		var geometry = new ymaps.geometry.Polygon(coordinates, 'evenOdd',{
			projection: _map.options.get('projection')
		});
		geometry.setMap(_map);

		var overlay = new MaskOverlay(geometry.getPixelGeometry(), null, getDefaultRegionsMaskOverlayOptions());
		overlay.setMap(_map);
		overlay.setGeometry(geometry);

	}
}

function getDefaultOptions() {
	return {
		defaultZoom: 3,
		defaultCenter: [69.642513,105.386271], /*Центр РФ*/
		inactiveFillColor: "#FFFF4D",
		activeFillColor: "FFCC99"
	};
}

function getDefaultRegionOptions() {
	return {
		country_iso2: 'RU',
		lang: 'ru',
		quality: 0
	};
}

function getDefaultRegionsMaskOverlayOptions() {
	return {
		zIndex: 1,
		stroke: false,
		strokeColor: false,
		fillColor: 'FFF'
	};
}
function getDefaultRegionBehaviorOptions() {
	return {
		randomFillColor: true,
		activeFillColor: true
	};
}

function attachRegionHandlers(data) {
	var options = getDefaultRegionBehaviorOptions(),
		regions = data.geoObjects;
	if (options.randomFillColor) {
		setRandomColors(regions);
	}
	if (options.activeFillColor) {
		attachActiveInactiveBehavior(regions);
	}
}

function setRandomColors(regions) {
	var colorGenerator = new ColorGenerator();
	regions.each(function (reg) {
		var color = colorGenerator.getColor();
		reg.options.set({fillColor: color});
	});
}

function attachActiveInactiveBehavior(regions) {
	regions.events.add('mouseenter', function (e) {
		var region = e.get('target');
		region.options.set({xfillColor: region.options.get('fillColor')});
		region.options.set({fillColor: getDefaultOptions().activeFillColor});
	});

	regions.events.add('mouseleave', function (e) {
		var region = e.get('target');
		var xFillColor = region.options.get('xfillColor');
		if (xFillColor)
			region.options.set({fillColor: xFillColor});
	});
}