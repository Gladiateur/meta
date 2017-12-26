/*
 * EntityWriter.java 17/12/18
 */

package gla.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
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
 * log4j配置文件，替换所有的syso。
 * 
 * 有个问题想了好几天没想明白：为什么配置文件会加载两遍?
 * 
 * 使用方法：老版sat4j需要继承FrameRoot去初始化配置文件
 * 新版的javabean.properties使用BeanFileManager来创建
 * 
 * 优化后的自动生成实体类的方法推荐使用本类中的静态方法autobeans().
 * 
 * @author Gladiateur
 */
public class EntityWriter implements Constant{
	private String tableName;
	private Map<String, String> fieldsMap = new HashMap<String, String>();
	private static Logger log = LogManager.getLogger(EntityWriter.class.getName());
	private static Connection conn = null;
	
	//以下信息仅获取一次
	private static final String VERSION;
	private static final String DATABASE_NAME;
	private static final String PACKAGE_PATH;
	private static final String TABLES;
	private static final int CONSTRUCTOR_STYLE;
	private static final boolean KEY_TABLES_FLAG;
	
	
	static{
		Map<String,String> configMap = ConstsManager.getConfigMap();
		VERSION = configMap.get("VERSION");
		Map<String,String> connMap = ConstsManager.getConnMap();
		DATABASE_NAME = connMap.get("dbName");
		Map<String, String> beanMap = ConstsManager.getBeanMap();
		PACKAGE_PATH = beanMap.get("path");
		TABLES = beanMap.get("tables");
		KEY_TABLES_FLAG = TABLES.equals("*") ? true : false;
		CONSTRUCTOR_STYLE = Integer.parseInt(beanMap.get("constructor-style"));
	}
	//----------------------------------------------
	
