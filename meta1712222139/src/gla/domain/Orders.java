package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class Orders{ 

	/**封装各属性**/

	private String id;
	private int uid;
	private double totalprice;
	private String address;
	private int status;
	private java.sql.Date createtime;

	/**提供getter,setter方法**/

	public String getId(){
 		return id;
	}
	public void setId(String id){
 		this.id=id;
	}
	public int getUid(){
 		return uid;
	}
	public void setUid(int uid){
 		this.uid=uid;
	}
	public double getTotalprice(){
 		return totalprice;
	}
	public void setTotalprice(double totalprice){
 		this.totalprice=totalprice;
	}
	public String getAddress(){
 		return address;
	}
	public void setAddress(String address){
 		this.address=address;
	}
	public int getStatus(){
 		return status;
	}
	public void setStatus(int status){
 		this.status=status;
	}
	public java.sql.Date getCreatetime(){
 		return createtime;
	}
	public void setCreatetime(java.sql.Date createtime){
 		this.createtime=createtime;
	}

	/*	无参数的构造方法	*/
	public Orders(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public Orders(java.sql.Date createtime,int uid,String id,int status,String address,double totalprice){
		super();
		this.createtime=createtime;
		this.uid=uid;
		this.id=id;
		this.status=status;
		this.address=address;
		this.totalprice=totalprice;
	}

 }