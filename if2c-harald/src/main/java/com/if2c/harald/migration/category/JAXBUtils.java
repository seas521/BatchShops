package com.if2c.harald.migration.category;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.if2c.harald.migration.category.model.ObjectFactory;

public class JAXBUtils {

	private JAXBUtils() {

		throw new UnsupportedOperationException();
	}

	public static Object unmarshal(Class contextPath, InputStream xmlStream)
			throws JAXBException {

		JAXBContext jc = JAXBContext.newInstance(contextPath);
		Unmarshaller u = jc.createUnmarshaller();

		return u.unmarshal(xmlStream);
	}

	public static void marshal(Class contextPath, Object obj,
			OutputStream stream) throws JAXBException {

		JAXBContext jc = JAXBContext.newInstance(contextPath);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		m.marshal(obj, stream);
	}

	public static void main(String[] args) throws FileNotFoundException,
			JAXBException {
		// unmarshal
		InputStream is = new FileInputStream("category.xml");
		Object obj = JAXBUtils.unmarshal(ObjectFactory.class, is);

		// marshal
		OutputStream f = new FileOutputStream("category111.xml");
		JAXBUtils.marshal(ObjectFactory.class, obj, f);
	}
}