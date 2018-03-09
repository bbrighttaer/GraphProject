//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.21 at 05:37:30 PM CST 
//


package com.ming.graph.xsd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * Complex type for the <node> element.
 * <p>
 * <p>
 * <p>Java class for node.type complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="node.type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://graphml.graphdrawing.org/xmlns}desc" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *               &lt;element ref="{http://graphml.graphdrawing.org/xmlns}data"/&gt;
 *               &lt;element ref="{http://graphml.graphdrawing.org/xmlns}port"/&gt;
 *             &lt;/choice&gt;
 *             &lt;element ref="{http://graphml.graphdrawing.org/xmlns}graph" minOccurs="0"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;element ref="{http://graphml.graphdrawing.org/xmlns}locator"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://graphml.graphdrawing.org/xmlns}node.extra.attrib"/&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "node.type", propOrder = {
        "desc",
        "dataOrPort",
        "graph",
        "locator"
})
public class NodeType {

    protected String desc;
    @XmlElements({
            @XmlElement(name = "data", type = DataType.class),
            @XmlElement(name = "port", type = PortType.class)
    })
    protected List<Object> dataOrPort;
    protected GraphType graph;
    protected LocatorType locator;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String id;
    @XmlAttribute(name = "parse.indegree")
    protected BigInteger parseIndegree;
    @XmlAttribute(name = "parse.outdegree")
    protected BigInteger parseOutdegree;

    /**
     * Gets the value of the desc property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the dataOrPort property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataOrPort property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataOrPort().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataType }
     * {@link PortType }
     */
    public List<Object> getDataOrPort() {
        if (dataOrPort == null) {
            dataOrPort = new ArrayList<Object>();
        }
        return this.dataOrPort;
    }

    /**
     * Gets the value of the graph property.
     *
     * @return possible object is
     * {@link GraphType }
     */
    public GraphType getGraph() {
        return graph;
    }

    /**
     * Sets the value of the graph property.
     *
     * @param value allowed object is
     *              {@link GraphType }
     */
    public void setGraph(GraphType value) {
        this.graph = value;
    }

    /**
     * Gets the value of the locator property.
     *
     * @return possible object is
     * {@link LocatorType }
     */
    public LocatorType getLocator() {
        return locator;
    }

    /**
     * Sets the value of the locator property.
     *
     * @param value allowed object is
     *              {@link LocatorType }
     */
    public void setLocator(LocatorType value) {
        this.locator = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the parseIndegree property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getParseIndegree() {
        return parseIndegree;
    }

    /**
     * Sets the value of the parseIndegree property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setParseIndegree(BigInteger value) {
        this.parseIndegree = value;
    }

    /**
     * Gets the value of the parseOutdegree property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getParseOutdegree() {
        return parseOutdegree;
    }

    /**
     * Sets the value of the parseOutdegree property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setParseOutdegree(BigInteger value) {
        this.parseOutdegree = value;
    }

}
