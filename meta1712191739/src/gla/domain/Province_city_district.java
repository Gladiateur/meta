package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class Province_city_district{ 

	/**封装各属性**/

	private int id;
	private int pid;
	private String name;

	/**提供getter,setter方法**/

	public int getId(){
 		return id;
	}
	public void setId(int id){
 		this.id=id;
	}
	public int getPid(){
 		return pid;
	}
	public void setPid(int pid){
 		this.pid=pid;
	}
	public String getName(){
 		return name;
	}
	public void setName(String name){
 		this.name=name;
	}

	/*	无参数的构造方法	*/
	public Province_city_district(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public Province_city_district(int id,String name,int pid){
		super();
		this.id=id;
		this.name=name;
		this.pid=pid;
	}

 }