	/**
	 * 创建实体类文件的方法
	 * 
	 * 该方法用于自动创建用户指定的数据库里各表的实体类
	 * 创建成功后请刷新工程
	 *
	 * */
	public final void createJavaFile(String fileName,String filepath){	
		String javaFileName=Core.captureName(fileName);
		System.out.println("\n正在创建实体类："+javaFileName+".java");
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
				e.printStackTrace();
			}
		}
	}
	
	//将内容写入文件
	public void writeJavaFile(String javaFileName,String filepath,StringBuffer content){
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
			System.out.println("\t写入成功！\n");
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
	/**
	 * 可以这么用：
	 * EntityWriter ew = new EntityWriter("user");
	 *	ew.autobean();
	 * 但一般不会这么做，表名通过配置文件配置后直接使用方法autobeans即可。
	 * 尽管如此，这个方法我还是决定用public而不用private.
	 * 
	 * @throws SQLException
	 */
	public void autobean() throws SQLException{
		this.createJavaFile(tableName, PACKAGE_PATH);
		this.writeJavaFile(tableName, PACKAGE_PATH, this.entityContent(tableName));
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
	
	private static void autobeans0(String[] tables) throws SQLException{
		for (String table : tables) {
			log.debug(table);
			autobean(table, CONSTRUCTOR_STYLE);
		}
	}
	
	/*
	 * 从配置文件获取指定表名，如果是星号：*，则生成全部表的实体类文件。
	 * 各表之间用逗号分隔。（逗号又叫列表分隔符）
	 */
	//自动创建配置文件需要添加新的键！
	//将键动态化。
	public static void autobeans() throws SQLException, ClassNotFoundException{
		String[] tableList;
		if(KEY_TABLES_FLAG){
			FrameRoot fr = new FrameRoot();
			conn = fr.getConnection();
			tableList = fr.getTablesName(DATABASE_NAME);//获取全部表名
		}else{
			tableList = Core.substringToArray(TABLES);//获取部分表名
		}
		autobeans0(tableList);
		if(conn != null){
			conn.close();
		}
	
		log.info("accomplished");
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
		}
		return packName;
	}
	
	//从配置文件获取包名
	public String getPackageName(){
		return formatToPackage(PACKAGE_PATH);
	}
	//加工为包名语句
	private StringBuffer declarePackage(String packageName){
		TextFormatExpress fe = new TextFormatExpress();
		if(packageName.trim().length()!=0){
			return fe.appendFormat("package ?;",packageName); //sb.append(packageName.concat(";"));	//提常量
		}
		return fe.appendFormat("//default package");
	}
	
	//注释语句部分
	private StringBuffer notes(){
		TextFormatExpress fe = new TextFormatExpress();
		return fe.appendFormat("/**通过?自动创建的实体类**/",VERSION); //sb;
	}
	
	//封装属性部分，modify预留，默认private
	/*
	 * 某一张表的实体类
	 * private 部分
	 * Modify word选择：default private protected public,默认private
	 * xxx.modify();
	 */
	private StringBuffer declareAttributes(){
		StringBuffer sb = new StringBuffer();	
		TextFormatExpress fe = new TextFormatExpress();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			fe.appendFormat("\t? ? ?;\n",PRIVATE,string.getValue(),string.getKey());
			sb.append(fe);
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
		TextFormatExpress fe = new TextFormatExpress();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			String result = string.getKey();
			String methodName = GET.concat(captureName(result));
			fe.appendFormat("\t? ? ?(){\n\t\treturn ?;\n\t}\n",PUBLIC,string.getValue(),methodName,result);
			sb.append(fe);
		}
		return sb;
	}
	//setter方法
	private StringBuffer declareSetterMethods(){
		StringBuffer sb = new StringBuffer();
		TextFormatExpress fe = new TextFormatExpress();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			String result = string.getKey();
			String type = string.getValue()+" ";
			String methodName = SET.concat(captureName(result));
			fe.appendFormat("\t? void ?(? ?){\n\t\tthis.?=?;\n\t}\n",PUBLIC,methodName,type,result,result,result);
			sb.append(fe);	//默认执行toStirng
		}
		return sb;
	}
	
	//生成无参，全参构造器
	//给某个类生成无参构造器,可选
	private StringBuffer declareDefaultConstructor(String table){
		//先判断表是否存在
		
	
		StringBuffer sb = new StringBuffer();
		TextFormatExpress fe = new TextFormatExpress();
		fe.appendFormat("\t? ?(){\n\t\t?();\n\t}\n",PUBLIC,captureName(table),SUPER);
		sb.append(fe);
		return sb;
	}
	//全参数构造器
	private StringBuffer decalreFullAttributesConstructor(String table){
		//先判断表是否存在
		
		
		StringBuffer paramList = new StringBuffer();	//参数列表
		
		TextFormatExpress fe = new TextFormatExpress();
		for (Entry<String, String> string : fieldsMap.entrySet()) {
			fe.appendFormat("? ?,",string.getValue(),string.getKey());
			paramList.append(fe);	
		}
		//去掉最后的逗号和空格-->去掉最后两个字符
		int length = paramList.length();
		paramList.replace(0, length, paramList.substring(0, length-2));	//	减2是因为参数列表：“a,b,c, ”这里最后有个空格，因此要减2
		StringBuffer methodBody = new StringBuffer();
		fe.appendFormat("\t\t?();\n", SUPER);
		methodBody.append(fe);
		Set<String> set = getAttributes();
		StringBuffer attributes = new StringBuffer();
		for (String attribute : set) {
			fe.appendFormat("\t\t?.?=?;\n",THIS,attribute,attribute);
			attributes.append(fe);
		}
		methodBody.append(attributes);
		StringBuffer sb = new StringBuffer();
		fe.appendFormat("\t? ?(?){\n?\t}\n",PUBLIC,captureName(table),paramList.toString(),methodBody.toString());
		sb.append(fe);
		return sb;
	}
	
	//拼接类文件中的全部内容
	//是否生成构造器，生成哪种：0-不生成构造器，1-生成无参默认构造器，2-生成无参和全参两个构造器
	private StringBuffer entityContent(String table){
		StringBuffer sb = new StringBuffer();
		TextFormatExpress fe = new TextFormatExpress();
		//-------以下5行可封装成对象
		String textPackage = declarePackage(getPackageName()).toString();
		String textNotes = notes().toString();
		String textAttributes = declareAttributes().toString();
		String textGetterMethods = declareGetterMethods().toString();
		String textSetterMethods = declareSetterMethods().toString();
		
		fe.appendFormat("?\n\n?\n\n",textPackage,textNotes);
		sb.append(fe);
		fe.appendFormat("? ? ?",PUBLIC,CLASS,captureName(table));
		sb.append(fe);
		fe.appendFormat("? ?{\n\n?\n?\n?",IMPLEMENTS,SERIALIZABLE,textAttributes,textGetterMethods,textSetterMethods);
		sb.append(fe);

		switch (CONSTRUCTOR_STYLE) {
		case 0:
			break;
		case 1:
			fe.appendFormat("\n?", declareDefaultConstructor(table).toString());
			sb.append(fe);
			break;
		case 2:
			fe.appendFormat("\n?\n?", declareDefaultConstructor(table).toString(),decalreFullAttributesConstructor(table).toString());
			sb.append(fe);
			break;
		}
		sb.append("}");
		return sb;
	}
}

