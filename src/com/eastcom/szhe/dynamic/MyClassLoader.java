package com.eastcom.szhe.dynamic;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;


public class MyClassLoader {
	
	public static void main(String[] args) {
//        if (args.length >= 1) {
		if( false )
		{
            try {
                
            	ClassPool pool = ClassPool.getDefault();
            	CtClass cc = pool.get("com.eastcom.szhe.dynamic.Tester");
            	
            	CtMethod m = cc.getDeclaredMethod("hello");
            	m.insertBefore("System.out.println(\"before ~~~~\");");
            	cc.writeFile();
            	
            	Class c = cc.toClass();
            	Tester tester = (Tester) c.newInstance();
            	tester.hello();
            	
            } catch(Exception e){
            	e.printStackTrace();
            	System.out.println(e.getMessage());
            }
		}
		
		 Tester  t = new Tester();
         
         t.hello();
            
    }    
    

}
