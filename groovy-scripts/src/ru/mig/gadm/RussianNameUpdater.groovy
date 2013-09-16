package ru.mig.gadm

import groovy.grape.Grape

class RussianNameUpdater {

	def init() {
		initMavenModule('postgresql', 'postgresql', '9.1-901.jdbc4');
		println "DONE"
//		initMavenModule('commons-lang', 'commons-lang', '2.6');
//		initMavenModule('org.apache.solr', 'solr-commons-csv', '3.5.0');

	}

	private void initMavenModule(String group, String artifact, String version) {
		def classLoader = this.getClass().getClassLoader();
		while (!classLoader.getClass().getName().equals("org.codehaus.groovy.tools.RootLoader")) {
			classLoader = classLoader.getParent()
		}
		Grape.grab(group:group, module:artifact, version:version,classLoader: classLoader)
	}
}
