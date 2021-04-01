package com.mujin.utils;

import com.csvreader.CsvReader;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.mujin.dto.PayQrCodeDto;
import com.mujin.exception.PayException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Slf4j
public class PayUtils {

    public static final String DEFULT_CHARSET = "UTF-8";
    public static final int SEND_MQ_RETRY_TIMES = 5;

    /**
     * 使用GBK编码可以避免压缩中文文件名乱码
     */
    public static final String CHINESE_GBK = "GBK";

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * 支付成功向减rabbitmq发消息
     *
     * @return void
     * @Title sendMessage
     * @Description
     * @author 伍成林
     * @date: 2020年3月17日
     */
    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 在下载文件之前需要先创建一次文件夹
     *
     * @param dirPath 临时的文件夹
     * @return void
     * @throws PayException
     * @Title createDir
     * @Description
     * @author 伍成林
     * @date: 2020年5月12日
     */
    public static void createDir(String dirPath) throws PayException {
        File file = new File(dirPath);
        if (file.exists()) {
            return;
        }
        if (!file.mkdirs()) {
            throw new PayException("创建文件夹失败！");
        }
    }

    /**
     * 下载网络文件
     *
     * @param path     要下载的对账单的网络地址
     * @param filePath
     * @return void
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @Title downloadNet
     * @Description TODO
     * @author 伍成林
     * @date: 2020年3月20日
     */
    @SuppressWarnings("unused")
    public static byte[] downloadNet(String path, String filePath) throws IOException {
        log.info("文件路径是{}", filePath);
        File file = new File(filePath);
        if (file.exists()) {
            boolean isDelete = file.delete();
            if (!isDelete) {
                log.error("文件删除失败:  {}", filePath);
            }
        }

//		file.createNewFile();
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(path);
        URLConnection conn = url.openConnection();
        byte[] fileByte = null;
        try (
                ByteArrayOutputStream byteArrOut = new ByteArrayOutputStream();
                InputStream inStream = conn.getInputStream();
                FileOutputStream fs = new FileOutputStream(filePath)
        ) {
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                byteArrOut.write(buffer, 0, byteread);
                fs.write(buffer, 0, byteread);
            }
            fileByte = byteArrOut.toByteArray();
            fs.flush();
            byteArrOut.flush();

        } catch (Exception e) {
            log.error("IO流异常：{}", e);
        }
        return fileByte;
    }


    /**
     * 读取csv中的文件去表头
     *
     * @param file
     * @return List<String [ ]>
     * @throws IOException
     * @Title readCsv
     * @Description TODO
     * @author 伍成林
     * @date: 2020年3月21日
     */
    public static List<String[]> readCsv(File file) throws IOException {
        // 用来保存数据
        ArrayList<String[]> csvList = new ArrayList<String[]>();
        String csvFilePath = file.getAbsolutePath();
        // 一般用这编码读就可以了
        CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("GBK"));
        // 跳过表头 如果需要表头的话，不要写这句。
        reader.readHeaders();
        // 逐行读入除表头的数据
        while (reader.readRecord()) {
            csvList.add(reader.getValues());
        }
        reader.close();
        return csvList;
    }



    /**
     * 获取当前签名的SignType
     *
     * @param signType
     * @return SignType
     * @Title getSignType
     * @Description TODO
     * @author 伍成林
     * @date: 2020年3月15日
     */
    public static SignType getSignType(String signType) {
        if (WXPayConstants.MD5.equals(signType)) {
            return SignType.MD5;
        } else if (WXPayConstants.HMACSHA256.equals(signType)) {
            return SignType.HMACSHA256;
        } else {
            return SignType.MD5;
        }
    }

    /**
     * 获取10位时间戳
     *
     * @return String
     * @Title getTimestamp
     * @Description
     * @author 伍成林
     * @date: 2020年3月30日
     */
    public static String getTimestamp() {
        long timeMillis = System.currentTimeMillis();
        return timeMillis / 1000 + "";
    }


    /**
     * @param qrCode      二维码连接
     * @param orderNumber 订单号
     * @return com.fa.ebs.pay.dto.PayQrCodeDto
     * @Title createPayQrCode
     * @Description:
     * @author 伍成林
     * @date 2020/5/26
     */
    public static PayQrCodeDto createPayQrCode(String qrCode, String orderNumber) {
        return new PayQrCodeDto(qrCode, orderNumber);
    }

    /**
     * @param timeExpress 过期时间单位为毫秒
     * @return java.lang.String
     * @throws
     * @Title getTimeExpire
     * @Description:
     * @author 伍成林
     * @date 2020/5/26
     */
    public static String getTimeExpire(long timeExpress) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date();
        Long timeExpireLong = nowDate.getTime() + timeExpress;
        nowDate.setTime(timeExpireLong);
        return simpleDateFormat.format(nowDate);
    }

}
