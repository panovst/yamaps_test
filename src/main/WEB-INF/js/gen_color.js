/*
	Генератор цветов регионов
    Диапазон цветов от зеленый ("все нормально") до Красный ("все плохо")

Зеленый		008000		4DA64D
Желтый		FFFF00      FFFF4D
Красный		FF0000      FF4D4D

Диапазон зеленого (4DA64D): [E6F2E6, CCE6CC, B2D9B2, 99CC99, 80C080, 66B366, 4DA64D]
Диапазон желтого (FFFF4D): [FFFFE6, FFFFCC, FFFFB2, FFFF99, FFFF80, FFFF66, FFFF4D]
Диапазон красного (FF4D4D): [FFE6E6, FFCCCC, FFB2B2, FF9999, FF8080, FF6666, FF4D4D]
*/
var greenPalette = ['E6F2E6', 'CCE6CC', 'B2D9B2', '99CC99', '80C080', '66B366', '4DA64D'];
var yellowPalette = ['FFFFE6', 'FFFFCC', 'FFFFB2', 'FFFF99', 'FFFF80', 'FFFF66', 'FFFF4D'];
var redPalette = ['FFE6E6', 'FFCCCC', 'FFB2B2', 'FF9999', 'FF8080', 'FF6666', 'FF4D4D'];

function ColorGenerator () {
	this.activePalette = [].concat(greenPalette, yellowPalette, redPalette);
	this.inactivePalette = [];
	console.log(this.palette);

	this.getColor = function() {
		var index = Math.floor((Math.random()*this.activePalette.length)+0);
		var result = this.activePalette[index];
		this.inactivePalette.push(result);
		this.activePalette = jQuery.grep(this.activePalette, function(n, i){
			return (i != index);
		});
		if (this.activePalette.length == 0) {
			this.activePalette = this.inactivePalette;
			this.inactivePalette = [];
		}
		return result;
	}
}

