/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org (C) 2002-2008, Open Source
 * Geospatial Foundation (OSGeo) This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.patch;

// Java Topology Suite dependencies
import java.util.ArrayList;

import org.geotools.gml.SubHandler;
import org.geotools.gml.SubHandlerLinearRing;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.disy.gis.org.geotools.gml.util.DelegatingCoordinateSequenceUtilities;

/**
 * Creates a Polygon geometry.
 *
 * @author Ian Turton, CCG
 * @author Rob Hranac, Vision for New York
 * @source $URL:
 *         http://svn.osgeo.org/geotools/tags/2.5.4/modules/library/main/src/main/java/org/geotools
 *         /gml/SubHandlerPolygon.java $
 * @version $Id: SubHandlerPolygon.java 30648 2008-06-12 19:22:35Z acuster $
 */
public class SubHandlerPolygon extends SubHandler {

  private final static Logger logger = LoggerFactory.getLogger(SubHandlerPolygon.class);

  /** Factory for creating the Polygon geometry. */
  private final GeometryFactory geometryFactory = new GeometryFactory();

  /** Handler for the LinearRings that comprise the Polygon. */
  private SubHandlerLinearRing currentHandler = new SubHandlerLinearRing();

  /** Stores Polygon's outer boundary (shell). */
  private LinearRing outerBoundary = null;

  /** Stores Polygon's inner boundaries (holes). */
  private final ArrayList<LinearRing> innerBoundaries = new ArrayList<LinearRing>();

  /**
   * Remembers the current location in the parsing stream (inner or outer boundary).
   */
  private int location = 0;

  /** Indicates that we are inside the inner boundary of the Polygon. */
  private static final int INNER_BOUNDARY = 1;

  /** Indicates that we are inside the outer boundary of the Polygon. */
  private static final int OUTER_BOUNDARY = 2;

  /**
   * Creates a new instance of GMLPolygonHandler.
   */
  SubHandlerPolygon() {
  }

  /**
   * Catches inner and outer LinearRings messages and handles them appropriately.
   *
   * @param message Name of sub geometry located.
   * @param type Type of sub geometry located.
   */
  @Override
  public void subGeometry(final String message, final int type) {
    // if we have found a linear ring, either
    // add it to the list of inner boundaries if we are reading them
    // and at the end of the LinearRing
    // add it to the outer boundary if we are reading it and at the end of
    // the LinearRing
    // create a new linear ring, if we are at the start of a new linear ring
    switch (message) {
      case "LinearRing":
        if (type == GEOMETRY_END) {
          if (location == INNER_BOUNDARY) {
            LinearRing ring = (LinearRing) currentHandler.create(geometryFactory);
            /* it is important later that internal rings (holes) are
             * anticlockwise (counter clockwise) - so we reverse the
             * points if necessary
             */
            if (DelegatingCoordinateSequenceUtilities.isCounterClockwise(ring.getCoordinateSequence())) {
              logger.debug("good hole found");
              innerBoundaries.add(ring);
            } else {
              logger.debug("bad hole found - fixing");
              try {
                ring = geometryFactory.createLinearRing(DelegatingCoordinateSequenceUtilities
                    .invert(ring.getCoordinateSequence()));
                innerBoundaries.add(ring);
              } catch (final TopologyException e) {
                logger.warn("Caught Topology exception in GMLPolygonHandler");
                ring = null;
              }
            }
          } else if (location == OUTER_BOUNDARY) {
            /* it is important later that the outerboundary is
             * clockwise  - so we reverse the
             * points if necessary
             */
            outerBoundary = (LinearRing) currentHandler.create(geometryFactory);
            if (DelegatingCoordinateSequenceUtilities.isCounterClockwise(outerBoundary
                .getCoordinateSequence())) {
              logger.debug("bad outer ring - rebuilding");
              try {
                outerBoundary = geometryFactory
                    .createLinearRing(DelegatingCoordinateSequenceUtilities.invert(outerBoundary
                        .getCoordinateSequence()));
              } catch (final TopologyException e) {
                logger.warn("Caught Topology exception in GMLPolygonHandler");
                outerBoundary = null;
              }
            }
          }
        } else if (type == GEOMETRY_START) {
          currentHandler = new SubHandlerLinearRing();
        }
        break;
      case "outerBoundaryIs":
        //  or, if we are getting notice of an inner/outer boundary marker,
        // set current location appropriately
        logger.debug("new outer Boundary");
        location = OUTER_BOUNDARY;
        break;
      case "innerBoundaryIs":
        logger.debug("new InnerBoundary");
        location = INNER_BOUNDARY;
        break;
    }
  }

  /**
   * Adds a coordinate to the current LinearRing.
   *
   * @param coordinate Name of sub geometry located.
   */
  @Override
  public void addCoordinate(final Coordinate coordinate) {
    currentHandler.addCoordinate(coordinate);
  }

  /**
   * Determines whether or not the geometry is ready to be returned.
   *
   * @param message Name of GML element that prompted this check.
   * @return Flag indicating whether or not the geometry is ready to be returned.
   */
  @Override
  public boolean isComplete(final String message) {
    // the conditions checked here are that the endGeometry message that
    // prompted this check is a Polygon and that this Polygon has an outer
    // boundary; if true, then return the all go signal
    // otherwise, send this message to the subGeometry method for further
    // processing
    return message.equals("Polygon") && outerBoundary != null;
  }

  /**
   * Returns the completed OGC Polygon.
   *
   * @param geometryFactory Geometry factory to be used in Polygon creation.
   * @return Completed OGC Polygon.
   */
  @Override
  public Geometry create(final GeometryFactory geometryFactory) {
    final LinearRing[] rings = innerBoundaries.toArray(LinearRing[]::new);
    final Polygon polygon = geometryFactory.createPolygon(outerBoundary, rings);
    polygon.setUserData(getSRS());
    polygon.setSRID(getSRID());
    return polygon;
  }
}
