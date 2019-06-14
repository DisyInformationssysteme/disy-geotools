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
package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;

public class AbstractFeatureCollectionTypeBinding extends AbstractComplexBinding {

  @Override
  public QName getTarget() {
    return GML.AbstractFeatureCollectionType;
  }

  @Override
  public Class getType() {
    return Feature[].class;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   *
   * @generated modifiable
   */
  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    //featureMember
    @SuppressWarnings("unchecked")
    final List<SimpleFeature> featuresList = node.getChildValues(SimpleFeature.class);
    return featuresList.toArray(SimpleFeature[]::new);

  }

  @Override
  public Object getProperty(Object object, QName name) {
    //just return the features themselves
    if (GML.featureMember.equals(name)) {
      return object;
    }
    return null;
  }
}
