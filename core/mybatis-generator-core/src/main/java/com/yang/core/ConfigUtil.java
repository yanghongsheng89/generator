package com.yang.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.yang.generator.GenTest;

import freemarker.template.TemplateException;

public class ConfigUtil {

	private  String pack="com.yang.test";
	
	
	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	private static Properties p=new Properties();
	static{
		p.setProperty("remarksReporting", "true");
		p.setProperty("user", "pahctest02");
		p.setProperty("password", "pahctest02");
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}		
	}
	public static Connection getConnection() throws SQLException{
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return  DriverManager.getConnection("jdbc:oracle:thin:@172.30.150.100:1521:orcl",p);
	}
	
	public static List<DBColumn> getListColumn(String schema,String tableName) throws SQLException{
		Connection  conn = PoolUtil.getConn();
		DatabaseMetaData metaData = conn.getMetaData();
		//---------------------------------column------------------------
		ResultSet columns = metaData.getColumns(conn.getCatalog(),schema,tableName,null);
		Map<String,Object> map=new HashMap<String, Object>();
		List<DBColumn> list=new ArrayList<DBColumn>();
		while(columns.next()){
			for (int i = 0; i < ENUM.COLUMN.values().length; i++) {
				Object object;
				try {
					object = columns.getObject(ENUM.COLUMN.values()[i]
							+ "");
//					System.err.println(GenTest.camel(ENUM.COLUMN.values()[i]+"")+"<>"+object);
					if(object!=null){
						map.put(StringUtil.firstlower(StringUtil.camel(ENUM.COLUMN.values()[i]+"")), object);						
					}
				} catch (Exception e) {
					continue;
				}
			}
			DBColumn c=new DBColumn();
			BeanUtils.map2Bean(c, map);
			map.clear();
			list.add(c);
			
		}
		close(columns,conn);
		return list;
	}
	public  static void close(ResultSet rs,Connection con) {
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if(con!=null){
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static List<DBTable> getListTable(String schema) throws SQLException{
		Connection conn = PoolUtil.getConn();
		DatabaseMetaData metaData = conn.getMetaData();
		List<DBTable> list=new ArrayList<DBTable>();
		ResultSet tables = metaData.getTables(conn.getCatalog(),schema, null, new String[]{"TABLE"});
		Map<String,Object> map=new HashMap<String, Object>();
		while(tables.next()){
			DBTable tab=new DBTable();
			for (int i = 0; i < ENUM.TABLE.values().length; i++) {
				Object object;
				try {
					object = tables.getObject(ENUM.TABLE.values()[i]
							+ "");
					map.put(StringUtil.firstlower(StringUtil.camel(ENUM.TABLE.values()[i]+"")), object);	
				} catch (Exception e) {
					continue;
				}
				
			}
			Object object = map.get(StringUtil.firstlower(StringUtil.camel(ENUM.TABLE.TABLE_NAME+"")));
			List<DBColumn> column = getListColumn(schema,object+"");
			map.put("list", column);
			BeanUtils.map2Bean(tab, map);
			list.add(tab);
		}
		close(tables,conn);
		return list;
	}
	/**{@link TypeConvert#getType(BigDecimal) }
	 * @param big
	 * @return
	 *@Deprecated
	 */
	@Deprecated
	public static String getType(BigDecimal big){
		String type="";
		if(big.intValue()==Types.BIT){
			type="int";
			return type;
		}else if(big.intValue()==Types.TINYINT){
			type="int";
			return type;
		}else if(big.intValue()==Types.SMALLINT){
			type="int";
			return type;
		}else if(big.intValue()==Types.INTEGER){
			type="int";
			return type;
		}else if(big.intValue()==Types.BIGINT){
			type="int";
			return type;
		}else if(big.intValue()==Types.FLOAT){
			type="float";
			return type;
		}else if(big.intValue()==Types.REAL){
			type="boolean";
			return type;
		}else if(big.intValue()==Types.DOUBLE){
			type="double";
			return type;
		}else if(big.intValue()==Types.NUMERIC){
			type="double";return type;
		}else if(big.intValue()==Types.DECIMAL){
			type=BigDecimal.class.getName();return type;
		}else if(big.intValue()==Types.CHAR){
			type="String";return type;
		}else if(big.intValue()==Types.VARCHAR){
			type="String";return type;
		}else if(big.intValue()==Types.LONGVARCHAR){
			type="String";return type;
		}else if(big.intValue()==Types.DATE){
			type=Date.class.getName();return type;
		}else if(big.intValue()==Types.TIME){
			type=Time.class.getName();return type;
		}else if(big.intValue()==Types.TIMESTAMP){
			type=Timestamp.class.getName();return type;
		}else if(big.intValue()==Types.BINARY){
			type="byte[]";return type;
		}else if(big.intValue()==Types.VARBINARY){
			type="byte[]";return type;
		}else if(big.intValue()==Types.LONGVARBINARY){
			type="byte[]";return type;
		}else if(big.intValue()==Types.NULL){
			type="void";return type;
		}else if(big.intValue()==Types.OTHER){
			type=Object.class.getName();return type;
		}else if(big.intValue()==Types.JAVA_OBJECT){
			type=Object.class.getName();return type;
		}else if(big.intValue()==Types.DISTINCT){
			type=Object.class.getName();return type;
		}else if(big.intValue()==Types.STRUCT){
			type=Struct.class.getName();return type;
		}else if(big.intValue()==Types.ARRAY){
			type=Array.class.getName();return type;
		}else if(big.intValue()==Types.BLOB){
			type=Blob.class.getName();return type;
		}else if(big.intValue()==Types.CLOB){
			type=Clob.class.getName();return type;
		}else if(big.intValue()==Types.REF){
			type=Ref.class.getName();return type;
		}else if(big.intValue()==Types.DATALINK){
			type=Object.class.getName();return type;
		}else if(big.intValue()==Types.NCHAR){
			type="String";return type;
		}else if(big.intValue()==Types.NVARCHAR){
			type="String";return type;
		}else if(big.intValue()==Types.LONGNVARCHAR){
			type="String";return type;
		}else if(big.intValue()==Types.NCLOB){
			type=NClob.class.getName();return type;
		}else if(big.intValue()==Types.SQLXML){
			type=SQLXML.class.getName();return type;
		}
		return type;
	}
	@Test
	public  void test() throws SQLException, IOException, TemplateException {
		List<DBTable> list = getListTable("PAHCTEST02");
		for (DBTable dbTable : list) {
//			GenTest.camel(dbTable.getTableName()); class
//			dbTable.getRemarks(); comment
//			System.err.println(dbTable.getTableName()+"\t");
//			System.err.println(dbTable.getRemarks()+"\t");
			List<DBColumn> lis = dbTable.getList();
			Constant bean=new Constant();
			bean.setClassName(StringUtil.camel(dbTable.getTableName()));
			bean.setPack(pack);
			bean.setComment(dbTable.getRemarks()!=null?dbTable.getRemarks():"");
			List<Property> listabs=new ArrayList<Property>();
			//TODO 常量待续
			for (DBColumn dbColumn : lis) {
				Property ab=new Property();
				ab.setClassName(TypeConvert.getType(dbColumn.getDataType()));
				ab.setProperty(StringUtil.firstlower(StringUtil.camel(dbColumn.getColumnName())));
				ab.setComment(dbColumn.getRemarks()!=null?dbColumn.getRemarks():"");
				//TODO 常量待续
//				GenTest.firstlower(GenTest.camel(dbColumn.getColumnName())); field
//				System.err.println(dbColumn.getColumnName());
//				dbColumn.getTypeName(); class
//				System.err.println(dbColumn.getTypeName());
//				System.err.println(dbColumn.getDataType());
//				dbColumn.getRemarks() comment
				//System.err.println(dbColumn.getRemarks());
				ab.setValue(dbColumn.getRemarks()!=null?dbColumn.getRemarks():"");
				listabs.add(ab);
			}
			bean.setList(listabs);
			JavaBeanTmp tmp=new JavaBeanTmp();
			System.err.println(tmp.javaBean(bean, 0));
//			new Thread(new GenCode(bean,true)).start();
//			new Thread(new GenCode(bean,false)).start();
//			GenTest.getByBean(bean);
		}
	}
	class GenCode implements Runnable{

		private Constant constant;
		private boolean boo;
		
		public GenCode(Constant constant,boolean boo) {
			super();
			this.constant = constant;
			this.boo=boo;
		}
		public void run() {
			try {
				if(boo){
					GenTest.getByBean(constant);
				}else{
					GenTest.genWithConstant(constant);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		
	} 
}
