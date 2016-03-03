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
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;

import jregex.Matcher;
import jregex.Pattern;
import org.json.JSONObject;

/**
 * Simple Interceptor class that sets the host name or IP on all events
 * that are intercepted.<p>
 * The host header is named <code>host</code> and its format is either the FQDN
 * or IP of the host on which this interceptor is run.
 *
 *
 * Properties:<p>
 *
 *   preserveExisting: Whether to preserve an existing value for 'host'
 *                     (default is false)<p>
 *
 *   useIP: Whether to use IP address or fully-qualified hostname for 'host'
 *          header value (default is true)<p>
 *
 *  hostHeader: Specify the key to be used in the event header map for the
 *          host name. (default is "host") <p>
 *
 * Sample config:<p>
 *
 * <code>
 *   agent.sources.r1.channels = c1<p>
 *   agent.sources.r1.type = SEQ<p>
 *   agent.sources.r1.interceptors = i1<p>
 *   agent.sources.r1.interceptors.i1.type = host<p>
 *   agent.sources.r1.interceptors.i1.preserveExisting = true<p>
 *   agent.sources.r1.interceptors.i1.useIP = false<p>
 *   agent.sources.r1.interceptors.i1.hostHeader = hostname<p>
 * </code>
 *
 */
public class nginxparse implements Interceptor {

  private static final Logger logger = LoggerFactory
          .getLogger(haproxyparse.class);

  
  public Pattern relog,reuri;
  public String rx,urix;
  public IPLocation ipdb;
  //private FileWriter fw;

  public String [] objlist = {"timestamp","host","statusCode","responsetime"};
  /**
   * Only {@link haproxyparse.Builder} can build me
   */
  public nginxparse() {
 //u can put field u want there
  //String [] objlist = {"timestamp","host","statusCode","responsetime"};
  }

  @Override
  public void initialize() {
        rx =
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
	relog = new Pattern(rx);
	urix = "({realuri}\\S+)\\?(\\S+)";
	reuri = new Pattern(urix);
	ipdb =  new IPLocation();
	//try{
        //	fw = new FileWriter("/tmp/hafilter");
    	//}catch(Exception e){
        //        e.printStackTrace();
    //}
  }

  /**
   * Modifies events in-place.
   */
  @Override
  public Event intercept(Event event) {
    Map<String, String> header = event.getHeaders();
    String s = new String(event.getBody());
    Matcher m = relog.matcher(s);
     
    if (!m.matches()) {
      //logger.info(s);
      //logger.info("log does not match");
      //try{
      //		fw.write(s+"\n");
      //		fw.flush();
      //	}catch(Exception e){
      //		e.printStackTrace();
      //	}
      return null; //this means drop
    }
    //String newbody = "";
    JSONObject halogjson = new JSONObject();
    for(String field: objlist){
		//header.put(field, m.group(field));
		//newbody = newbody + field + ":" + m.group(field) +",";
		halogjson.put(field,m.group(field));
	}
    //convert ip to country province city isp
    List<String> ipresult = ipdb.search(m.group("xforwardfor"));
    halogjson.put("country",ipresult.get(0));
    halogjson.put("province",ipresult.get(1));
    halogjson.put("city",ipresult.get(2));
    halogjson.put("isp",ipresult.get(3));
    //add null to body
    //logger.info("now encoding is:"+System.getProperty("file.encoding"));
    //logger.info("headers:"+header);
    //String newbody = "";
    //event.setBody(newbody.getBytes());
    //convert uri to read uri
    //Matcher n = reuri.matcher(m.group("uri"));
    //if(n.matches()){
    //	halogjson.put("uri",n.group("realuri"));
    //	}
    //else{
    //	halogjson.put("uri","nourl");
    //	}
    halogjson.put("uri",m.group("host")+m.group("uri").split("\\?")[0]);
    header.put("key", m.group("remoteaddr"));
    event.setHeaders(header);
    event.setBody(halogjson.toString().getBytes());
    return event;
  }

  /**
   * Delegates to {@link #intercept(Event)} in a loop.
   * @param events
   * @return
   */
  @Override
  public List<Event> intercept(List<Event> events) {
    List<Event> interceptedEvents = new ArrayList<Event>(events.size());
    for (Event event : events) {
      interceptedEvents.add(intercept(event));
    }
    return interceptedEvents;
  }

  @Override
  public void close() {
    // no-op
  }

  /**
   * Builder which builds new instances of the haproxyparse.
   */
  public static class Builder implements Interceptor.Builder {
    private boolean filterorno = true;

    @Override
    public Interceptor build() {
      return new nginxparse();
    }

    @Override
    public void configure(Context context) {
      filterorno = context.getBoolean("filter", true);
    }

  }


}
