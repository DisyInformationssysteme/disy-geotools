//Copyright (c) 2012 by Disy Informationssysteme GmbH
package net.disy.gis.org.geotools.gml.util;

import org.locationtech.jts.geom.CoordinateSequence;

public final class DelegatingCoordinateSequenceUtilities {

  private DelegatingCoordinateSequenceUtilities() {}

  private static final String DEFAULT_IMPLEMENTATION = "net.disy.gis.geotools.gml.adapter.CoordinateSequenceUtilitiesAdapter";

  private static final ICoordinateSequenceUtilities coordinateSequenceUtilities;

  static {
    try {
      final Class<?> clazz = Class.forName(DEFAULT_IMPLEMENTATION);
      coordinateSequenceUtilities = (ICoordinateSequenceUtilities) clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Can't create default implementation  " + DEFAULT_IMPLEMENTATION, e);
    }
  }

  public static boolean isCounterClockwise(CoordinateSequence coordinateSequence) {
    return coordinateSequenceUtilities.isCounterClockwise(coordinateSequence);
  }

  public static CoordinateSequence invert(CoordinateSequence coordinateSequence) {
    return coordinateSequenceUtilities.invert(coordinateSequence);
  }

}
