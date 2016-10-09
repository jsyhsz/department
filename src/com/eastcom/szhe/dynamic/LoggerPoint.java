package com.eastcom.szhe.dynamic;

public class LoggerPoint extends AbstractInterceptor{

	public LoggerPoint(Object i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("start of the method!");
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		System.out.println("end of the method!");
	}

}
