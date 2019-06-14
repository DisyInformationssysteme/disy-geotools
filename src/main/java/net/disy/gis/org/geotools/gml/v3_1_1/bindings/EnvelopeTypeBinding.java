//Copyright (c) 2013 by Disy Informationssysteme GmbH
package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import org.geotools.gml2.SrsSyntax;
import org.geotools.xsd.Configuration;

// NOT_PUBLISHED
public class EnvelopeTypeBinding extends org.geotools.gml3.bindings.EnvelopeTypeBinding {

  public EnvelopeTypeBinding(Configuration config) {
    super(config, SrsSyntax.EPSG_CODE);
  }

}
