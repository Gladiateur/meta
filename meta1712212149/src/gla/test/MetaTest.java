package gla.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.sat4j.core.ConstsManager;
import com.sat4j.core.Core;
import com.sat4j.user.FrameRoot;

/**
 * 啊
 * @author Administrator
 *
 */
public class MetaTest {
	// 有关ResultSetMetaData
	@Test
	public void fun1() throws SQLException {
		final int COL = 2;
		ResultSet rs = Core.selectResultSet("user");
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount(); // 返回表的总列数
		String type = rsmd.getColumnClassName(COL); // 返回对应列的java类型
		int colMaxSize = rsmd.getColumnDisplaySize(COL);// 返回对应列的最大标准宽度，以字符为单位
		String colLabel = rsmd.getColumnLabel(COL); // 返回对应列的建议标题,即返回指定列的列名
		String colName = rsmd.getColumnName(COL); // 返回指定列的名称
		int colSqlTypeIndex = rsmd.getColumnType(COL); // 返回指定列的SQL类型
		String colSqlTypeName = rsmd.getColumnTypeName(COL);// 返回指定列的数据库特定类型名称
		int colSize = rsmd.getPrecision(COL); // 返回指定列的指定列宽
		int scale = rsmd.getScale(COL); // 返回指定列的小数点右边的位数
		String schemaName = rsmd.getSchemaName(COL); // 返回指定列的表模式
		String tableName = rsmd.getTableName(COL); // 返回指定列的表名称
		int nullAble = rsmd.isNullable(COL); // 返回指定列的值是否可以为NULL
		boolean readOnly = rsmd.isReadOnly(COL); // 返回指定列是否只读
		boolean writable = rsmd.isWritable(COL); // 返回指定列上进行写操作是否可以获得成功
		boolean autoIncrement = rsmd.isAutoIncrement(COL);// 返回指定列是否自增

		System.out.println("结果集中的列数：" + columnCount);
		System.out.println("第" + COL + "列的类型：" + type);
		System.out.println("第" + COL + "列的最大标准宽度：" + colMaxSize);
		System.out.println("第" + COL + "列的建议标题：" + colLabel);
		System.out.println("第" + COL + "列的名称：" + colName);
		System.out.println("第" + COL + "列的SQL类型：" + colSqlTypeIndex);
		System.out.println("第" + COL + "列的数据库特定类型名称：" + colSqlTypeName);
		System.out.println("第" + COL + "列的指定列宽：" + colSize);
		System.out.println("第" + COL + "列的小数点右边的位数：" + scale);
		System.out.println("第" + COL + "列的表模式：" + schemaName);
		System.out.println("第" + COL + "列的表名称：" + tableName);
		System.out.println("第" + COL + "列的值是否可以为NULL：" + nullAble);
		System.out.println("第" + COL + "列是否只读：" + readOnly);
		System.out.println("第" + COL + "列上进行写操作是否可以获得成功：" + writable);
		System.out.println("第" + COL + "列是否自增：" + autoIncrement);
	}

