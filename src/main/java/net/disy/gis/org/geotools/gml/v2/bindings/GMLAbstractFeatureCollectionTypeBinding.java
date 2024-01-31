package net.disy.gis.org.geotools.gml.v2.bindings;

import java.util.List;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class GMLAbstractFeatureCollectionTypeBinding
    extends org.geotools.gml2.bindings.GMLAbstractFeatureCollectionTypeBinding {

  @Override
  public Class getType() {
    return SimpleFeature[].class;
  }

  @Override
  public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
    final List features = node.getChildValues(SimpleFeature.class);
    return features.toArray(SimpleFeature[]::new);
  }
}
