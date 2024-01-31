/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org (C) 2002-2008, Open Source
 * Geospatial Foundation (OSGeo) This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.v2;

import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.api.feature.type.FeatureType;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GML;
import org.geotools.gml2.bindings.GMLAbstractFeatureCollectionBaseTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractFeatureTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractGeometryCollectionBaseTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractGeometryTypeBinding;
import org.geotools.gml2.bindings.GMLBoundingShapeTypeBinding;
import org.geotools.gml2.bindings.GMLBoxTypeBinding;
import org.geotools.gml2.bindings.GMLCoordTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.gml2.bindings.GMLFeatureAssociationTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryAssociationTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryCollectionTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringMemberTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringTypeBinding;
import org.geotools.gml2.bindings.GMLLinearRingMemberTypeBinding;
import org.geotools.gml2.bindings.GMLLinearRingTypeBinding;
import org.geotools.gml2.bindings.GMLMultiGeometryPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiLineStringPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiLineStringTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPointPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPointTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPolygonPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPolygonTypeBinding;
import org.geotools.gml2.bindings.GMLNullTypeBinding;
import org.geotools.gml2.bindings.GMLPointMemberTypeBinding;
import org.geotools.gml2.bindings.GMLPointPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLPointTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonMemberTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonTypeBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xsd.Configuration;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.picocontainer.MutablePicoContainer;

import net.disy.gis.org.geotools.gml.v2.bindings.GMLAbstractFeatureCollectionTypeBinding;

/**
 * Configuration used by gml2 parsers.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-gml2/src/main/java/org/geotools/gml2/GMLConfiguration.java $
 */
public class ModifiedGMLConfiguration extends Configuration {

  /**
   * Property which controls whether encoded features should include bounds.
   */
  public static final QName NO_FEATURE_BOUNDS = new QName("org.geotools.gml", "noFeatureBounds");

  /**
   * Boolean property which controls whether the FeatureCollection should be encoded with multiple
   * featureMember as opposed to a single featureMembers
   */
  public static final QName ENCODE_FEATURE_MEMBER =
      new QName("org.geotools.gml", "encodeFeatureMember");
  private final FeatureTypeCache featureTypeCache = new FeatureTypeCache();

  /**
   * Creates the new gml configuration, with a dependency on {@link XLINKConfiguration}
   */
  public ModifiedGMLConfiguration(final FeatureType... featureTypes) {
    super(GML.getInstance());

    for (FeatureType featureType : featureTypes) {
      featureTypeCache.put(featureType);
    }

    //add xlink dependency
    addDependency(new XLINKConfiguration());
  }

  @Override
  public void registerBindings(Map bindings) {
    //geometry
    bindings.put(GML.AbstractGeometryCollectionBaseType, GMLAbstractGeometryCollectionBaseTypeBinding.class);
    bindings.put(GML.AbstractGeometryType, GMLAbstractGeometryTypeBinding.class);
    bindings.put(GML.BoxType, GMLBoxTypeBinding.class);
    bindings.put(GML.CoordinatesType, GMLCoordinatesTypeBinding.class);
    bindings.put(GML.CoordType, GMLCoordTypeBinding.class);
    bindings.put(GML.GeometryAssociationType, GMLGeometryAssociationTypeBinding.class);
    bindings.put(GML.GeometryCollectionType, GMLGeometryCollectionTypeBinding.class);
    bindings.put(GML.LinearRingMemberType, GMLLinearRingMemberTypeBinding.class);
    bindings.put(GML.LinearRingType, GMLLinearRingTypeBinding.class);
    bindings.put(GML.LineStringMemberType, GMLLineStringMemberTypeBinding.class);
    bindings.put(GML.LineStringType, GMLLineStringTypeBinding.class);
    bindings.put(GML.MultiLineStringType, GMLMultiLineStringTypeBinding.class);
    bindings.put(GML.MultiPointType, GMLMultiPointTypeBinding.class);
    bindings.put(GML.MultiPolygonType, GMLMultiPolygonTypeBinding.class);
    bindings.put(GML.PointMemberType, GMLPointMemberTypeBinding.class);
    bindings.put(GML.PointType, GMLPointTypeBinding.class);
    bindings.put(GML.PolygonMemberType, GMLPolygonMemberTypeBinding.class);
    bindings.put(GML.PolygonType, GMLPolygonTypeBinding.class);

    //feature
    bindings.put(GML.AbstractFeatureCollectionBaseType, GMLAbstractFeatureCollectionBaseTypeBinding.class);
    bindings.put(GML.AbstractFeatureCollectionType, GMLAbstractFeatureCollectionTypeBinding.class);
    bindings.put(GML.AbstractFeatureType, GMLAbstractFeatureTypeBinding.class);
    bindings.put(GML.BoundingShapeType, GMLBoundingShapeTypeBinding.class);
    bindings.put(GML.FeatureAssociationType, GMLFeatureAssociationTypeBinding.class);
    bindings.put(GML.GeometryPropertyType, GMLGeometryPropertyTypeBinding.class);
    bindings.put(GML.LineStringPropertyType, GMLLineStringPropertyTypeBinding.class);
    bindings.put(GML.MultiGeometryPropertyType, GMLMultiGeometryPropertyTypeBinding.class);
    bindings.put(GML.MultiLineStringPropertyType, GMLMultiLineStringPropertyTypeBinding.class);
    bindings.put(GML.MultiPointPropertyType, GMLMultiPointPropertyTypeBinding.class);
    bindings.put(GML.MultiPolygonPropertyType, GMLMultiPolygonPropertyTypeBinding.class);
    bindings.put(GML.NullType, GMLNullTypeBinding.class);
    bindings.put(GML.PointPropertyType, GMLPointPropertyTypeBinding.class);
    bindings.put(GML.PolygonPropertyType, GMLPolygonPropertyTypeBinding.class);
  }

  /**
   * Configures the gml2 context.
   * <p>
   * The following classes are registered:
   * <ul>
   * <li>{@link CoordinateArraySequenceFactory} under {@link CoordinateSequenceFactory}
   * <li>{@link GeometryFactory}
   * <li>{@link FeatureTypeCache}
   * </ul>
   */
  @Override
  public void configureContext(MutablePicoContainer container) {
    super.configureContext(container);
    container.registerComponentInstance(featureTypeCache);
    container.registerComponentInstance(CoordinateSequenceFactory.class, CoordinateArraySequenceFactory.instance());
    container.registerComponentImplementation(GeometryFactory.class);
  }
}
