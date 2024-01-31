/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org (C) 2002-2008, Open Source
 * Geospatial Foundation (OSGeo) This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; version 2.1 of the License. This library is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.disy.gis.org.geotools.gml.v3_1_1;

import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.api.feature.type.FeatureType;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.bindings.GMLCoordTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.AbstractRingPropertyTypeBinding;
import org.geotools.gml3.bindings.BoundingShapeTypeBinding;
import org.geotools.gml3.bindings.ComplexSupportXSAnyTypeBinding;
import org.geotools.gml3.bindings.CurveArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.CurveSegmentArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.DirectPositionTypeBinding;
import org.geotools.gml3.bindings.DoubleListBinding;
import org.geotools.gml3.bindings.FeatureArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.FeaturePropertyTypeBinding;
import org.geotools.gml3.bindings.IntegerListBinding;
import org.geotools.gml3.bindings.LineStringSegmentTypeBinding;
import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.geotools.gml3.bindings.LinearRingPropertyTypeBinding;
import org.geotools.gml3.bindings.LinearRingTypeBinding;
import org.geotools.gml3.bindings.LocationPropertyTypeBinding;
import org.geotools.gml3.bindings.MeasureTypeBinding;
import org.geotools.gml3.bindings.MultiGeometryPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiGeometryTypeBinding;
import org.geotools.gml3.bindings.MultiLineStringTypeBinding;
import org.geotools.gml3.bindings.MultiPointTypeBinding;
import org.geotools.gml3.bindings.NullTypeBinding;
import org.geotools.gml3.bindings.PointArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.PointTypeBinding;
import org.geotools.gml3.bindings.ReferenceTypeBinding;
import org.geotools.gml3.smil.SMIL20Configuration;
import org.geotools.gml3.smil.SMIL20LANGConfiguration;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xs.XS;
import org.geotools.xsd.Configuration;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.picocontainer.MutablePicoContainer;

import net.disy.gis.org.geotools.gml.v3_1_1.bindings.AbstractFeatureCollectionTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.AbstractFeatureTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.AbstractGeometryTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.CurvePropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.CurveTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.DirectPositionListTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.EnvelopeTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.GeometryPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.LineStringPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiCurvePropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiCurveTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiLineStringPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiPointPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiPolygonPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiPolygonTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiSurfacePropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.MultiSurfaceTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.PointPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.PolygonPatchTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.PolygonPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.PolygonTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.SurfaceArrayPropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.SurfacePropertyTypeBinding;
import net.disy.gis.org.geotools.gml.v3_1_1.bindings.SurfaceTypeBinding;

/**
 * Parser configuration for the gml3 schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/tags/2.6.5/modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/GMLConfiguration.java $
 */
public class GMLConfiguration extends Configuration {

  /**
   * Boolean property which controls whether encoded features should include bounds.
   */
  public static final QName NO_FEATURE_BOUNDS = org.geotools.gml2.GMLConfiguration.NO_FEATURE_BOUNDS;

  /**
   * Boolean property which controls whether the FeatureCollection should be encoded with multiple featureMember
   * as opposed to a single featureMembers
   */
  public static final QName ENCODE_FEATURE_MEMBER = org.geotools.gml2.GMLConfiguration.ENCODE_FEATURE_MEMBER;
  private final FeatureTypeCache featureTypeCache = new FeatureTypeCache();

  public GMLConfiguration(FeatureType... featureTypes) {
    super(GML.getInstance());
    for (FeatureType featureType : featureTypes) {
      featureTypeCache.put(featureType);
    }
    //add xlink cdependency
    addDependency(new XLINKConfiguration());

    //add smil depenedncy
    addDependency(new SMIL20Configuration());
    addDependency(new SMIL20LANGConfiguration());
  }

