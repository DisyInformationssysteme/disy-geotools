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

import java.util.ArrayList;
import java.util.List;

import org.geotools.gml.SubHandler;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a MultiPoint, MultiLineString, or MultiPolygon geometry as required by the internal
 * functions.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 * @source $URL:
 *         http://svn.osgeo.org/geotools/tags/2.5.4/modules/library/main/src/main/java/org/geotools
 *         /gml/SubHandlerMulti.java $
 * @version $Id: SubHandlerMulti.java 30648 2008-06-12 19:22:35Z acuster $
 */
public class SubHandlerMulti extends SubHandler {
  private final static Logger logger = LoggerFactory.getLogger(SubHandlerPolygon.class);

  /**
   * Remembers the list of all possible sub (base) types for this multi type.
   */
  private static final List<String> BASE_GEOMETRY_TYPES = List.of("Point", "LineString", "Polygon");

  /** Geometry factory to return the multi type. */
  private final GeometryFactory geometryFactory = new GeometryFactory();

  /** Handler factory to return the sub type. */
  private final SubHandlerFactory handlerFactory = new SubHandlerFactory();

  /** Creates a SubHandler for the current sub type. */
  private SubHandler currentHandler;

  /** Stores list of all sub types. */
  private final List<Geometry> geometries = new ArrayList<>();

  /** Remembers the current sub type (ie. Line, Polygon, Point). */
  private String internalType;

  /** Remembers whether or not the internal type is set already. */
  private boolean internalTypeSet = false;

  /**
   * Empty constructor.
   */
  SubHandlerMulti() {
  }

  /**
   * Handles all internal (sub) geometries.
   *
   * @param message The sub geometry type found.
   * @param type Whether or not it is at a start or end.
   */
  @Override
  public void subGeometry(final String message, final int type) {
    logger.debug("subGeometry message = " + message + " type = " + type);

    // if the internal type is not yet set, set it
    if (!internalTypeSet && BASE_GEOMETRY_TYPES.contains(message)) {
      internalType = message;
      internalTypeSet = true;
      logger.debug("Internal type set to " + message);
    }

    // if the internal type is already set, then either:
    // create a new handler, if at start of geometry, or
    // return the completed geometry, if at the end of it
    if (message.equals(internalType)) {
      if (type == GEOMETRY_START) {
        currentHandler = handlerFactory.create(internalType);
      }
      else if (type == GEOMETRY_END) {
        geometries.add(currentHandler.create(geometryFactory));
      }
      else if (type == GEOMETRY_SUB) {
        currentHandler.subGeometry(message, type);
      }
    }
    else {
      currentHandler.subGeometry(message, type);
      logger.debug(internalType + " != " + message);
    }
  }

  /**
   * Adds a coordinate to the current internal (sub) geometry.
   *
   * @param coordinate The coordinate.
   */
  @Override
  public void addCoordinate(final Coordinate coordinate) {
    currentHandler.addCoordinate(coordinate);
  }

  /**
   * Determines whether or not it is time to return this geometry.
   *
   * @param message The geometry element that prompted this check.
   * @return DOCUMENT ME!
   */
  @Override
  public boolean isComplete(final String message) {
    return message.equals("Multi" + internalType);
  }

  /**
   * Returns a completed multi type.
   *
   * @param geometryFactory The factory this method should use to create the multi type.
   * @return Appropriate multi geometry type.
   */
  @Override
  public Geometry create(final GeometryFactory geometryFactory) {
    if (internalType.equals("Point")) {
      final Point[] pointArray = GeometryFactory.toPointArray(geometries);
      final MultiPoint multiPoint = geometryFactory.createMultiPoint(pointArray);
      multiPoint.setUserData(getSRS());
      multiPoint.setSRID(getSRID());
      logger.debug("created " + multiPoint);
      return multiPoint;
    }
    else if (internalType.equals("LineString")) {
      final LineString[] lineStringArray = GeometryFactory.toLineStringArray(geometries);
      final MultiLineString multiLineString = geometryFactory
          .createMultiLineString(lineStringArray);
      multiLineString.setUserData(getSRS());
      multiLineString.setSRID(getSRID());
      logger.debug("created " + multiLineString);
      return multiLineString;
    }
    else if (internalType.equals("Polygon")) {
      final Polygon[] polygonArray = GeometryFactory.toPolygonArray(geometries);
      final MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygonArray);
      multiPolygon.setUserData(getSRS());
      multiPolygon.setSRID(getSRID());
      logger.debug("created " + multiPolygon);
      return multiPolygon;
    }
    else {
      return null;
    }
  }
}
