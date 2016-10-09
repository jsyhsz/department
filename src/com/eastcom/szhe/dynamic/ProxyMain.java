package com.eastcom.szhe.dynamic;

public class ProxyMain {
	
	/** 
	* @param args 
	*/  
	public static void main(String[] args) {  
		ActionImp c=new ActionImp();  
		Action i=(Action)Proxy.createProxy(ActionImp.class, new LoggerPoint(c));  
		i.action(); 
	}  

}
