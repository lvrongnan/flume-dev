/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lvrn.flume.interceptor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.lang.Math;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class IPLocation {

  private static final Logger logger = LoggerFactory
          .getLogger(IPLocation.class);
  private String IP_FILE_CHINA="/opt/flume/funshion.city.dat";
  private String IP_FILE_OTHER="/opt/flume/funshion.country.dat";
  private List<IPEntry> list_china;
  private List<IPEntry> list_other;

  public class IPEntry{
	public long startip;
	public long endip;
	public String country;
	public String province;
	public String city;
	public String isp;
	public String reliability;
	
	public IPEntry(long startip,long endip,String country,String province,String city,String isp,String reliability){
		this.startip = startip;
		this.endip = endip;
		this.country = country;
		this.province = province;
		this.city = city;
		this.isp = isp;
		this.reliability = reliability;
	}

  }
  public IPLocation(){
	File file_china = new File(IP_FILE_CHINA);
	File file_other = new File(IP_FILE_OTHER);
	try{
	if(!file_china.exists()||!file_other.exists())
		throw new FileNotFoundException();
	BufferedReader buff_china = new BufferedReader(new FileReader(file_china));
	BufferedReader buff_other = new BufferedReader(new FileReader(file_other));
	this.list_china = loaddata(buff_china,"buff_china");
	this.list_other = loaddata(buff_other,"buff_other");
	}
	catch(FileNotFoundException e){
		logger.info("FileNotFoundException:"+e);	
	}

	}
  public List<IPEntry>loaddata(BufferedReader buff,String buffname){
	List<IPEntry> ret = new ArrayList<IPEntry>();
	String line = null;
	try{
	line = buff.readLine();	
	while(line!=null){
		String[] dataline = line.split(",");
		long sip = striptoint(dataline[0]);
		long eip = striptoint(dataline[1]);		
		if(buffname == "buff_china"){
			IPEntry entry = new IPEntry(sip,eip,"中国",dataline[2],dataline[3],dataline[4],dataline[5]);
			ret.add(entry);
		}
		else{
			IPEntry entry = new IPEntry(sip,eip,dataline[2],dataline[2],dataline[3],dataline[4],dataline[5]);
			ret.add(entry);	
		}
		line = buff.readLine();	
	}
	}
	catch(IOException e){
		logger.info("IOException:"+e);
	}
	return ret;
	}
  public List<String> search(String ip){
	List<String> ret = search(list_china,ip);
	if(ret.isEmpty())
		ret = search(list_other,ip);
	return ret;
   }
  public List<String> search(List<IPEntry> ipdb,String ip){
	int lid = 0;
	int hid = ipdb.size() - 1;
	long desip = striptoint(ip);
	List<String> ret = new ArrayList<String>();
	while(lid <= hid){
		int mid = (lid + hid)/2;
		if(desip >= ipdb.get(mid).startip && desip <= ipdb.get(mid).endip){
			ret.add(ipdb.get(mid).country);
			ret.add(ipdb.get(mid).province);
			ret.add(ipdb.get(mid).city);
			ret.add(ipdb.get(mid).isp);
			ret.add(ipdb.get(mid).reliability);
			return ret;
		}
		if(desip < ipdb.get(mid).startip)
			hid = mid - 1;
		if(desip > ipdb.get(mid).endip)
			lid = mid + 1;		
	}
	return ret;
		
   }
  public long striptoint(String ip){
	String []iparry = ip.split("\\.");
	long sum = 0;
	for(int i = 0;i < iparry.length;i++){

		sum = sum + (long)Math.pow(256,iparry.length-1-i)*Integer.parseInt(iparry[i]);
	}
	return sum;
   }

}
