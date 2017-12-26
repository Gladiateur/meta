package gla.test;

public class ThreadDemo extends Thread {
	public static void main(String[] args){
		A a=new A();
		a.start();
		
		for(int i=0;i<10;i++){
			System.out.println("main "+i);
		}
		System.out.println("thread id: "+
			a.getId()+" name: "+a.getName());
	}
}
class A extends Thread{
	public void run(){
		for(int i=0;i<10;i++){
			System.out.println("fun "+i);
		}
	} 
}