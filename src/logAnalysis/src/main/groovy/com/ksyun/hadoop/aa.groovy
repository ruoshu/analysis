package com.ksyun.hadoop

/**
 * Created by Administrator on 2016/9/30.
 */
DataBean bean1 = new DataBean()
bean1.respTime = 1.232

DataBean bean2 = new DataBean()
bean2.respTime = 1.454

DataBean bean3 = new DataBean()
bean3.respTime = 0.232

List<DataBean> list = [bean1,bean2,bean3]
def max = list.max { it.respTime }.respTime
def min = list.min { it.respTime }.respTime
println "max=$max,min=$min"
