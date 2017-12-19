/*
 * EntityWriter.java 17/12/18
 */

package gla.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sat4j.core.Core;
import com.sat4j.user.FrameRoot;

//do not delete this class! 17/12/18
/**
 * 注意格式化代码，按公式法进行。
 * 本类代码优化。
 * 公式法处理代码StringBuffer拼接部分。
 * modify配置部分。
 * 
 * @author Gladiateur
 *
 */
public class EntityWriter {
	private String tableName;
	private Map<String, String> fieldsMap = new HashMap<String, String>();
	private static Logger log = LogManager.getLogger(EntityWriter.class.getName());
	
	//更上一层做的事
	private static Map<String, String> beanMap = ConstsManager.getBeanMap();
	private static final String packagePath = beanMap.get("path");
	private static Map<String,String> connMap = ConstsManager.getConnMap();
	private static String dbName = connMap.get("dbName");
	private static String tables = beanMap.get("tables");
	private static int constructorStyle = Integer.parseInt(beanMap.get("constructor-style"));
	//----------------------------------------------
	
	/**
	 * 创建实体类文件的方法
	 * 
	 * 该方法用于自动创建用户指定的数据库里各表的实体类
	 * 创建成功后请刷新工程
	 *
	 * */
	public final void createFile(String fileName,String filepath){	
		String javaFileName=Core.captureName(fileName);
		System.out.println("/*****************************create the new javabean file*********************************/\n正在创建实体类："+javaFileName+".java");
		File directory = new File("");// 设定为当前文件夹
		String path = null;
		try {
			path = directory.getCanonicalPath()
					+ filepath;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		javaFileName = javaFileName+".java";
		File file = new File(f, javaFileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				System.out.println("javabean:"+javaFileName+"创建成功！");
			} catch (IOException e) {
				log.info("创建实体类文件时出现空指针异常，位置:com.sat4j.core.Core.createFile");
				e.printStackTrace();
			}
		}
	}
	
