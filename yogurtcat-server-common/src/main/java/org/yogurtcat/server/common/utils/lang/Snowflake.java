package org.yogurtcat.server.common.utils.lang;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 * 
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 * 
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * 
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * 
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：hutool
 * 
 * @author s
 */
public class Snowflake {

	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	/**
	 * 最大支持机器节点数0~31，一共32个
	 */
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	/**
	 * 最大支持数据中心节点数0~31，一共32个
	 */
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	/**
	 * 序列号12位
	 */
	private final long sequenceBits = 12L;
	/**
	 * 机器节点左移12位
	 */
	private final long workerIdShift = sequenceBits;
	/**
	 * 数据中心节点左移17位
	 */
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	/**
	 * 时间毫秒数左移22位
	 */
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	/**
	 * 4095
	 */
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;
	
	private static Snowflake idWorker;
	 
    static {
        idWorker = new Snowflake(getWorkId(),getDataCenterId());
    }

	/**
	 * 构造
	 * 
	 * @param workerId 终端ID
	 * @param datacenterId 数据中心ID
	 */
	public Snowflake(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}


	/**
	 * 下一个ID
	 * 
	 * @return ID
	 */
	public synchronized long nextId() {
		long timestamp = genTime();
		if (timestamp < lastTimestamp) {
			// 如果服务器时间有问题(时钟后退) 报错。
			throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %dms", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}
	


	// ------------------------------------------------------------------------------------------------------------------------------------ Private method start
	/**
	 * 循环等待下一个时间
	 * 
	 * @param lastTimestamp 上次记录的时间
	 * @return 下一个时间
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = genTime();
		while (timestamp <= lastTimestamp) {
			timestamp = genTime();
		}
		return timestamp;
	}

	/**
	 * 生成时间戳
	 * 
	 * @return 时间戳
	 */
	private long genTime() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 用IP地址来计算WorkId
	 * @return
	 */
	private static Long getWorkId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
            return (long)(new Random().nextInt(32));
        }
    }
 
	/**
	 * 用HostName来计算DataCenterId
	 * @return
	 */
    private static Long getDataCenterId(){
    	try {
	        int[] ints = StringUtils.toCodePoints(Inet4Address.getLocalHost().getHostName());
	        int sums = 0;
	        for (int i: ints) {
	            sums += i;
	        }
	        return (long)(sums % 32);
    	} catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
    		return (long)(new Random().nextInt(32));
        }
    }
    
	// ------------------------------------------------------------------------------------------------------------------------------------ Private method end
	
 
    /**
     * 静态工具类
     *
     * @return
     */
    public static Long generateId(){
    	return idWorker.nextId();
    }
    
	/**
	 * 下一个ID（字符串形式）
	 *
	 * @return ID 字符串形式
	 */
	public static String nextIdStr() {
		return Long.toString(idWorker.nextId());
	}
}
