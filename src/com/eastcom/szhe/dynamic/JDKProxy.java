package com.eastcom.szhe.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy {

	public static void main(String[] args) {
		Object f =  Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { Action.class,OtherAction.class },
				new MyInvocationHandler());
		((OtherAction)f).doSomeThing();
	}
}

class MyInvocationHandler implements InvocationHandler {

	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2)
			throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("invoke method: " + arg0.getClass().getName() + ","
				+ arg1.getName());
		return null;
	}

}
