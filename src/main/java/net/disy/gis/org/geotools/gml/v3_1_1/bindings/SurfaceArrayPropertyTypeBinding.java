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
import org.locationtech.jts.geom.Polygon;

/**
 * Binding object for the type http://www.opengis.net/gml:SurfaceArrayPropertyType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="SurfaceArrayPropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A container for an array of surfaces. The elements are always contained in the array property, referencing geometry elements or arrays of geometry elements is not supported.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:_Surface"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/bindings/SurfaceArrayPropertyTypeBinding.java $
 */
public class SurfaceArrayPropertyTypeBinding extends AbstractComplexBinding {
  /**
   * @generated
   */
  public QName getTarget() {
    return GML.SurfaceArrayPropertyType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  public Class getType() {
    return Polygon[].class;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    return BindingUtilities.getSurfaces(node).toArray(Polygon[]::new);
  }
}
