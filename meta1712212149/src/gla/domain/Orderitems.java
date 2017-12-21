package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class Orderitems{ 

	/**封装各属性**/

	private String oid;
	private int gid;
	private int buynum;

	/**提供getter,setter方法**/

	public String getOid(){
 		return oid;
	}
	public void setOid(String oid){
 		this.oid=oid;
	}
	public int getGid(){
 		return gid;
	}
	public void setGid(int gid){
 		this.gid=gid;
	}
	public int getBuynum(){
 		return buynum;
	}
	public void setBuynum(int buynum){
 		this.buynum=buynum;
	}

	/*	无参数的构造方法	*/
	public Orderitems(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public Orderitems(int buynum,String oid,int gid){
		super();
		this.buynum=buynum;
		this.oid=oid;
		this.gid=gid;
	}

 }