//Copyright (c) 2012 by Disy Informationssysteme GmbH
package net.disy.gis.org.geotools.gml.util;

import org.locationtech.jts.geom.CoordinateSequence;

// NOT_PUBLISHED
public interface ICoordinateSequenceUtilities {

  boolean isCounterClockwise(CoordinateSequence coordinateSequence);

  CoordinateSequence invert(CoordinateSequence coordinateSequence);
}
