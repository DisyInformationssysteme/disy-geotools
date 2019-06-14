// Copyright (c) 2011 by Disy Informationssysteme GmbH
package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geotools.xsd.Node;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

public class BindingUtilities {

  @SuppressWarnings("unchecked")
  public static List<Polygon> getSurfaces(Node node) {
    List<Polygon> surfaces = node.getChildValues(Polygon.class);
    if (!surfaces.isEmpty()) {
      return surfaces;
    }
    List<Polygon[]> surfaceArrays = node.getChildValues(Polygon[].class);
    if (!surfaceArrays.isEmpty()) {
      final ArrayList<Polygon> polygonsFormArray = new ArrayList<Polygon>();
      for (Polygon[] polygons : surfaceArrays) {
        polygonsFormArray.addAll(Arrays.asList(polygons));
      }
      return polygonsFormArray;
    }
    final ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    List<MultiPolygon> multiPolygons = node.getChildValues(MultiPolygon.class);
    for (MultiPolygon multiPolygon : multiPolygons) {
      multiPolygon.getNumGeometries();
      for (int i = 0; i < multiPolygon.getNumGeometries(); ++i) {
        polygons.add((Polygon) multiPolygon.getGeometryN(i));
      }
    }
    return polygons;
  }

  @SuppressWarnings("unchecked")
  public static List<LineString> getCurves(Node node) {
    List<LineString> curves = node.getChildValues(LineString.class);
    if (!curves.isEmpty()) {
      return curves;
    }
    List<LineString[]> curveArrays = node.getChildValues(LineString[].class);
    if (!curveArrays.isEmpty()) {
      return curveArrays.stream().flatMap(Stream::of).collect(Collectors.toList());
    }
    final ArrayList<LineString> lineStrings = new ArrayList<LineString>();
    List<MultiLineString[]> multiLineStringArrays = node.getChildValues(MultiLineString[].class);
    for (MultiLineString[] multiLineStringArray : multiLineStringArrays) {
      for (MultiLineString multiLineString : multiLineStringArray) {
        multiLineString.getNumGeometries();
        for (int i = 0; i < multiLineString.getNumGeometries(); ++i) {
          lineStrings.add((LineString) multiLineString.getGeometryN(i));
        }
      }
    }
    return lineStrings;
  }

}
