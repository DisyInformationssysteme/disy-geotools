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

import static org.geotools.gml.SubHandler.GEOMETRY_END;
import static org.geotools.gml.SubHandler.GEOMETRY_START;
import static org.geotools.gml.SubHandler.GEOMETRY_SUB;

import org.geotools.gml.GMLHandlerGeometry;
import org.geotools.gml.GMLHandlerJTS;
import org.geotools.gml.SubHandler;
import org.xml.sax.SAXException;

/**
 * LEVEL2 saxGML4j GML filter: translates coordinates and GML events into OGC simple types.
 * <p>
 * This filter simply reads in the events and coordinates passed to it by its GMLFilterDocument
 * child and converts them into JTS objects. Note that it passes through anything not specifically
 * sent to it by GMLFilterDocument (i.e. more or less everything not in geometry.xsd). The parent of
 * this filter must implement GMLHandlerJTS in order to receive the JTS objects passed by this
 * filter.
 * </p>
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL:
 *         http://svn.osgeo.org/geotools/tags/2.5.4/modules/library/main/src/main/java/org/geotools
 *         /gml/GMLFilterGeometry.java $
 * @version $Id: GMLFilterGeometry.java 30648 2008-06-12 19:22:35Z acuster $
 */
public class GMLFilterGeometry extends org.xml.sax.helpers.XMLFilterImpl
    implements
    GMLHandlerGeometry {
  /** Handler for the JTS elements generated by this filter. */
  private final GMLHandlerJTS parent;

  /** Factory for the JTS geometries. */
  private final org.locationtech.jts.geom.GeometryFactory geometryFactory =
      new org.locationtech.jts.geom.GeometryFactory();

  /** Factory for the GML geometry type subhandlers. */
  private final SubHandlerFactory handlerFactory = new SubHandlerFactory();

  /** Generic GML geometry type subhandler. */
  private SubHandler currentHandler;

  /**
   * Constructor with parent, which must implement GMLHandlerJTS.
   *
   * @param parent The parent of this filter.
   */
  public GMLFilterGeometry(final GMLHandlerJTS parent) {
    super();
    this.parent = parent;
  }

  /**
   * Manages the start of a new main or sub geometry. This method looks at the status of the current
   * handler and either returns a new sub-handler (if the last one was successfully returned
   * already) or passes the element start notification along to the current handler as a sub
   * geometry notice.
   *
   * @param localName The local name of the geometry, which corresponds to an OGC simple feature
   *          type.
   * @param atts The attributes of the geometry, including SRID, etc.
   * @throws SAXException parser error.
   */
  @Override
  public void geometryStart(final String localName, final org.xml.sax.Attributes atts)
      throws SAXException {
    String srs = null;
    for (int i = 0; i < atts.getLength(); i++) {
      final String NAME = atts.getQName(i);
      if ("srs".equalsIgnoreCase(NAME)) {
        srs = atts.getValue(i);
      }
    }
    if (currentHandler == null) {
      currentHandler = handlerFactory.create(localName);
    }
    else {
      currentHandler.subGeometry(localName, GEOMETRY_START);
    }
    currentHandler.setSRS(srs);
  }

  /**
   * Manages the end of a new main or sub geometry. This method looks at the status of the current
   * handler and either returns the finished JTS object to its parent or passes the element end
   * notification along to the current handler as a sub geometry notice.
   *
   * @param localName The local name of the geometry, which corresponds to an OGC simple feature
   *          type.
   * @throws SAXException parser error.
   */
  @Override
  public void geometryEnd(final String localName) throws SAXException {
    if (currentHandler.isComplete(localName)) {
      parent.geometry(currentHandler.create(geometryFactory));
      currentHandler = null;
    }
    else {
      currentHandler.subGeometry(localName, GEOMETRY_END);
    }
  }

  /**
   * Manages a sub geometry, which simply means always pass it to the current content handler as a
   * sub.
   *
   * @param localName The local name of the geometry, which corresponds to an OGC simple feature
   *          type.
   * @throws SAXException parser error.
   */
  @Override
  public void geometrySub(final String localName) throws SAXException {
    currentHandler.subGeometry(localName, GEOMETRY_SUB);
  }

  /**
   * Gets a coordinate from the child and passes it to the current handler as an add request.
   *
   * @param x The X coordinate of the received coordinate.
   * @param y The Y coordinate of the received coordinate.
   * @throws SAXException parser error.
   */
  @Override
  public void gmlCoordinates(final double x, final double y) throws SAXException {
    currentHandler.addCoordinate(new org.locationtech.jts.geom.Coordinate(x, y));
  }

  /**
   * Gets a coordinate from the child and passes it to the current handler as an add request.
   *
   * @param x The X coordinate of the received coordinate.
   * @param y The Y coordinate of the received coordinate.
   * @param z The Z coordinate of the received coordinate.
   * @throws SAXException parser error.
   */
  @Override
  public void gmlCoordinates(final double x, final double y, final double z) throws SAXException {
    currentHandler.addCoordinate(new org.locationtech.jts.geom.Coordinate(x, y, z));
  }

  /**
   * Checks for GML element start and - if not a coordinates element - sends it directly on down the
   * chain to the appropriate parent handler. If it is a coordinates (or coord) element, it uses
   * internal methods to set the current state of the coordinates reader appropriately.
   *
   * @param namespaceURI The namespace of the element.
   * @param localName The local name of the element.
   * @param qName The full name of the element, including namespace prefix.
   * @param atts The element attributes.
   * @throws SAXException Some parsing error occurred while reading coordinates.
   */
  @Override
  public void startElement(
      final String namespaceURI,
      final String localName,
      final String qName,
      final org.xml.sax.Attributes atts) throws SAXException {
    parent.startElement(namespaceURI, localName, qName, atts);
  }

  /**
   * Reads the only internal characters read by pure GML parsers, which are coordinates. These
   * coordinates are sent to the coordinates reader class which interprets them appropriately,
   * depending on its current state.
   *
   * @param ch Raw coordinate string from the GML document.
   * @param start Beginning character position of raw coordinate string.
   * @param length Length of the character string.
   * @throws SAXException Some parsing error occurred while reading coordinates.
   */
  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    parent.characters(ch, start, length);
  }

  /**
   * Checks for GML element end and - if not a coordinates element - sends it directly on down the
   * chain to the appropriate parent handler. If it is a coordinates (or coord) element, it uses
   * internal methods to set the current state of the coordinates reader appropriately.
   *
   * @param namespaceURI The namespace of the element.
   * @param localName The local name of the element.
   * @param qName The full name of the element, including namespace prefix.
   * @throws SAXException Some parsing error occurred while reading coordinates.
   */
  @Override
  public void endElement(final String namespaceURI, final String localName, final String qName)
      throws SAXException {
    parent.endElement(namespaceURI, localName, qName);
  }
}
