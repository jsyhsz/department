package com.eastcom.szhe.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * 根据地址，获取经纬度信息
 * 调用 百度 gis 开放api
 * @author James
 * 
 */
public class BaiduAPI {
	/**
	 * 百度接口说明：
	 * 百度地图Geocoding API服务地址：

		http://api.map.baidu.com/geocoder/v2/
		
		组成说明：
		
		域名：api.map.baidu.com
		
		服务名：geocoder
		
		服务版本号：较之前版本，v2版本新增参数。
		5.通用接口参数
		参数 	是否必须 	默认值 	格式举例 	含义
		output 	否 	xml 	json或xml 	输出格式为json或者xml
		ak 	是 	无 	E4805d16520de693a3fe707cdc962045 	用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key”
		sn 	否 	无 		若用户所用ak的校验方式为sn校验时该参数必须。 （sn生成算法）
		callback 	否 	无 	callback=showLocation(JavaScript函数名) 	将json格式的返回值通过callback函数返回以实现jsonp功能 
		
		地理编码专属请求参数：
		参数 	是否必须 	默认值 	格式举例 	含义 参数
		address 	是 	无 	北京市海淀区上地十街10号 	根据指定地址进行坐标的反定向解析
		
		该参数是地理编码的必填项，可以输入三种样式的值，分别是：
		•标准的地址信息，如北京市海淀区上地十街十号;
		•名胜古迹、标志性建筑物，如天安门，百度大厦;
		• 支持 “*路与*路交叉口”描述方式，如北一环路和阜阳路的交叉路口
		注意：后两种方式并不总是有返回结果，只有当地址库中存在该地址描述时才有返回。
		city 	否 	“北京市” 	“广州市” 	地址所在的城市名
		
		该参数是可选项，用于指定上述地址所在的城市，当多个城市都有上述地址时，该参数起到过滤作用。
		
		对于address字段可能会出现中文或其它一些特殊字符（如：空格），对于类似的字符要进行编码处理，编码成 UTF-8 字符的二字符十六进制值，凡是不在下表中的字符都要进行编码。
		字符集合 	字符
		URL非保留字 	a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z 0 1 2 3 4 5 6 7 8 9 - _ . ~
		URL保留字 	! * ' ( ) ; : @ & = + $ , / ? % # [ ]
		
		附注：
		
		(1) javascript中一般采用encodeURIComponent函数对特殊字符进行编码。
		
		(2) Java中可以使用函数URLEncoder.encode对特殊字符进行编码。
		
		(3) C#中可以使用函数HttpUtility.UrlEncode对特殊字符进行编码。
		
		(4) php中可以使用函数urlencode对特殊字符进行编码。
		
		地理编码示例
		
		以下是关于地理编码参数使用方法的示例。发送请求显示结果的JavaScript代码此处查看。
		请求示例：对北京市百度大厦进行地理编码查询；
		http://api.map.baidu.com/geocoder/v2/?ak=E4805d16520de693a3fe707cdc962045&callback=renderOption&output=json&address=百度大厦&city=北京市
		
		地理编码返回结果字段
		
		名称
			
		
		类型
			
		
		说明
		
		status
			
		
		Int
			
		
		返回结果状态值， 成功返回0，其他值请查看下方返回码状态表。
		
		location
			
		
		object
			
		
		经纬度坐标
			
		
		lat
			
		
		float
			
		
		纬度值
		
		lng
			
		
		float
			
		
		经度值
		
		precise
			
		
		Int
			
		
		位置的附加信息，是否精确查找。1为精确查找，0为不精确。
		
		confidence
			
		
		Int
			
		
		可信度
		
		level
			
		
		string
			
		
		地址类型
		
		json格式的返回值
		
		//带回调函数的返回格式  
		showLocation&&showLocation( 
		 {
		 status: 0,
		 result: 
		 {
		 location: 
		 {
		 lng: 116.30814954222,
		 lat: 40.056885091681
		 },
		 precise: 1,
		 confidence: 80,
		 level: "商务大厦"
		 }
		 }
		)
		
		//不带回调函数的返回值
		{
		 status: 0,
		 result: 
		 {
		 location: 
		 {
		 lng: 116.30814954222,
		 lat: 40.056885091681
		},
		precise: 1,
		confidence: 80,
		level: "商务大厦"
		}
		}
		
		
		xml格式的返回值
		
		<GeocoderSearchResponse>
			<status>0</status>
			<result>
				<location>
					<lat>40.056885091681</lat>
					<lng>116.30814954222</lng>
				</location>
				<precise>1</precise>
				<confidence>80</confidence>    
				<level>商务大厦</level>
			</result>
		</GeocoderSearchResponse>
		
		特别说明： 若解析status字段为OK，但结果内容为空，原因分析及可尝试方法：
		
		    地址库里无此数据，本次结果为空
		    加入city字段重新解析
		    将过于详细或简单的地址更改至省市区县街道重新解析
	 */
	
	
	private String url = "http://api.map.baidu.com/geocoder/v2/?ak=11e5VhTDPFP2mV07XbpMaQlo&output=json&address=ADDRESS&city=上海市";
	
