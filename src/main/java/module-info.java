module net.disy.geotools {
  requires java.logging;
  requires java.xml;

  requires slf4j.api;

  requires org.eclipse.xsd;

  requires org.locationtech.jts;

  requires org.geotools.main;
  requires org.geotools.opengis;
  requires org.geotools.metadata;
  requires org.geotools.xml;
  requires org.geotools.xsd.xsd_core;
  requires org.geotools.xsd.xsd_gml2;
  requires org.geotools.xsd.xsd_gml3;

  requires picocontainer;

  exports net.disy.gis.org.geotools.gml.patch;
  exports net.disy.gis.org.geotools.gml.util;
  exports net.disy.gis.org.geotools.gml.v2;
  exports net.disy.gis.org.geotools.gml.v2.bindings;
  exports net.disy.gis.org.geotools.gml.v3_1_1;
  exports net.disy.gis.org.geotools.gml.v3_1_1.bindings;
}
