package com.eastcom.szhe.dynamic;

import java.lang.reflect.Method;

public interface Interceptor {
	
	public int intercept(Object instance, Method method, Object[] Args);

}