	private String pk = "11e5VhTDPFP2mV07XbpMaQlo";
	
	public JSONObject transAddessToGis(String address)
	{		
		
		String uri = url.replace("ADDRESS", address).replaceAll("\\s","").replace("\\","");
		
		try {
			return get(uri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	private static JSONObject get(String url) throws IOException
	{		
		
//		System.out.println(url);
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		
		CloseableHttpResponse response = null;
		JSONObject jsonObject ;
		try {
			response = httpclient.execute(httpGet);
//		    System.out.println(response.getStatusLine());
		    jsonObject = getJSONObjectByGet(response);
		    HttpEntity entity = response.getEntity();
		    EntityUtils.consume(entity);
		} finally {
			if (response != null)
				response.close();
		}
		
		return jsonObject;
	}
	
	private static JSONObject getJSONObjectByGet(CloseableHttpResponse httpResponse){
        JSONObject resultJsonObject=null;
        
       
        //得到httpResponse的状态响应码
        int statusCode=httpResponse.getStatusLine().getStatusCode();
        if (statusCode==HttpStatus.SC_OK) {
            //得到httpResponse的实体数据
            HttpEntity httpEntity=httpResponse.getEntity();
            if (httpEntity!=null) {
                try {
                	BufferedReader bufferedReader=new BufferedReader
                    (new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
                    String line=null;
                    StringBuilder entityStringBuilder = new StringBuilder();
                    while ((line=bufferedReader.readLine())!=null) {
                        entityStringBuilder.append(line+"/n");
                    }
                    //利用从HttpEntity中得到的String生成JsonObject
                    
                    resultJsonObject=new JSONObject(entityStringBuilder.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultJsonObject;
    }
	
	
	public static void main(String[] args)
	{
		
		System.out.println(">>>>>>>>>>>>>>>>begin");
		final BaiduAPI trans = new BaiduAPI();
		
		
		File customers = new File("d:\\customers.csv");
		File resultFile = new File("d:\\custoemrs_gis.csv");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		int total = 0;
		int succ=0;
		
		Set<Future<String>> results = new HashSet<>();
		
		ExecutorService executor =
			    new ThreadPoolExecutor(
			        10, // core thread pool size
			        100, // maximum thread pool size
			        1, // time to wait before resizing pool
			        TimeUnit.MINUTES, 
			        new ArrayBlockingQueue<Runnable>(20000, true),
			        new ThreadPoolExecutor.CallerRunsPolicy());
			
		System.out.println("step 1");
		try {			
			
			reader = new BufferedReader( new InputStreamReader(new FileInputStream(customers), "GB2312"));			
			System.out.println("step 2");
			String line = null;			
			
			
			while((line = reader.readLine())!= null)
			{
//				System.out.println(line);
				final String[] columns = line.split(",");
				if(columns.length>2)
				{
					total++;
					
					 Future<String> future = executor.submit(new Callable<String>(){
						 public String call() {
							String address = columns[2] == null ? columns[1]:columns[2];
							
							if(address != null)
							{
								JSONObject result = trans.transAddessToGis(address);
								if(result != null )
								{
//									System.out.println(result);
									int status = result.getInt("status");
									if(status != 0) //通过地址获取不到经纬度，则用用户名称试试看
									{
										result = trans.transAddessToGis(columns[1]);
										if(result == null || result.getInt("status") == 1)
										{
											//System.out.println(line);
											return columns[0]+","+columns[1]+","+columns[2]+",,";
										}
									}
									
									JSONObject location = result.getJSONObject("result").getJSONObject("location");
									
									if(location == null)
										 return columns[0]+","+columns[1]+","+columns[2]+",,";									
									
									Double lng = location.getDouble("lng");
									Double lat = location.getDouble("lat");
									
									return columns[0]+","+columns[1]+","+columns[2]+","+lng+","+lat;									
								}								
								
							}
							return null;
						 }
						});
					 
					 results.add(future);
				
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				if ( reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}
		
		System.out.println(total);
		
		try {
		    System.out.println("attempt to shutdown executor");
		    executor.shutdown();
		    executor.awaitTermination(30, TimeUnit.MINUTES);
		    }
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow();
		    System.out.println("shutdown finished");
		}
		
		try
		{
			writer = new BufferedWriter(new FileWriter(resultFile));
			Future<String> future = null;
			Iterator<Future<String>> it = results.iterator();
			while(it.hasNext() ) 
			{
				future = it.next();
				String line = future.get();
				writer.write(line+"\n");
			}
			
			writer.flush();
			succ = results.size();
			
		}catch(Exception e)
		{
			
		}finally
		{
			try {
				if( writer != null)
				{
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("<<<<<<<<<<<<<<<<<<<< end ,total line num is "+total+",succ is "+succ);
		
		
	}
	


}
