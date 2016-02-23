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
        "^((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) +\\d{1,2} \\d{2}:\\d{2}:\\d{2} ([-\\w]+ )?\\w+\\[\\d+\\]: )?"
        + "({clientIp}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):"
        + "({clientPort}\\d+) "
        + "\\[({timestamp}\\d{2}/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)/\\d{4}:\\d{2}:\\d{2}:\\d{2}.\\d{3})\\] "
        + "({frontend}\\w+) "
        + "({backend}\\w+)/"
        + "({host}\\w+) "
        + "({times}\\d+/\\d+/\\d+/\\d+/\\d+) "
        + "({statusCode}\\d{3}) "
        + "({byteRead}\\d+) "
        + "({capturedRequestCookie}\\S+) "
        + "({capturedResponseCookie}\\S+) "
        + "({terminationState}\\S+) "
        + "({connStates}\\S+) "
        + "({queuesStates}\\w/\\w) "
        + "(\\{({capturedRequestHeaders}.*)\\} )?"
        + "(\\{({capturedResponseHeaders}.*)\\} )?"
        + "\"({verb}\\S+) "
        + "({uri}\\S+) "
        + "({version}\\S+)\"$";
		p = new Pattern(rx);
		String log="Jul 24 13:49:05 localhost haproxy[29565]: 182.200.197.199:54209 [24/Jul/2015:13:49:05.755] front1 mc/mc_85 2/0/0/3/5 302 508 - - --VN 1120/1120/6/2/0 0/0 {mc.funshion.com|http://static.funshion.com/market/p2p/openplatform/master/2015-7-13/FunVodPlayer_1.0.1.5.swf} {} \"GET /interface/mc?mcid=3696&oc=152055&mac=&fck=142155377579a6a&mick=1424230417fd94a&uid=0&reqId=b1aa0230-31c7-11e5-b2dc-9b76214f4876&source=adp-c_wb&ad=22360 HTTP/1.1\"";
		Matcher m = p.matcher(log);
		if (m.matches()) {
     			 System.out.println("match!");
			 System.out.println(m.group("clientIp"));
			 System.out.println(m.group("clientPort"));
			 System.out.println(m.group("statusCode"));
    		}
		else{
			System.out.println("does not match");	
		}
		String ip = args[0];
		IPLocation ipdb =  new IPLocation();
		List<String> result = ipdb.search(ip);
		//int num = striptoint(ip);
		System.out.println(result);		
        }

}
