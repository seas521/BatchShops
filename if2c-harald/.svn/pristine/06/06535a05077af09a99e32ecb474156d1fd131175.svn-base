package com.if2c.harald.job;

// this is the example code that processes the annotation
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.quartz.DisallowConcurrentExecution;

import com.if2c.harald.db.DBConf;
@DisallowConcurrentExecution
public class UseCustomAnnotation {
	public static void main(String[] args) {
		Class<SendEmailJob> classObject = SendEmailJob.class;
		readAnnotation(classObject);
	}

	static void readAnnotation(AnnotatedElement element) {
		try {
			System.out.println("Annotation element values: \n");
			if (element.isAnnotationPresent(DBConf.class)) {
				// getAnnotation returns Annotation type
				Annotation singleAnnotation = element
						.getAnnotation(DBConf.class);
				DBConf header = (DBConf) singleAnnotation;
				System.out.println(header.url());
				System.out.println(header.userName());
				System.out.println(header.password());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}