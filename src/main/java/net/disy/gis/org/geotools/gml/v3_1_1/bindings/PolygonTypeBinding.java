/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org (C) 2002-2008, Open Source
 * Geospatial Foundation (OSGeo) This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import net.disy.gis.org.geotools.gml.util.DelegatingCoordinateSequenceUtilities;

/**
 * Binding object for the type http://www.opengis.net/gml:PolygonType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="PolygonType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A Polygon is a special surface that is defined by a single surface patch. The boundary of this patch is coplanar and the polygon uses planar interpolation in its interior. It is backwards compatible with the Polygon of GML 2, GM_Polygon of ISO 19107 is implemented by PolygonPatch.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractSurfaceType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="gml:exterior"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:interior"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/bindings/PolygonTypeBinding.java $
 */
public class PolygonTypeBinding extends AbstractComplexBinding {
  GeometryFactory gFactory;

  public PolygonTypeBinding(GeometryFactory gFactory) {
    this.gFactory = gFactory;
  }

  /**
   * @generated
   */
  @Override
  public QName getTarget() {
    return GML.PolygonType;
  }

  @Override
  public int getExecutionMode() {
    return BEFORE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  @Override
  public Class getType() {
    return Polygon.class;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    //TODO: schema allows no exterior ring, but what the heck is that all about ?
    LinearRing exterior = (LinearRing) node.getChildValue("exterior");
    if (exterior == null) {
      // deegree server sometimes returns gml2 polygons
      LinearRing shell = (LinearRing) node.getChild("outerBoundaryIs").getValue();
      List innerRings = node.getChildren("innerBoundaryIs");
      LinearRing[] holes = new LinearRing[innerRings.size()];
      for (int i = 0; i < innerRings.size(); i++) {
        Node inode = (Node) innerRings.get(i);
        holes[i] = (LinearRing) inode.getValue();
      }
      return createPolygon(shell, holes);
    }
    LinearRing[] interior = null;
    if (node.hasChild("interior")) {
      List<Object> list = node.getChildValues("interior");
      interior = list.toArray(LinearRing[]::new);
    }
    return createPolygon(exterior, interior);
  }

  private Object createPolygon(LinearRing exterior, LinearRing[] interior) {
    LinearRing shell = checkShell(exterior);
    LinearRing[] holes = checkHoles(interior);
    return gFactory.createPolygon(shell, holes);
  }

  private LinearRing checkShell(LinearRing exterior) {
    if (DelegatingCoordinateSequenceUtilities.isCounterClockwise(exterior.getCoordinateSequence())) {
      return (LinearRing) exterior.reverse();
    }
    return exterior;
  }

  private LinearRing[] checkHoles(LinearRing[] interior) {
    if (interior == null) {
      return new LinearRing[0];
    }
    List<LinearRing> rings = new ArrayList<LinearRing>();
    for (LinearRing ring : interior) {
      if (DelegatingCoordinateSequenceUtilities.isCounterClockwise(ring.getCoordinateSequence())) {
        rings.add(ring);
        continue;
      }
      rings.add((LinearRing) ring.reverse());
    }
    return rings.toArray(LinearRing[]::new);
  }

  @Override
  public Object getProperty(Object object, QName name) throws Exception {
    Polygon polygon = (Polygon) object;

    if (name.equals(GML.exterior)) {
      return polygon.getExteriorRing();
    }

    if (name.equals(GML.interior)) {
      int n = polygon.getNumInteriorRing();

      if (n > 0) {
        LineString[] interior = new LineString[n];

        for (int i = 0; i < n; i++) {
          interior[i] = polygon.getInteriorRingN(i);
        }

        return interior;
      }
    }

    return null;
  }
}
