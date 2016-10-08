package com.ksyun.hadoop

import groovy.util.logging.Log4j2
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.FileInputFormat
import org.apache.hadoop.mapred.FileOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.hadoop.mapred.TextOutputFormat
import org.apache.hadoop.mapreduce.Job

/**
 * Created by MiaoJia(miaojia@kingsoft.com) on 2016/9/28.
 */
@Log4j2
class Bootstrap {

    static main(args) {
        //log.info "begin"
        Configuration conf = new Configuration();
        JobConf jobConf = new JobConf(conf, DataCount.class);

        jobConf.setJobName("count")
        jobConf.setJarByClass(DataCount.class);

        jobConf.setMapperClass(DataCount.DCMapper.class)
        jobConf.setMapOutputKeyClass(Text.class);
        jobConf.setMapOutputValueClass(DataBean.class);
        FileInputFormat.setInputPaths(jobConf, new Path(args[0] as String));

        jobConf.setReducerClass(DataCount.DCReducer.class);
        jobConf.setOutputKeyClass(Text.class);
        jobConf.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(jobConf, new Path(args[1] as String));

        jobConf.setInputFormat(TextInputFormat.class);
        jobConf.setOutputFormat(TextOutputFormat.class);

        Job job = new Job(jobConf);
        job.waitForCompletion(true);



        //log.info "end"

    }
}
