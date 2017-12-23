package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class User{ 

	/**封装各属性**/

	private int id;
	private String email;
	private String nickname;
	private String password;
	private String role;
	private int state;
	private String code;

	/**提供getter,setter方法**/

	public int getId(){
 		return id;
	}
	public void setId(int id){
 		this.id=id;
	}
	public String getEmail(){
 		return email;
	}
	public void setEmail(String email){
 		this.email=email;
	}
	public String getNickname(){
 		return nickname;
	}
	public void setNickname(String nickname){
 		this.nickname=nickname;
	}
	public String getPassword(){
 		return password;
	}
	public void setPassword(String password){
 		this.password=password;
	}
	public String getRole(){
 		return role;
	}
	public void setRole(String role){
 		this.role=role;
	}
	public int getState(){
 		return state;
	}
	public void setState(int state){
 		this.state=state;
	}
	public String getCode(){
 		return code;
	}
	public void setCode(String code){
 		this.code=code;
	}

	/*	无参数的构造方法	*/
	public User(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public User(int id,String nickname,String email,int state,String role,String code,String password){
		super();
		this.id=id;
		this.nickname=nickname;
		this.email=email;
		this.state=state;
		this.role=role;
		this.code=code;
		this.password=password;
	}

 }