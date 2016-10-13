import com.ksyun.hadoop.ConsoleDataBean
import org.apache.hadoop.io.Text

def s = '''114.255.44.132 - - [13/Sep/2016:04:28:30 +0800] fwf[-] network.console.ksyun.com GET /api?Action=GetEipMinitorDate&Service=eip&Version=2016-03-04&Region=cn-beijing-6&allocationId=4ddbfd73-dd5d-44ef-8a0c-f3c09615b58b&startTime=1473708499178&endTime=1473712099178&metric=qos.bps&downsample=30s-max-zero&_=1473711025486 HTTP/1.1 200  0.054 0.054 1115 1006 http://network.console.ksyun.com/index.html Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36 10.65.143.4 80 -'''
String[] contents = s.split(" ")
ConsoleDataBean bean = new ConsoleDataBean()

bean.remoteAddr = contents[0].trim()
bean.requestTime = contents[3].substring(1).trim()
bean.responseTime = contents[12].trim()
bean.responseStatus = contents[10].trim()

bean.length = contents[14].trim()

String param
bean.params = param = contents[8].trim()

String[] params = param.substring(param.indexOf("?") + 1).split("&")
params.each {
    String[] p = it.split("=")
    println p
    if(p[0] == "Action"){
        action = bean.action = p[1]
    }
}
println bean
