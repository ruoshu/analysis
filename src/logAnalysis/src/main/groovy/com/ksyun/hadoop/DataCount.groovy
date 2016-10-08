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
 * Created by MiaoJia(miaojia@kingsoft.com) on 2016/9/28.
 */
@Log4j2
public class DataCount {
    @Log4j2
    public static class DCMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, DataBean> {

        @Override
        void map(LongWritable key, Text value, OutputCollector<Text, DataBean> output, Reporter reporter) throws IOException {
            //accept
            String line = value.toString();
            //split
            String[] fields = line.split("\\|");
            if (fields.length > 1) {
                //log.info "$fields[1], $fields[5], $fields[7], $fields[10]"
                DataBean bean = new DataBean()

                bean.time = fields[1].trim()
                bean.httpStatus = fields[5].trim()
                if (fields[7].trim() == "-") return
                if (fields[7].trim().contains(",")) return

                bean.respTime = Double.valueOf(fields[7].trim())

                String param
                bean.param = param = fields[10].trim()
                String action = "null"
                try {
                    String[] ps = param.substring(param.indexOf("?") + 1, param.indexOf(" HTTP/1.1")).split("&")

                    ps.each {
                        String[] p = it.split("=")
                        if (p[0] == "Action") {
                            action = p[1];
                            bean.action = action
                            output.collect(new Text(action), bean);
                        }
                    }
                } catch (e) {
                }
            }
        }

    }

    public static class DCReducer extends MapReduceBase implements Reducer<Text, DataBean, Text, Text> {


        @Override
        void reduce(Text key, Iterator<DataBean> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

            List<DataBean> list=new ArrayList<DataBean>();
            double dmin = Double.MAX_VALUE,dmax = Double.MIN_VALUE
            DataBean bean
            while (values.hasNext()){
                bean = values.next()
                list.add(bean)
                if(bean.respTime < dmin)
                    dmin = bean.respTime
                if(bean.respTime > dmax)
                    dmax = bean.respTime
            }




            def sum = list.size()

//            def max = list.max{it.respTime}.respTime

            double sumTime = 0
            list.each { sumTime += it.respTime }

            def avg = sumTime / (list.size() == 0 as double ? 1 : list.size())
                    output.collect(key, new Text(",$sum,$avg,$dmin,$dmax"))

        }

    }
}
