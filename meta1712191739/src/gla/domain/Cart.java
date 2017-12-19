package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class Cart{ 

	/**封装各属性**/

	private int uid;
	private int gid;
	private int buynum;

	/**提供getter,setter方法**/

	public int getUid(){
 		return uid;
	}
	public void setUid(int uid){
 		this.uid=uid;
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
	public Cart(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public Cart(int uid,int buynum,int gid){
		super();
		this.uid=uid;
		this.buynum=buynum;
		this.gid=gid;
	}

 }