  @Override
  protected void registerBindings(Map bindings) {
    //Types
    bindings.put(GML.AbstractGeometryType, AbstractGeometryTypeBinding.class);
    bindings.put(GML.AbstractFeatureType, AbstractFeatureTypeBinding.class);

    bindings.put(GML.AbstractGeometryType, AbstractGeometryTypeBinding.class);

    bindings.put(GML.AbstractFeatureCollectionType, AbstractFeatureCollectionTypeBinding.class);
    bindings.put(GML.AbstractRingPropertyType, AbstractRingPropertyTypeBinding.class);
    bindings.put(GML.BoundingShapeType, BoundingShapeTypeBinding.class);
    //bindings.put(GML.COORDINATESTYPE,CoordinatesTypeBinding.class);
    bindings.put(GML.CoordinatesType, GMLCoordinatesTypeBinding.class);
    //bindings.put(GML.COORDTYPE,CoordTypeBinding.class);
    bindings.put(GML.CoordType, GMLCoordTypeBinding.class);
    bindings.put(GML.CurveArrayPropertyType, CurveArrayPropertyTypeBinding.class);
    bindings.put(GML.CurveType, CurveTypeBinding.class);
    bindings.put(GML.CurvePropertyType, CurvePropertyTypeBinding.class);
    bindings.put(GML.CurveSegmentArrayPropertyType, CurveSegmentArrayPropertyTypeBinding.class);
    bindings.put(GML.DirectPositionListType, DirectPositionListTypeBinding.class);
    bindings.put(GML.DirectPositionType, DirectPositionTypeBinding.class);
    bindings.put(GML.doubleList, DoubleListBinding.class);
    bindings.put(GML.EnvelopeType, EnvelopeTypeBinding.class);
    bindings.put(GML.FeatureArrayPropertyType, FeatureArrayPropertyTypeBinding.class);
    bindings.put(GML.FeaturePropertyType, FeaturePropertyTypeBinding.class);
    bindings.put(GML.GeometryPropertyType, GeometryPropertyTypeBinding.class);
    bindings.put(GML.integerList, IntegerListBinding.class);
    bindings.put(GML.LinearRingPropertyType, LinearRingPropertyTypeBinding.class);
    bindings.put(GML.LinearRingType, LinearRingTypeBinding.class);
    bindings.put(GML.LineStringPropertyType, LineStringPropertyTypeBinding.class);
    bindings.put(GML.LineStringSegmentType, LineStringSegmentTypeBinding.class);
    bindings.put(GML.LineStringType, LineStringTypeBinding.class);
    bindings.put(GML.LocationPropertyType, LocationPropertyTypeBinding.class);

    bindings.put(GML.MeasureType, MeasureTypeBinding.class);
    bindings.put(GML.MultiCurveType, MultiCurveTypeBinding.class);
    bindings.put(GML.MultiCurvePropertyType, MultiCurvePropertyTypeBinding.class);
    bindings.put(GML.MultiGeometryType, MultiGeometryTypeBinding.class);
    bindings.put(GML.MultiGeometryPropertyType, MultiGeometryPropertyTypeBinding.class);
    bindings.put(GML.MultiLineStringPropertyType, MultiLineStringPropertyTypeBinding.class);
    bindings.put(GML.MultiLineStringType, MultiLineStringTypeBinding.class);
    bindings.put(GML.MultiPointPropertyType, MultiPointPropertyTypeBinding.class);
    bindings.put(GML.MultiPointType, MultiPointTypeBinding.class);
    bindings.put(GML.MultiPolygonPropertyType, MultiPolygonPropertyTypeBinding.class);
    bindings.put(GML.MultiPolygonType, MultiPolygonTypeBinding.class);
    bindings.put(GML.MultiSurfaceType, MultiSurfaceTypeBinding.class);
    bindings.put(GML.MultiSurfacePropertyType, MultiSurfacePropertyTypeBinding.class);
    bindings.put(GML.NullType, NullTypeBinding.class);
    bindings.put(GML.PointArrayPropertyType, PointArrayPropertyTypeBinding.class);
    bindings.put(GML.PointPropertyType, PointPropertyTypeBinding.class);
    bindings.put(GML.PointType, PointTypeBinding.class);
    bindings.put(GML.PolygonPatchType, PolygonPatchTypeBinding.class);
    bindings.put(GML.PolygonPropertyType, PolygonPropertyTypeBinding.class);
    bindings.put(GML.PolygonType, PolygonTypeBinding.class);
    bindings.put(GML.ReferenceType, ReferenceTypeBinding.class);
    bindings.put(GML.SurfaceArrayPropertyType, SurfaceArrayPropertyTypeBinding.class);
    bindings.put(GML.SurfacePropertyType, SurfacePropertyTypeBinding.class);
    bindings.put(GML.SurfaceType, SurfaceTypeBinding.class);

    bindings.put(XS.ANYTYPE, ComplexSupportXSAnyTypeBinding.class);
  }

  /**
   * Configures the gml3 context.
   * <p>
   * The following factories are registered:
   * <ul>
   * <li>{@link CoordinateArraySequenceFactory} under {@link CoordinateSequenceFactory}
   * <li>{@link GeometryFactory}
   * </ul>
   * </p>
   */
  @Override
  public void configureContext(MutablePicoContainer container) {
    super.configureContext(container);

    container.registerComponentInstance(featureTypeCache);
    container.registerComponentInstance(new XSDIdRegistry());

    //factories
    container.registerComponentInstance(CoordinateSequenceFactory.class, CoordinateArraySequenceFactory.instance());
    container.registerComponentImplementation(GeometryFactory.class);
  }
}
