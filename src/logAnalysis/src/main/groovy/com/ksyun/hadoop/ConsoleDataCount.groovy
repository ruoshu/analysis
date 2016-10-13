package com.ksyun.hadoop

import groovy.util.logging.Log4j2
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.MapReduceBase
import org.apache.hadoop.mapred.Mapper
import org.apache.hadoop.mapred.OutputCollector
import org.apache.hadoop.mapred.Reducer
import org.apache.hadoop.mapred.Reporter

/**
 * Created by Administrator on 2016/10/12.
 */
@Log4j2
public class ConsoleDataCount {

    @Log4j2
    public static class ConsoleDCMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, ConsoleDataBean>{
        @Override
        void map(LongWritable key, Text value, OutputCollector<Text, ConsoleDataBean> output, Reporter reporter) throws IOException {
            String line = value.toString();
            //去除非请求inner api的日志
            if(!line.contains("/api")){
                return
            }

            String[] contents = line.split(" ");
            if(contents.length > 1){
                ConsoleDataBean bean = new ConsoleDataBean()

                bean.remoteAddr = contents[0].trim()
                bean.requestTime = contents[3].substring(1).trim()
                bean.responseTime = contents[12].trim()
                bean.responseStatus = contents[10].trim()

                bean.length = contents[14].trim()

                String param
                bean.params = param = contents[8].trim()

                String action = "null";
                try{
                    String[] params = param.substring(param.indexOf("?") + 1).split("&")
                    params.each {
                        String[] p = it.split("=")
                        if(p[0] == "Action"){
                            action = bean.action = p[1]
                            output.collect(new Text(action), bean);
                        }
                    }
                }catch(e){

                }

            }
        }
    }


    public static class ConsoleDCReducer extends MapReduceBase implements Reducer<Text, ConsoleDataBean, Text, Text> {
        void reduce(Text key, Iterator<ConsoleDataBean> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
            List<ConsoleDataBean> list = new ArrayList<ConsoleDataBean>();
            double dmin = Double.MAX_VALUE,dmax = Double.MIN_VALUE
            int invalid = 0,error = 0,success = 0
            ConsoleDataBean bean
            while (values.hasNext()){
                bean = values.next()
                list.add(bean)
                if(bean.responseTime < dmin)
                    dmin = bean.responseTime
                if(bean.responseTime > dmax)
                    dmax = bean.responseTime
                if(bean.responseStatus >= 200 && bean.responseStatus < 300)
                    success ++
                if(bean.responseStatus >= 300 && bean.responseStatus < 500)
                    invalid ++
                if(bean.responseStatus >= 500)
                    error ++
            }

            def sum = list.size()

            double sumTime = 0
            list.each { sumTime += it.responseTime }

            def avg = sumTime / (list.size() == 0 as double ? 1 : list.size())
            output.collect(key, new Text(",$sum,$avg,$dmin,$dmax,$success,$invalid,$error"))

        }
    }
}
