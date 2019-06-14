/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.patch;

import java.util.List;

import org.geotools.gml.SubHandler;
import org.geotools.gml.SubHandlerBox;
import org.geotools.gml.SubHandlerLineString;
import org.geotools.gml.SubHandlerLinearRing;
import org.geotools.gml.SubHandlerPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the appropriate SubHandler element for a given OGC simple geometry type.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL:
 *         http://svn.osgeo.org/geotools/tags/2.5.4/modules/library/main/src/main/java/org/geotools
 *         /gml/SubHandlerFactory.java $
 * @version $Id: SubHandlerFactory.java 30648 2008-06-12 19:22:35Z acuster $
 */
public class SubHandlerFactory {
  private final static Logger logger = LoggerFactory.getLogger(SubHandlerPolygon.class);

  /** List of all valid OGC multi geometry types. */
  private static final List<String> BASE_GEOMETRY_TYPES =
      List.of("MultiPoint", "MultiLineString", "MultiPolygon");

  /**
   * Empty constructor.
   */
  SubHandlerFactory() {}

  /**
   * Creates a new SubHandler, based on the appropriate OGC simple geometry type. Note that some
   * types are aggregated into a generic 'multi' type.
   *
   * @param type Type of SubHandler to return.
   * @return DOCUMENT ME!
   * @task TODO: throw an exception, not return a null
   */
  public SubHandler create(final String type) {
    logger.debug("create " + type);
    if (type.equals("Point")) {
      return new SubHandlerPoint();
    }
    if (type.equals("LineString")) {
      return new SubHandlerLineString();
    }
    if (type.equals("LinearRing")) {
      return new SubHandlerLinearRing();
    }
    if (type.equals("Polygon")) {
      return new SubHandlerPolygon();
    }
    if (type.equals("Box")) {
      return new SubHandlerBox();
    }
    if (BASE_GEOMETRY_TYPES.contains(type)) {
      return new SubHandlerMulti();
    }
    return null;
  }
}