	// 有关DatabaseMetaData
	@Test
	public void fun2() throws ClassNotFoundException, SQLException {
		FrameRoot fr = new FrameRoot();
		Connection conn = fr.getConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		int dbMajorVersion = dbmd.getDatabaseMajorVersion(); // 获取底层数据库的主版本号
		int dbMinorVersion = dbmd.getDatabaseMinorVersion(); // 底层数据库的次版本号。
		String dbProductName = dbmd.getDatabaseProductName(); // 获取此数据库产品的名称
		String dbProductVersion = dbmd.getDatabaseProductVersion();// 获取此数据库产品的版本号
		int transactionLevel = dbmd.getDefaultTransactionIsolation();// 获取此数据库的默认事务隔离级别
		int driverMajorVersion = dbmd.getDriverMajorVersion(); // 获取此 JDBC
		// 驱动程序的主版本号
		int driverMinorVersion = dbmd.getDriverMinorVersion(); // 获取此 JDBC
		// 驱动程序的次版本号
		String driverName = dbmd.getDriverName(); // 获取此 JDBC 驱动程序的名称。
		String driverVersion = dbmd.getDriverVersion(); // 获取此 JDBC 驱动程序的 String
		// 形式的版本号
		int maxStatements = dbmd.getMaxStatements(); // 获取在此数据库中在同一时间内可处于开放状态的最大活动语句数
		String url = dbmd.getURL(); // 获取此 DBMS 的 URL
		String userName = dbmd.getUserName(); // 获取此数据库的已知的用户名称
		boolean readOnly = dbmd.isReadOnly(); // 获取此数据库是否处于只读模式
		boolean batchUpdates = dbmd.supportsBatchUpdates(); // 获取此数据库是否支持批量更新
		boolean differentTable = dbmd.supportsDifferentTableCorrelationNames();// 获取在表关联名称受支持时，是否要限制它们与表的名称不同
		boolean transactionIndicatedLevel = dbmd
				.supportsTransactionIsolationLevel(1); // 获取此数据库是否支持给定事务隔离级别
		boolean supportsTransactions = dbmd.supportsTransactions();// 获取此数据库是否支持事务
		boolean usesLocalFilePerTable = dbmd.usesLocalFilePerTable();// 获取此数据库是否为每个表使用一个文件
		boolean usesLocalFiles = dbmd.usesLocalFiles(); // 获取此数据库是否将表存储在本地文件中

		System.out.println("数据库版本：" + dbMajorVersion + "." + dbMinorVersion);
		System.out.println("此数据库产品的名称：" + dbProductName);
		System.out.println("此数据库产品的版本号:" + dbProductVersion);
		System.out.println("默认事务隔离级别:" + transactionLevel);
		System.out.println("JDBC 驱动程序的版本:" + driverMajorVersion + "."
				+ driverMinorVersion);
		System.out.println(" JDBC 驱动程序的名称:" + driverName);
		System.out.println(" JDBC 驱动程序的版本号:" + driverVersion);
		System.out.println("此数据库中在同一时间内可处于开放状态的最大活动语句数:" + maxStatements);
		System.out.println("此 DBMS 的 URL:" + url);
		System.out.println("当前用户：" + userName);
		System.out.println("数据库是否只读：" + readOnly);
		System.out.println("此数据库是否支持批量更新:" + batchUpdates);
		System.out.println("在表关联名称受支持时，是否要限制它们与表的名称不同:" + differentTable);
		System.out.println("此数据库是否支持给定事务隔离级别:" + transactionIndicatedLevel);
		System.out.println("此数据库是否支持事务:" + supportsTransactions);
		System.out.println("此数据库是否为每个表使用一个文件" + usesLocalFilePerTable);
		System.out.println("此数据库是否将表存储在本地文件中:" + usesLocalFiles);
	}

	// 遍历指定数据库的所有表
	@Test
	public void fun3() throws ClassNotFoundException, SQLException {
		FrameRoot fr = new FrameRoot();
		fr.getConnection();
		String[] tables = fr.getTablesName("estore");
		for (String string : tables) {
			System.out.println(string);
		}
	}
	
	//判断指定表是否存在
	@Test
	public void fun3a() throws SQLException, ClassNotFoundException{
		FrameRoot fr = new FrameRoot();
		fr.getConnection();
		Map<String,String> configMap =ConstsManager.getConnMap();
		String dbName = configMap.get("dbName");	//获取配置文件指定数据库的名称
		System.out.println(dbName);
		List<String> tableList = Core.getTables(dbName);	//该值从配置文件获取
		for (String string : tableList) {
			System.out.println(string);
		}
	}
	
