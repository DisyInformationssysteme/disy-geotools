/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org (C) 2002-2008, Open Source
 * Geospatial Foundation (OSGeo) This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

/**
 * Binding object for the type http://www.opengis.net/gml:MultiCurveType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="MultiCurveType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A MultiCurve is defined by one or more Curves, referenced through curveMember elements.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The members of the geometric aggregate can be specified either using the "standard" property or the array property style. It is also valid to use both the "standard" and the array property style in the same collection.
 *  NOTE: Array properties cannot reference remote geometry elements.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:curveMember"/&gt;
 *                  &lt;element minOccurs="0" ref="gml:curveMembers"/&gt;
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
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/bindings/MultiCurveTypeBinding.java $
 */
public class MultiCurveTypeBinding extends AbstractComplexBinding {
  GeometryFactory gf;

  public MultiCurveTypeBinding(GeometryFactory gf) {
    this.gf = gf;
  }

  /**
   * @generated
   */
  public QName getTarget() {
    return GML.MultiCurveType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  public Class getType() {
    //return MultiCurve.class;
    return MultiLineString.class;
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
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    return gf.createMultiLineString(BindingUtilities.getCurves(node).toArray(LineString[]::new));
  }

  @Override
  public Object getProperty(Object object, QName name) throws Exception {
    if (GML.curveMember.equals(name)) {
      MultiLineString multiCurve = (MultiLineString) object;
      LineString[] members = new LineString[multiCurve.getNumGeometries()];

      for (int i = 0; i < members.length; i++) {
        members[i] = (LineString) multiCurve.getGeometryN(i);
      }

      return members;
    }

    return null;
  }
}