package com.ksyun.hadoop

import groovy.transform.ToString

/**
 * Created by Administrator on 2016/10/12.
 */


class ConsoleDataBean implements org.apache.hadoop.io.Writable{

    def action,remoteAddr,requestTime,responseStatus,responseTime,params,length;

    @Override
    void write(DataOutput dataOutput) throws IOException{
        dataOutput.writeUTF(this.action as String)
        dataOutput.writeUTF(this.remoteAddr as String)
        dataOutput.writeUTF(this.requestTime as String)
        dataOutput.writeDouble(this.responseTime as Double)
        dataOutput.writeInt(this.responseStatus as int)
        dataOutput.writeUTF(this.params as String)
        dataOutput.writeInt(this.length as int)
    }

    @Override
    void readFields(DataInput dataInput) throws IOException{
        this.action = dataInput.readUTF();
        this.remoteAddr = dataInput.readUTF();
        this.requestTime = dataInput.readUTF();
        this.responseTime = dataInput.readDouble();
        this.responseStatus = dataInput.readInt();
        this.params = dataInput.readUTF();
        this.length = dataInput.readInt();
    }


    @Override
    public String toString() {
        return "ConsoleDataBean{" +
                "action=" + action +
                ", remoteAddr=" + remoteAddr +
                ", requestTime=" + requestTime +
                ", responseStatus=" + responseStatus +
                ", responseTime=" + responseTime +
                ", params=" + params +
                ", length=" + length +
                '}';
    }
}
