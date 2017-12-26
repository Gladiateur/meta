/*
 * BeanFileManager.java 17/12/23
 */

package gla.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sat4j.core.WriteFileManager;

public class BeanFileManager extends WriteFileManager{
	
	/**
	 * 初始化javabean配置文件
	 * */
	public static void initJavaBeanConfig() {
		System.out
				.println("初始化javabean配置文件");
		final String fileName = "javabean.properties";
		FileOutputStream fop = null;
		File file;
		StringBuffer sb = new StringBuffer();
		sb.append("#由SAT4j自动创建\n\n#path:实体类所在的包名,这里的书写格式比如：'com\\\\mycompany\\\\javabean',这个键的值可以为空\n");
		sb.append("path=\n\n#指定表\ntables=\n\n");
		sb.append("#构造器样式\nconstructor-style=0\n\n");
		sb.append("#这个键用于声明哪一张表需要验证身份，该键键值格式为：表名，列名1，列名2\nCheckTable=");
		
	
		String path;
		try {
			file = new File("");
			path = file.getCanonicalPath() + "\\src\\" + fileName;
			file = new File(path);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.exists()) {
				// 判断内容是否为空，如若空则写入，不为空则不写
				if (file.length() == 0) {
					fop = new FileOutputStream(path);
					byte[] contentInBytes = sb.toString().getBytes();
					fop.write(contentInBytes);
					fop.flush();
					fop.close();
					System.out.println("配置文件写入成功！");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
