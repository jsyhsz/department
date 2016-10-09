package com.eastcom.szhe.dynamic;

import javassist.CtMethod;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * ����Javassist��̬�����ֽ���ʵ�ּ򵥵Ķ�̬���� Dynamic Proxy based on Javassist
 * 
 * @author:godsong
 * @version:1.01 2012/3/16
 * */
public class Proxy {
	/**
	 * ��̬���ɵĴ�������ǰ׺ prefix name for Proxy
	 * */
	private static final String PROXY_CLASS_NAME = ".Gproxy$";
	/**
	 * ������������ ���ڱ�ʾһ��Ψһ�Ĵ����ࣨ����Ĵ�������ΪGproxy$n�� index for generate a unique proxy
	 * class
	 * */
	private static int proxyIndex = 1;
	/**
	 * ����������(���ü̳м��ٶ�̬������ֽ���) Proxy interceptor(desingn for inherit)
	 * */
	protected Interceptor interceptor;

	/**
	 * Prohibit instantiation ����˽�й��캯����ֹ����ʵ����
	 * */
	private Proxy() {
	}

	protected Proxy(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	/**
	 * ������̬����Ĺ������� static factory method for create proxy
	 * 
	 * @param targetClass
	 *            :�����������
	 * @param interceptor
	 *            ������ʵ��
	 * @return ���ض�̬����ʵ�� ��ʵ����targerClass�����нӿڡ� ��˿�������ת��Ϊ��Щ֮�е�����ӿ�
	 * */
	public static Object createProxy(Class<?> targetClass,
			Interceptor interceptor) {
		int index = 0;
		/* �������ʱ��������� */
		ClassPool pool = ClassPool.getDefault();
		/* ��̬���������� */
		CtClass proxy = pool.makeClass(targetClass.getPackage().getName()
				+ PROXY_CLASS_NAME + proxyIndex++);

		try {
			/* ���DProxy����Ϊ������ĸ��� */
			CtClass superclass = pool.get("com.eastcom.szhe.dynamic.Proxy");
			proxy.setSuperclass(superclass);
			/* ��ñ�����������нӿ� */
			CtClass[] interfaces = pool.get(targetClass.getName())
					.getInterfaces();
			for (CtClass i : interfaces) {
				/* ��̬����ʵ����Щ�ӿ� */
				proxy.addInterface(i);
				/* ��ýṹ�е����з��� */
				CtMethod[] methods = i.getDeclaredMethods();
				for (int n = 0; n < methods.length; n++) {
					CtMethod m = methods[n];
					/* ������ЩMethod���� �Ա㴫�ݸ���������interceptor���� */
					StringBuilder fields = new StringBuilder();
					fields.append("private static java.lang.reflect.Method method"
							+ index);
					fields.append("=Class.forName(\"");
					fields.append(i.getName());
					fields.append("\").getDeclaredMethods()[");
					fields.append(n);
					fields.append("];");
					System.out.println(fields.toString());
					/* ��̬����֮ */
					CtField cf = CtField.make(fields.toString(), proxy);
					proxy.addField(cf);
					GenerateMethods(pool, proxy, m, index);
					index++;
				}
			}
			/* �������췽���Ա�ע�������� */
			CtConstructor cc = new CtConstructor(
					new CtClass[] { pool.get("com.eastcom.szhe.dynamic.Interceptor") },
					proxy);
			cc.setBody("{super($1);}");
			proxy.addConstructor(cc);
			// proxy.writeFile();
			return proxy.toClass().getConstructor(Interceptor.class)
					.newInstance(interceptor);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��̬�������ɷ���ʵ�֣��ڲ����ã�
	 * */
	private static void GenerateMethods(ClassPool pool, CtClass proxy,
			CtMethod method, int index) {

		try {
			CtMethod cm = new CtMethod(method.getReturnType(),
					method.getName(), method.getParameterTypes(), proxy);
			/* ���췽���� */
			StringBuilder mbody = new StringBuilder();
			mbody.append("{super.interceptor.intercept(this,method");
			mbody.append(index);
			mbody.append(",$args);}");
			System.out.println(mbody.toString());
			cm.setBody(mbody.toString());
			proxy.addMethod(cm);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
}
