package com.eastcom.szhe.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class AbstractInterceptor implements Interceptor {

	Object proxyed;

	public AbstractInterceptor(Object i) {
		proxyed = i;
	}
	
	public abstract void start();
	
	public abstract void end();

	@Override
	public int intercept(Object instance, Method method, Object[] Args) {
		try {
			start();
			method.invoke(this.proxyed, Args);
			end();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}
}