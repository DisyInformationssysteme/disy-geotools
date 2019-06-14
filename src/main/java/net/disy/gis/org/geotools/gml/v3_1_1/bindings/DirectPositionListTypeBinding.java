package net.disy.gis.org.geotools.gml.v3_1_1.bindings;

import org.geotools.gml3.GML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DirectPositionListTypeBinding
    extends
    org.geotools.gml3.bindings.DirectPositionListTypeBinding {

  @Override
  public Element encode(Object object, Document document, Element value) throws Exception {
//    value.setAttribute("gml:dimension", "2");
    Element encodedElement = super.encode(object, document, value);
    encodedElement.setAttributeNS(GML.NAMESPACE, "dimension", "2");
    return encodedElement;
  }

}