	//讲内容写入文件
	public void write(String javaFileName,String filepath,StringBuffer content){
		File file;
		FileOutputStream fop = null;
		String path;
		try {

			file = new File("");
			path = file.getCanonicalPath() + filepath + "\\" + javaFileName
					+ ".java";

			file = new File(path);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.toString().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out
					.println("\t写入成功！\n/*****************************finished*********************************/");

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
	
	//生成指定一张表的实体类
	public void autobean() throws SQLException{
		this.createFile(tableName, packagePath);
		this.write(tableName, packagePath, this.entityContent(tableName));
	}
	/*
	 * 这个方法必须用static修饰。
	 * 如果不这样做，那么需要在autobeans方法中按一下方式调用：
	 * this.autobean(table,constructorStyle);
	 * 而autobeans方法是静态方法。
	 */
	public static void autobean(String table,int constructorStyle) throws SQLException{
		EntityWriter ew = new EntityWriter(table);
		ew.autobean();
	}
	
	
	/*
	 * 从配置文件获取指定表名，如果是星号：*，则生成全部表的实体类文件。
	 * 各表之间用逗号分隔。（逗号又叫列表分隔符）
	 */
	//自动创建配置文件需要添加新的键！
	//将键动态化。
	public static void autobeans() throws SQLException, ClassNotFoundException{
		if(tables.equals("*")){
			//生成全部实体类
			//获取全部表名
			FrameRoot fr = new FrameRoot();
			fr.getConnection();
			String[] tableList = fr.getTablesName(dbName);
			for (String table : tableList) {
				autobean(table, constructorStyle);
			}
			
		}else{
			String[] tablesArray = Core.substringToArray(tables);
			for (String table : tablesArray) {
				log.info(table);
				autobean(table, constructorStyle);
			}
		}
		log.info("accomplished");
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		System.out.println(constructorStyle);
		autobeans();
		//getConfigTableNames();
//		EntityWriter ew = new EntityWriter("cart");
//		ew.autobean(0);
		
		//System.out.println(packageName);
	}
	
	/*
	 * 将  private int xxx;
	 * 的变量看做是Map中的key ,类型int看做是Map中的value
	 * 将所有<xxx,int>看成是Map<String,String>集合
	 * 回调至fun6
	 * 
	 * 这个方法是否有必要改为构造方法呢？（实验）
	 */
	private void fieldsMap(String table) throws SQLException{
		ResultSet rs = Core.selectResultSet(table);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount(); // 返回表的总列数
		for (int i = 1; i <= columnCount; i++) {
			fieldsMap.put(rsmd.getColumnName(i), rsmd.getColumnClassName(i));
		}
	}
	
	//是否需要无参构造方法
	/*
	 * 该构造方法构造一个EntityWriter的实例，同时初始化fieldsMap
	 */
	public EntityWriter(String tableName) throws SQLException{
		this.tableName=tableName;
		this.fieldsMap(tableName);
	}
	
	public void showFieldsMap(){
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			System.out.println(string.getKey()+"\t"+string.getValue());
		}
	}
	
	//从fieldsMap获取所有属性
	/*
	 * 使用Set集合是应为在同一实体类中属性名不能相同。
	 */
	public Set<String> getAttributes(){
		Set<String> set = new HashSet<String>();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			set.add(string.getKey());
		}
		return set;
	}
	//从fieldsMap获取所有类型
	/*
	 * 使用List是应为在同一类中不同的属性可能有相同的类型。
	 */
	public List<String> getTypes(){
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			list.add(string.getValue());
		}
		return list;
	}
	//从fieldsMap中获取指定属性的类型
	public String getType(String attribute){
		return (String)fieldsMap.get(attribute);	//如果参数在fieldsMap的key中不存在，那么是否有必要抛出异常
	}
	
	/**
	* 路径格式转报名格式
	* windows文件系统的路径格式为:com\a\b\c,在java中的路径格式为:com.a.b.c;所以需要将配置文件javabean
	* .properties 中的文件系统的路径格式转换为java中的路径格式。
	* */
	private final static String formatToPackage(String path) {
		String packName = null;
		if(path.trim().length()!=0){
			path = path.substring(5); // /path的完全格式是\src\xxx\xxx...
			packName = path.replace('\\', '.');
			//System.out.println("路径格式转包名格式：" + path + "--->" + packName);
		}
		return packName;
	}
	
	//从配置文件获取包名
	public String getPackageName(){
		Map<String, String> beanMap = ConstsManager.getBeanMap();
		String packageName = beanMap.get("path");
		return formatToPackage(packageName);
	}
	//加工为包名语句
	private StringBuffer declarePackage(String packageName){
		StringBuffer sb = new StringBuffer("package " );
		return sb.append(packageName+";");	//提常量
	}
	
	//注释语句部分
	private StringBuffer notes(){
		Map<String,String> configMap=ConstsManager.getConfigMap();
		StringBuffer sb = new StringBuffer("/**通过");
		sb.append(configMap.get("VERSION")).append("自动创建的实体类**/");//常量提出
		return sb;
	}
	
	//封装属性部分，modify预留，默认private
	private StringBuffer declareAttributes(){
		final String modify = "private ";	//常量从配置文件获取
		StringBuffer sb = new StringBuffer();	
		/*
		 * 某一张表的实体类
		 * private 部分
		 * Modify word选择：default private protected public,默认private
		 * xxx.modify();
		 */
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			sb.append("\t"+modify.concat(string.getValue()+" ")).append(string.getKey()+";\n");
		}
		return sb;
	}
	
	/**
	 * 关于首字母，若已经是大写的，比如"P"，那么就不需要执行char[i]-32;
	 * 否则，结果首字母会会变为"0"，这样做不是预期效果。
	 * captureName方法应该先对首字母进行判断，判断是否是小写。
	 * 该方法功能是对一个字符串的首字母进行判断，判断其是否为小写，若是则返回true,若不是则返回false
	 * 在ASCII码中97-122号为26个小写英文字母，据此来判断单个字符是否为小写。
	 * 这里说明为什么需要这个方法：因为在以前的SAT4j版本中，没有这个方法时，出现了一个小错误,
	 * 当时数据库中的某张表的表名的首字母是"P",就是说它已经是大写的了,在执行了方法captureName后出现了不符合预期的结果。
	 * 因为不知道用户的数据库中的表名的首字母是否是大写，而实体类的类名是按照表名生成的并且java中的命名规范要求类名
	 * 首字母必须大写,所以要先判断表名的首字母是否是小写,若是,则执行首字母大写的方法captureName,否则不执行。
	 * 
	 * @param table 数据库中的表名
	 * */
	public final static boolean isLowwer(String table) {
		byte[] b = table.getBytes();
		if (b[0] >= 97 && b[0] <= 122) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 首字母大写
	 * 
	 * 因为数据库不区分大小写，而java严格区分大小写，所以不能直接根据遍历的表名数组来创建java文件
	 * 而是把遍历表名数组得到的表名经过首字母大写处理后再去创建实体类文件，这样符合java的命名规范。
	 * */
	public final static String captureName(String name) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// return name;
		if(isLowwer(name)==true){
			char[] cs = name.toCharArray();
			cs[0] -= 32;
			return String.valueOf(cs);
		}else{
			return name;
		}

	}
	//getter,setter方法分开，两个可选，默认都生成
	//getter方法
	private StringBuffer declareGetterMethods(){
		StringBuffer sb = new StringBuffer();
		final String modify = "\tpublic ";	//常量提出
		final String headName = "get";
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			String result = string.getKey();
			String methodName = headName.concat(captureName(result));
			sb.append(modify);
			sb.append(string.getValue().concat(" ")).append(methodName);
			sb.append("(){\n");
			sb.append("\t\treturn ");
			sb.append(result+";\n");
			sb.append("\t}\n");
		}
		return sb;
	}
	//setter方法
	private StringBuffer declareSetterMethods(){
		StringBuffer sb = new StringBuffer();
		final String modify = "\tpublic ";	//常量提出
		final String headName = "set";
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			String result = string.getKey();
			String type = string.getValue()+" ";
			String methodName = headName.concat(captureName(result));
			sb.append(modify);
			sb.append("void ");
			sb.append(methodName);
			sb.append("(");
			sb.append(type);
			sb.append(result);
			sb.append("){\n");
			sb.append("\t\tthis.");
			sb.append(result+"="+result+";\n");
			sb.append("\t}\n");
		}
		return sb;
	}
	
	//生成无参，全参构造器
	//给某个类生成无参构造器,可选
	private StringBuffer declareDefaultConstructor(String table){
		//先判断表是否存在
		final String modify = "\tpublic ";	//常量提出
		StringBuffer sb = new StringBuffer();
		sb.append(modify);
		sb.append(captureName(table+"(){\n"));
		sb.append("\t\tsuper();\n");
		sb.append("\t}\n");
		return sb;
	}
	//全参数构造器
	private StringBuffer decalreFullAttributesConstructor(String table){
		//先判断表是否存在
		final String modify = "\tpublic ";	//常量提出
		StringBuffer paramList = new StringBuffer();	//参数列表
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			paramList.append(string.getValue()+" ");
			paramList.append(string.getKey()+",");
		}
		//去掉最后一个逗号-->去掉最后一个字符
		int length = paramList.length();
		paramList.replace(0, length, paramList.substring(0, length-1));
		//System.out.println(paramList.toString());
		StringBuffer methodBody = new StringBuffer();
		methodBody.append("\t\tsuper();\n");
		Set<String> set = getAttributes();
		StringBuffer attributes = new StringBuffer();
		for (String attribute : set) {
			attributes.append("\t\tthis.");
			attributes.append(attribute);
			attributes.append("="+attribute+";\n");
		}
		methodBody.append(attributes);
		StringBuffer sb = new StringBuffer();
		sb.append(modify);
		sb.append(captureName(table)+"(");
		sb.append(paramList+"){\n");
		sb.append(methodBody);
		sb.append("\t}\n");
		return sb;
	}
	
	//拼接类文件中的全部内容
	//是否生成构造器，生成哪种：0-不生成构造器，1-生成无参默认构造器，2-生成无参和全参两个构造器
	public StringBuffer entityContent(String table){
		StringBuffer sb = new StringBuffer();
		sb.append(declarePackage(getPackageName()));
		sb.append("\n\n");
		sb.append(notes());
		sb.append("\n\n");
		sb.append("public class ");
		sb.append(captureName(table));
		sb.append("{\n\n");
		sb.append(declareAttributes());
		sb.append("\n");
		sb.append(declareGetterMethods());
		sb.append("\n");
		sb.append(declareSetterMethods());
		switch (constructorStyle) {
		case 0:
			break;
		case 1:
			sb.append("\n");
			sb.append(declareDefaultConstructor(table));
			break;
		case 2:
			sb.append("\n");
			sb.append(declareDefaultConstructor(table));
			sb.append("\n");
			sb.append(decalreFullAttributesConstructor(table));
			break;
		}
		sb.append("}");
		return sb;
	}
	
//	public static void main(String[] args) throws SQLException {
//		EntityWriter ew = new EntityWriter("user");
//		//ew.fieldMap("user");
//		//ew.showFieldsMap();
//		System.out.println(ew.entityContent("user", 0));
//		//System.out.println(ew.getPackageName());
//		//System.out.println(ew.decalreFullAttributesConstructor("user").toString());
//		//System.out.println(ew.declareDefaultConstructor("user").toString());
//		//System.out.println(ew.declareSetterMethods().toString());
//		//System.out.println(ew.declareGetterMethods().toString());
////		System.out.println(	ew.getPackageName());
////		System.out.println(ew.declarePackage(ew.getPackageName()));
////		System.out.println(ew.notes());
////		System.out.println(ew.declareAttributes().toString());
//		//		Set<String> set = ew.getAttributes();
////		for (String string : set) {
////			System.out.println(string);
////		}
////		List<String> list = ew.getTypes();
////		for (String string : list) {
////			System.out.println(string);
////		}
////		System.out.println("Indicator:"+ew.getType("role"));
//	}
		
}
