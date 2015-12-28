import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * 
 */

/**
 * @author pzoli
 *
 */
public class Homework4Monitoring {

    /**
     * @param args
     *            http://docs.oracle.com/javase/8/docs/technotes/guides/
     *            management/mxbeans.html
     * 
     */
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        
        System.out.printf("Available processors: %d \n", rt.availableProcessors());
        System.out.printf("total memory: %.2f MeByte \n", rt.totalMemory() / 1024F / 1024F);
        float usedKiB = ((float) (rt.totalMemory() - rt.freeMemory())) / 1024F;
        float maxGiB = ((float) rt.maxMemory()) / 1024F / 1024F / 1024F;
        System.out.printf("max memory: %.2f GiByte\n", maxGiB);
        System.out.printf("memory usage: %.2f KiByte\n", usedKiB);
        System.out.println("fájlrendszer kódolása:" + System.getProperty("file.encoding"));
        
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        
        System.out.println(runtimeMxBean.getVmName() + " " + runtimeMxBean.getVmVendor() + " " + runtimeMxBean.getVmVersion());
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String item : arguments) {
            System.out.println(item);
        }

        Map<String, String> sysprops = runtimeMxBean.getSystemProperties();
        for (String item : sysprops.keySet()) {
            System.out.println(item + " : " + sysprops.get(item));
        }

        ThreadMXBean threadMB = ManagementFactory.getThreadMXBean();
        System.out.println("Thread count :" + threadMB.getThreadCount());
        MBeanServerConnection mbs = ManagementFactory.getPlatformMBeanServer(); 
        ObjectName oname;
        try {
            oname = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

            String vendor = (String) mbs.getAttribute(oname, "Name");

            // Check if this MXBean contains Oracle JDK's extension
            if (mbs.isInstanceOf(oname, "com.sun.management.OperatingSystemMXBean")) {
                // Get platform-specific attribute "ProcessCpuTime"
                long cpuTime = (Long) mbs.getAttribute(oname, "ProcessCpuTime");
                System.out.printf("Process CPU Time: %d",cpuTime);
            }
        } catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | MBeanException | ReflectionException | IOException e) {
            e.printStackTrace();
        }
        
        List<MemoryPoolMXBean> memMBeans = ManagementFactory.getMemoryPoolMXBeans();
        for(MemoryPoolMXBean memMB : memMBeans) {
            for(String name : memMB.getMemoryManagerNames()) {
            System.out.println("Memory manager names [" + name +"]");
            }
        }
        
        MemoryMXBean memMXB = ManagementFactory.getMemoryMXBean();
        System.out.println("Heap memory usage:" + memMXB.getHeapMemoryUsage());
    }

}
