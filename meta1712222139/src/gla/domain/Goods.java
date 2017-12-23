package gla.domain;


	/**通过sat4j-1.3-test.jar自动创建的实体类**/ 

public class Goods{ 

	/**封装各属性**/

	private int id;
	private String name;
	private double marketprice;
	private double estoreprice;
	private String category;
	private int num;
	private String imgurl;
	private String description;

	/**提供getter,setter方法**/

	public int getId(){
 		return id;
	}
	public void setId(int id){
 		this.id=id;
	}
	public String getName(){
 		return name;
	}
	public void setName(String name){
 		this.name=name;
	}
	public double getMarketprice(){
 		return marketprice;
	}
	public void setMarketprice(double marketprice){
 		this.marketprice=marketprice;
	}
	public double getEstoreprice(){
 		return estoreprice;
	}
	public void setEstoreprice(double estoreprice){
 		this.estoreprice=estoreprice;
	}
	public String getCategory(){
 		return category;
	}
	public void setCategory(String category){
 		this.category=category;
	}
	public int getNum(){
 		return num;
	}
	public void setNum(int num){
 		this.num=num;
	}
	public String getImgurl(){
 		return imgurl;
	}
	public void setImgurl(String imgurl){
 		this.imgurl=imgurl;
	}
	public String getDescription(){
 		return description;
	}
	public void setDescription(String description){
 		this.description=description;
	}

	/*	无参数的构造方法	*/
	public Goods(){
		super();
	}

	/*	包含全部参数的构造方法	*/
	public Goods(int id,int num,String category,double estoreprice,String description,double marketprice,String name,String imgurl){
		super();
		this.id=id;
		this.num=num;
		this.category=category;
		this.estoreprice=estoreprice;
		this.description=description;
		this.marketprice=marketprice;
		this.name=name;
		this.imgurl=imgurl;
	}

 }