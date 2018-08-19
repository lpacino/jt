package com.jt.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {
	
    @Autowired(required=false)
    private CloseableHttpClient httpClient;

    @Autowired(required=false)
    private RequestConfig requestConfig;
    
    /**
     * 1.代码通用
     * 2.满足用户任意的需求
     * 3.传递参数时方便
     */
    
    public String doGet(String url,Map<String,String> params,String charset){
    	
    	String result = null;
    	
    	//1.判断字符集是否为null,如果为null添加默认值
    	if(StringUtils.isEmpty(charset)){
    		
    		charset = "UTF-8";
    	}
    	
    	//2.封装参数 www.jt.com/add?name=tom&age=18....
    	try {
			
	    	if(params != null){
	    		URIBuilder builder = new URIBuilder(url);
	    		
	    		for (Map.Entry<String,String> entry: params.entrySet()) {
	    			
	    			builder.addParameter(entry.getKey(), entry.getValue());
	    			
				}
	    		//参数拼接成功 www.jt.com?name=tom&age=18
	    		url = builder.build().toString();
	    	}
	    	
	    	//3.定义请求对象
	    	HttpGet httpGet = new HttpGet(url);
	    	httpGet.setConfig(requestConfig);
	    	
	    	//4.发起请求
	    	CloseableHttpResponse httpResponse =
	    			httpClient.execute(httpGet);
	    	
	    	//5.判断返回是否正确
	    	if(httpResponse.getStatusLine().getStatusCode() == 200){
	    		
	    		result = EntityUtils.toString(httpResponse.getEntity(),charset);
	    	}
	    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    	
    }
    
    public String doGet(String url){
    	
    	return doGet(url, null, null);
    }
    
    public String doGet(String url,Map<String,String> params){
    	
    	return doGet(url, params, null);
    }
    
    
    //实现post提交方式
    public String doPost(String url,Map<String,String> params,String charset){
    	String result = null;
    	
    	//1.判断字符集是否为null
    	if(StringUtils.isEmpty(charset)){
    		
    		charset = "UTF-8";
    	}
    	
    	//2.POST参数提交
    	HttpPost httpPost = new HttpPost(url);
    	httpPost.setConfig(requestConfig);
    	
    	try {
    		if(params !=null){
	    		//3.封装实体对象
	    		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	    		
	    		for (Map.Entry<String,String> param : params.entrySet()) {
					
	    			parameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
	    		
	        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,charset);
	        	httpPost.setEntity(entity);
    		}
        	
    		//发送post请求
    		CloseableHttpResponse httpResponse = 
    				httpClient.execute(httpPost);
    		
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
    			
    			result = EntityUtils.toString(httpResponse.getEntity(),charset);
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public String doPost(String url){
    	return doPost(url, null, null);
    }
    
    public String doPost(String url,Map<String,String> params){
    	return doPost(url, params, null);
    }

}