	// 遍历指定表的指定属性
	@Test
	public void fun4() throws SQLException {
		Map<String,String> configMap =ConstsManager.getConnMap();
		String dbName = configMap.get("dbName");	//获取配置文件指定数据库的名称
		final String TABLE = "cart";	
		String tableName = dbName.concat(".").concat(TABLE);//更严谨的数据表名称：dbName.table
		ResultSet rs = Core.selectResultSet(tableName);
		ResultSetMetaData rsmd = rs.getMetaData();
		System.out.println("table : " + TABLE);
		int columnCount = rsmd.getColumnCount(); // 返回表的总列数
		for (int i = 1; i <= columnCount; i++) {
			System.out.println("colName: " + rsmd.getColumnName(i)
					+ "\tcolType: " + rsmd.getColumnClassName(i));
		}
	}

	// StringBuffer
	@Test
	public void fun5() {
		final String DB = "estore";
		StringBuffer sb = new StringBuffer(DB);
		StringBuffer sbAppend = sb.append("hello");
		String sbAppendStr = sbAppend.toString();
		System.out.println(sb + "\t" + sbAppendStr);
	}

	// 声明所在包语句
	// 注释语句
	// 声明属性语句
	// getter,setter方法语句
	@Test
	public void fun6() throws SQLException {
		/*
		 * 声明所在包语句部分
		 */
		Map<String, String> beanMap = ConstsManager.getBeanMap();
		String path = beanMap.get("path");
		String packageName = formatToPackage(path);
		String declarePackage = "package " + packageName+";";
		System.out.println(declarePackage);
		
		/*
		 * 注释语句部分
		 */
		Map<String,String> configMap=ConstsManager.getConfigMap();
		String str1 = "\t/**通过"+configMap.get("VERSION")+"自动创建的实体类**/";
		System.out.println(str1);
		
		/*
		 * 某一张表的实体类
		 * private 部分
		 * Modify word选择：default private protected public,默认private
		 * xxx.modify();
		 */
		final String TABLE = "cart";
		ResultSet rs = Core.selectResultSet(TABLE);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount(); // 返回表的总列数
		for (int i = 1; i <= columnCount; i++) {
			//用append连接字符串
			System.out.println("private " + rsmd.getColumnClassName(i)+" " + rsmd.getColumnName(i)+";");
		}
		
		/*
		 * getter,setter
		 * 每遍历一张表就建立字段缓存，否则之后生成getter,setter方法时又要遍历一遍。
		 */
		//字段缓存
		/*
		 * 将  private int xxx;
		 * 的变量看做是Map中的key ,类型int看做是Map中的value
		 * 将所有<xxx,int>看成是Map<String,String>集合
		 * 参见fun6a
		 */
		
		/*
		 * 先扫描字段，类型，建缓存
		 * 之后从缓存取字段类型拼接封装语句和提供的getter,setter方法语句
		 */
	}
	
	/*
	 * 将  private int xxx;
	 * 的变量看做是Map中的key ,类型int看做是Map中的value
	 * 将所有<xxx,int>看成是Map<String,String>集合
	 * 回调至fun6
	 */
	@Test
	public void fun6a() throws SQLException{
		final String TABLE = "cart";
		ResultSet rs = Core.selectResultSet(TABLE);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount(); // 返回表的总列数
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 1; i <= columnCount; i++) {
			map.put(rsmd.getColumnName(i), rsmd.getColumnClassName(i));
		}
		
		//////////////////
		for (Entry<String, String> string : map.entrySet()) {
			System.out.println(string.getKey()+" "+string.getValue());
		}
	}

	// 对指定的一张表生成实体类，对指定的多张表生成实体类，对全部表生成实体类
	// 生成无参构造函数，生成全参构造函数
	@Test
	public void funn() {

	}

	// ---------------------------------------------------
	/**
	 * 路径格式转报名格式
	 * windows文件系统的路径格式为:com\a\b\c,在java中的路径格式为:com.a.b.c;所以需要将配置文件javabean
	 * .properties 中的文件系统的路径格式转换为java中的路径格式。
	 * */
	private final static String formatToPackage(String path) {
		String packName;
		path = path.substring(5); // /path的完全格式是\src\xxx\xxx...
		packName = path.replace('\\', '.');
		System.out.println("路径格式转包名格式：" + path + "--->" + packName);
		return packName;
	}
}
