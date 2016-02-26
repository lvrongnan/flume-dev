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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jregex.Matcher;
import jregex.Pattern;

public class test {
	private static Pattern p;
	public static int striptoint(String ip){
        String []iparry = ip.split("\\.");
        int sum = 0;
        for(int i = 0;i < iparry.length;i++){
		System.out.println((int)Math.pow(256,iparry.length-1-i));
		System.out.println(Integer.parseInt(iparry[i]));
                sum = sum + (int)Math.pow(256,iparry.length-1-i)*Integer.parseInt(iparry[i]);
        }
        return sum;
   }
        public static void main(String[] args) {
                System.out.println("Hello world!");
		
		String rx=
		       "^({timestamp}\\d{2}/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)/\\d{4}:\\d{2}:\\d{2}:\\d{2}) "
        		+"({timezone}\\S+) \\| "
        		+"({host}\\S+) \\| "
        		+"({statusCode}\\d{3}) \\| "
        		+"({requestLength}\\d+) \\| "
        		+"({byteSent}\\d+) \\| "
        		+"({backend}\\S+) \\| "
        		+"({responsetime}\\S+) \\| "
        		+"({refer}\\S+) \\| "
        		+"({remoteaddr}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) \\| "
        		+"({remoteuser}\\S+) \\| "
        		+"({verb}\\S+) "
        		+"({uri}\\S+) "
        		+"({version}\\S+) \\| "
        		+"({useragent}.+) \\| "
        		+"({xforwardfor}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})$";
		p = new Pattern(rx);
		String log="23/Feb/2016:16:00:01 +0800 | js.funtv.bestv.com.cn | 200 | 331 | 8609 | [::1]:9000 | 0.084 | - | 10.201.194.112 | - | GET /search/mretrieve/v1?mtype=1&area=0&cate=52&year=1900_2100&order=3&pg=1&pz=101&pv=1 HTTP/1.1 | Dalvik/1.6.0 (Linux; U; Android 4.4.4; LS48H310G Build/KTU84P) | 112.112.184.3";
		String testrx=
			"^({timestamp}\\d{2}/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)/\\d{4}:\\d{2}:\\d{2}:\\d{2}) "
			+"({timezone}\\S+) \\| "
			+"({host}\\S+) \\| "
			+"({statusCode}\\d{3}) \\| "
			+"({requestLength}\\d+) \\| "
			+"({byteSent}\\d+) \\| "
			+"({backend}\\S+) \\| "
			+"({responsetime}\\S+) \\| "
			+"({refer}\\S+) \\| "
			+"({remoteaddr}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) \\| "
			+"({remoteuser}\\S+) \\| "
			+"({verb}\\S+) "
			+"({uri}\\S+) "
			+"({version}\\S+) \\|"
			+"({useragent}.+) \\|";
		//p = new Pattern(testrx);
		String testlog="23/Feb/2016:16:00:01 +0800 | js.funtv.bestv.com.cn | 200 | 331 | 8609 | [::1]:9000 | 0.089 | - | 10.201.194.112 | - | GET /search/mretrieve/v1?mtype=1&area=0&cate=52&year=1900_2100&order=3&pg=1&pz=101&pv=1 HTTP/1.1 | Dalvik/1.6.0 (Linux; U; Android 4.4.4; LS48H310G Build/KTU84P) |";
		Matcher m = p.matcher(log);
		if (m.matches()) {
     			 System.out.println("match!");
			 System.out.println(m.group("timestamp"));
			 System.out.println(m.group("host"));
			 System.out.println(m.group("statusCode"));
			 System.out.println(m.group("useragent"));
    		}
		else{
			System.out.println("does not match");	
		}
		String ip = m.group("xforwardfor");
		IPLocation ipdb =  new IPLocation();
		List<String> result = ipdb.search(ip);
		//int num = striptoint(ip);
		System.out.println(result);		
        }

}
