//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.21 at 05:37:30 PM CST 
//


package com.ming.graph.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for key.for.type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="key.for.type"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="all"/&gt;
 *     &lt;enumeration value="graphml"/&gt;
 *     &lt;enumeration value="graph"/&gt;
 *     &lt;enumeration value="node"/&gt;
 *     &lt;enumeration value="edge"/&gt;
 *     &lt;enumeration value="hyperedge"/&gt;
 *     &lt;enumeration value="port"/&gt;
 *     &lt;enumeration value="endpoint"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "key.for.type")
@XmlEnum
public enum KeyForType {

    @XmlEnumValue("all")
    ALL("all"),
    @XmlEnumValue("graphml")
    GRAPHML("graphml"),
    @XmlEnumValue("graph")
    GRAPH("graph"),
    @XmlEnumValue("node")
    NODE("node"),
    @XmlEnumValue("edge")
    EDGE("edge"),
    @XmlEnumValue("hyperedge")
    HYPEREDGE("hyperedge"),
    @XmlEnumValue("port")
    PORT("port"),
    @XmlEnumValue("endpoint")
    ENDPOINT("endpoint");
    private final String value;

    KeyForType(String v) {
        value = v;
    }

    public static KeyForType fromValue(String v) {
        for (KeyForType c : KeyForType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public String value() {
        return value;
    }

}
