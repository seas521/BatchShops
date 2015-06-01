//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.7 ��ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ���ʱ��: 2014.03.26 ʱ�� 10:03:43 AM CST 
//


package com.if2c.harald.migration.category.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element ref="{}Category" maxOccurs="unbounded"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{}GoodsAttribute" maxOccurs="unbounded"/>
 *           &lt;element ref="{}ExtendedAttribute" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="level" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}byte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "category",
    "goodsAttribute",
    "extendedAttribute"
})
@XmlRootElement(name = "Category")
public class Category {

    @XmlElement(name = "Category")
    protected List<Category> category;
    @XmlElement(name = "GoodsAttribute")
    protected List<GoodsAttribute> goodsAttribute;
    @XmlElement(name = "ExtendedAttribute")
    protected List<ExtendedAttribute> extendedAttribute;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "level", required = true)
    protected byte level;
    @XmlAttribute(name = "id", required = true)
    protected long id;

    /**
     * Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Category }
     * 
     * 
     */
    public List<Category> getCategory() {
        if (category == null) {
            category = new ArrayList<Category>();
        }
        return this.category;
    }

    /**
     * Gets the value of the goodsAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the goodsAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGoodsAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GoodsAttribute }
     * 
     * 
     */
    public List<GoodsAttribute> getGoodsAttribute() {
        if (goodsAttribute == null) {
            goodsAttribute = new ArrayList<GoodsAttribute>();
        }
        return this.goodsAttribute;
    }

    /**
     * ��ȡextendedAttribute���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ExtendedAttribute }
     *     
     */
    public List<ExtendedAttribute> getExtendedAttribute() {
    	  if (extendedAttribute == null) {
    		  extendedAttribute = new ArrayList<ExtendedAttribute>();
          }
          return this.extendedAttribute;
    }

    /**
     * ����extendedAttribute���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedAttribute }
     *     
     */
    public void setExtendedAttribute(List<ExtendedAttribute> value) {
        this.extendedAttribute = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * ��ȡlevel���Ե�ֵ��
     * 
     */
    public byte getLevel() {
        return level;
    }

    /**
     * ����level���Ե�ֵ��
     * 
     */
    public void setLevel(byte value) {
        this.level = value;
    }

    /**
     * ��ȡid���Ե�ֵ��
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * ����id���Ե�ֵ��
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

	public void setGoodsAttribute(List<GoodsAttribute> result) {
		goodsAttribute=result;
	}

}
