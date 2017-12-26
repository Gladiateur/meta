package gla.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class AnnotationPractice {
	@DataOptions(method="aa")
	public void aaa(){
		System.out.println("hello annotation");
	}
	
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AnnotationPractice annotationPractice = new AnnotationPractice();
		    //获取AnnotationPractice的Class实例
		   Annotation annotation = AnnotationPractice.class.getAnnotation(DataOptions.class);
		   System.out.println(annotation);
		  //  Class<AnnotationPractice> obj = Class.forName("gla.annotation.AnnotationPractice");
		    //获取需要处理的方法Method实例
		  //  Method method = c.getMethod("aaa", new Class[]{});
		    //判断该方法是否包含MyAnnotation注解
		   // if(method.isAnnotationPresent(DataOptions.class)){
		        //获取该方法的MyAnnotation注解实例
		    	//DataOptions myAnnotation = method.getAnnotation(DataOptions.class);
		        //执行该方法
		       // method.invoke(AnnotationPractice, new Object[]{});
		        //获取myAnnotation
		       // Method1 value1 = myAnnotation.method();
		       // System.out.println(value1);
		    //}
//		    for (Method method : method1) {
//		    	 //获取该方法的MyAnnotation注解实例
//		    	DataOptions myAnnotation = method.getAnnotation(DataOptions.class);
//		    	 //执行该方法
//			      method.invoke(annotationPractice);
//			    //获取myAnnotation
//			        Method1 value1 = myAnnotation.method();
//			        System.out.println(value1);
//		    	//获取方法上的所有注解
//			    Annotation[] annotations = method.getAnnotations();
//			    for(Annotation annotation : annotations){
//			        System.out.println(annotation.annotationType());
//			    }
//			}
		    
		  
	}
}
