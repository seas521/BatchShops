//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.7 ��ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ���ʱ��: 2014.03.26 ʱ�� 10:03:43 AM CST 
//


package com.if2c.harald.migration.category.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ida.category.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GoodsAttributeValue_QNAME = new QName("", "GoodsAttributeValue");
    private final static QName _ExtendedAttributeValue_QNAME = new QName("", "ExtendedAttributeValue");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ida.category.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExtendedAttribute }
     * 
     */
    public ExtendedAttribute createExtendedAttribute() {
        return new ExtendedAttribute();
    }

    /**
     * Create an instance of {@link Category }
     * 
     */
    public Category createCategory() {
        return new Category();
    }

    /**
     * Create an instance of {@link GoodsAttribute }
     * 
     */
    public GoodsAttribute createGoodsAttribute() {
        return new GoodsAttribute();
    }

    /**
     * Create an instance of {@link CategoryRoot }
     * 
     */
    public CategoryRoot createCategoryRoot() {
        return new CategoryRoot();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "GoodsAttributeValue")
    public JAXBElement<String> createGoodsAttributeValue(String value) {
        return new JAXBElement<String>(_GoodsAttributeValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ExtendedAttributeValue")
    public JAXBElement<String> createExtendedAttributeValue(String value) {
        return new JAXBElement<String>(_ExtendedAttributeValue_QNAME, String.class, null, value);
    }

}
