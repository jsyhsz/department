package com.eastcom.szhe.jsoup.lianjia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LianjiaXiaoquInfoParser {
	
	//六师附小：http://sh.lianjia.com/xuequ/4600007382.html
	//二中心：http://sh.lianjia.com/xuequ/4600006934.html
	public static void main(String[] args)
	{
		Document doc;
		Map<String,String> schools = new HashMap<>();
		try
		{
			
			List<String> resources = new ArrayList<>();
			resources.add("http://sh.lianjia.com/xuequ/pudongxinqu/d1");
			resources.add("http://sh.lianjia.com/xuequ/pudongxinqu/d2");
			
			for (String r : resources)
			{
				double random = Math.random();
				doc = Jsoup.connect( r+"?random="+random).get();
				Elements sList = doc.getElementsByAttributeValue("name", "selectDetail");				
				
				for(Element s : sList)
				{					
					if(s.attr("title") != null && s.attr("title").length()>1)
						schools.put(s.attr("title"), s.attr("href"));
					
				}
			}
			
			System.out.println(schools.keySet().size());
			System.out.println(schools);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		
			for(String s_name : schools.keySet())
			{
				int count = 0;
				boolean succ = false;
				while(!succ){
					count++;
					try {
						double random = Math.random();
						int total = 0;
						String s_href = schools.get(s_name);
						Map<String,String> xiaoqus = new HashMap<>();
						doc = Jsoup.connect("http://sh.lianjia.com"+s_href+"?random="+random).get();
						Elements newsHeadlines = doc.getElementsByClass("propertyEllipsis");
						
						for (Element tag : newsHeadlines) {// 遍历所有artibody下的p标签
							
							String title = tag.attr("title");
							String href = tag.attr("href");				
//							System.out.println(title+","+href);
							xiaoqus.put(title, href);

						}
						
						for(String name : xiaoqus.keySet())
						{
							String href = xiaoqus.get(name);
							double r = Math.random();
							boolean success = false;
							int try_times = 0;
							while(!success)
							{
								try_times +=1;
								try
								{
									//System.out.println(name);
									Thread.sleep((13+try_times*2)*1000);
									doc = Jsoup.connect("http://sh.lianjia.com"+href+"?random="+r).get();
									
									Elements infos = doc.getElementsByClass("other");
									
									for (Element tag : infos) {// 遍历所有artibody下的p标签
										
										String info = tag.text();
										if(info.indexOf("户")>0)
										{
											System.out.println(s_name+","+name+","+info);	
											total += 	Integer.parseInt(info.replace("户", ""));
											break;
										}		

									}
									
									success = true;
									
								}catch(Exception e)
								{
//									e.printStackTrace();
									
									if (try_times>4) //如果失败，尝试4次
									{
										System.out.println("没有抓取到数据:"+name);
										System.out.println(s_name+","+name+",");
										success =  true;
									}
								}
							}
							
						}
						
						System.out.println(s_name+",总计,"+total);
						Thread.sleep(10*1000);
						succ = true;
						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(count > 4)
								succ = true;
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
							
			
		
	}
	
	

}
