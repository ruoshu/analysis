package com.ksyun.hadoop

import groovy.transform.ToString

/**
 * Created by MiaoJia(miaojia@kingsoft.com) on 2016/9/28.
 */
@ToString
class DataBean implements org.apache.hadoop.io.Writable {
    def action, time, httpStatus, param;
    double respTime

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.action as String)
        dataOutput.writeUTF(this.time as String)
        dataOutput.writeInt(this.httpStatus as Integer)
        dataOutput.writeDouble(this.respTime as Double)
        dataOutput.writeUTF(this.param as String)
    }

    @Override
    void readFields(DataInput dataInput) throws IOException {
        this.action = dataInput.readUTF()
        this.time = dataInput.readUTF()
        this.httpStatus = dataInput.readInt()
        this.respTime = dataInput.readDouble()
        this.param = dataInput.readUTF()

    }
}


