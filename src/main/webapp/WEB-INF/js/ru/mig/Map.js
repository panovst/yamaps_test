/**
 * Класс для работы с API Яндекс карт
 */
function Map(options) {
	var _map,         // ссылка на саму карту
		_this = this,
		events = new ymaps.event.Manager(),  // событийный менеджер от яндекса
		mapOptions = options;   // настройки карты

	/**
	 * Инициализация карты
	 */
	this.loadMap = function() {
		_map = new ymaps.Map ("map", {
			center:     getDefaultOptions().defaultCenter,
			zoom: getDefaultOptions().defaultZoom,
			behaviors: ['drag', 'scrollZoom']
		});

		return _this;
	};

	/**
	 * Загрузка регионов (@see сервис Яндекс-Регионы http://api.yandex.ru/maps/doc/jsapi/2.x/dg/concepts/regions.xml)
	 *
	 */
	this.loadRegions = function ()  {
		events.add('regionLoaded', onRegionLoaded);  // регистрируем слушатель события когда все регионы загрузятся
		ymaps.regions.load(mapOptions.region_country_iso2, {
			lang: mapOptions.region_country_lang,
			quality: mapOptions.region_quality
		}).then(function (result) {
				attachRegionHandlers(result, mapOptions);  // подсветка hover, генерация цветов
				events.fire('regionLoaded', {
					regions: result.geoObjects
				});
			}, function () {
				console.log('No response !!!!!!!!!!!!!!!!!!!!!!!!!!!');
			});
		return _this;
	};
	/**
	 *	Добавляет geo объект на карту
	 *
	 *  @param geo - точка "ymaps.Placemark", многоугольник "ymaps.Polygon",
	 *  коллекция гео объектов "ymaps.GeoObjectCollection" etc.
	 */
	this.addGeoObject = function (geo) {
		_map.geoObjects.add(geo);
	};
	/**
	 * Центрирование карты на центре границ
	 * @param bounds границы геометрии
	 */
	this.setBounds = function (bounds) {
		_map.setBounds(bounds);
	};

	/* callback для события 'Регионы загружены' */
	var onRegionLoaded = function (data) {
		_map.geoObjects.add(data.get('regions'));
		_this.renderMask(data.get('regions'));
	};

	/* Отрисовка тумана (т.е. оставить на карте только Россию) */
	this.renderMask = function (regions) {
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

function getDefaultRegionsMaskOverlayOptions() {
	return {
		zIndex: 1,
		stroke: false,
		strokeColor: false,
		fillColor: 'FFF'
	};
}

function attachRegionHandlers(data, options) {
	var regions = data.geoObjects;
	if (options.region_randomFillColor) {
		setRandomColors(regions);
	}
	if (options.region_hover) {
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