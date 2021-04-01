package com.mujin.utils;


import com.mujin.annotation.PropertyKey;
import com.mujin.constants.IntConstants;
import com.mujin.constants.PropertiesKey;
import com.mujin.constants.PropertyNameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverterSupport;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: TODO 暂时不支持bootStrap配置文件的读取
 *
 * @Author: 伍成林
 * @Date： 2021年 03月18日
 */
public final class PropertyLoadUtils {
    /**
     * 日志输出
     */
    private static final Logger log = LoggerFactory.getLogger(PropertyLoadUtils.class);

    /**
     * set方法的前缀
     */
    private static final String SET_PREFIX = "set";
    /**
     * get方法的前缀
     */
    private static final String GET_PREFIX = "get";
    /**
     * boolean值的is
     */
    private static final String IS_PREFIX = "is";
    /**
     * 类型转换支持
     */
    private static final TypeConverterSupport TYPE_CONVERT = new SimpleTypeConverter();
    /**
     * 匹配占位符的正则
     */
    private static final Pattern PATTERN;
    /**
     * 配置文件的map
     */
    private static volatile ConcurrentHashMap<String, Properties> PRO_MAP = new ConcurrentHashMap<>(IntConstants.INT_16);

    static {
        // 匹配 ${}的正则表达式
        String regex = "\\$\\{(.*?)}";
        // 将正则放到里面进行预热处理
        PATTERN = Pattern.compile(regex);
        // 读取所有的文件
        try {
            loadAllProperties();
        } catch (IOException e) {
            log.error("读取配置文件失败：", e);
        }
    }

    /**
     * 私有化工具类的构造器
     */
    private PropertyLoadUtils() {

    }

    /**
     * 配置简单的config类
     * TODO 暂时只考虑简单的对config配置类的赋值，不考虑IOC容器相互注入的问题
     *
     * @param tClass               需要配置的配置类的Class对象
     * @param propertiesNameSuffix 需要读取的文件名的前缀
     * @param <T>                  泛型
     * @return T 泛型的实例
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public static <T> T loadProperties(Class<T> tClass, String propertiesNameSuffix) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 创建是否赋过值的map
        Map<String, Boolean> theAssignmentMap = new HashMap<>(IntConstants.INT_16);
        // 创建当前Class文件的实例
        T instance = tClass.newInstance();
        // 读取配置文件
        Properties properties = loadProperties(propertiesNameSuffix);
        // 获取当前类中的所有属性
        Field[] fields = tClass.getDeclaredFields();
        // 获取当前类中的所有方法
        Method[] declaredMethods = tClass.getDeclaredMethods();
        // 设置属性的值
        setFieldValue(fields, tClass, instance, properties, theAssignmentMap);
        // 为方法赋值
        invokeMethods(declaredMethods, instance, properties, theAssignmentMap);

        return instance;
    }

    /**
     * 调用被@PropertyKey修饰的方法
     *
     * @param declaredMethods  Class类中所有的方法
     * @param instance         Class对象的实例
     * @param properties       读取出来的配置文件
     * @param theAssignmentMap 是否赋值的map
     * @param <T>              泛型
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void invokeMethods(Method[] declaredMethods, T instance, Properties properties, Map<String, Boolean> theAssignmentMap) throws InvocationTargetException, IllegalAccessException {
        for (Method method : declaredMethods) {
            boolean annotationPresent = method.isAnnotationPresent(PropertyKey.class);
            boolean isSetMethod = method.getName().startsWith(SET_PREFIX);
            // 判断是否有注解
            if (!annotationPresent) {
                continue;
            }
            // 判断是否有方法
            if (isSetMethod) {
                String filed = StrUtils.lowerCaseFirst(method.getName().replace(SET_PREFIX, ""));
                // 判断是否包含那个键，如果包含则说明已经有值了
                if (theAssignmentMap.containsKey(filed)) {
                    continue;
                }
            }
            // 注解中的值
            String value = method.getAnnotation(PropertyKey.class).value();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                method.invoke(instance);
                return;
            }
            method.invoke(instance, getObject(properties.getProperty(value), parameterTypes[0]));
        }

    }


    /**
     * 为属性赋值
     *
     * @param fields        Class类中所有的属性的数组
     * @param tClass        Class对象
     * @param instance      当前Class对象的实例
     * @param properties    配置文件读取出来的名字
     * @param assignmentMap 赋值与否的map
     * @param <T>           泛型
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void setFieldValue(Field[] fields, Class<T> tClass, T instance, Properties properties, Map<String, Boolean> assignmentMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field field : fields) {
            // 获取当前类中的属性的名字
            String fieldName = field.getName();
            // 判断是否有此注解
            boolean annotationPresent = field.isAnnotationPresent(PropertyKey.class);
            // 判断此前是否已经赋过值
            boolean isSetValue = assignmentMap.get(fieldName) != null && assignmentMap.get(fieldName);
            if (!annotationPresent || isSetValue) {
                continue;
            }
            // 获取注解的类
            PropertyKey annotation = field.getAnnotation(PropertyKey.class);
            // 获取注解中的value值
            String value = annotation.value();
            // 将参数的首字母大写，方便拼接方法
            String filedName = StrUtils.upperCaseFirst(field.getName());
            // 获取set方法
            Class<?> fieldType = field.getType();
            Method method = tClass.getMethod(SET_PREFIX + filedName, fieldType);

            method.invoke(instance, getObject(properties.getProperty(value), fieldType));
        }
    }

    /**
     * 类型为boolean的属性设值
     *
     * @param tClass     反射类的class对象
     * @param instance   class对象生成的
     * @param properties 获取到的properties文件对象
     * @param field      类型为boolean的值
     * @param value      所属自定义注解中的value
     * @param <T>        反射泛型
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static <T> void booleanSetValue(Class<T> tClass, T instance, Properties properties, Field field, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object invoke;
        // 将参数的首字母大写，方便拼接方法
        String filedName = StrUtils.upperCaseFirst(field.getName());
        Method method = null;
        // 获取
        try {
            method = tClass.getMethod(IS_PREFIX + filedName);
        } catch (NoSuchMethodException e) {
            method = tClass.getMethod(GET_PREFIX + filedName);
        } finally {
            Assert.notNull(method, "当前class类：" + tClass.getName() + "中没有这个属性的get或is方法：" + GET_PREFIX + filedName);
            invoke = method.invoke(instance);
        }
        if (invoke == null || !Boolean.parseBoolean(invoke.toString())) {
            Method setMethod = tClass.getMethod(SET_PREFIX + filedName);
            setMethod.invoke(instance, properties.get(value));
        }
    }


    /**
     * 读取配置文件成properties类
     *
     * @param propertiesNameSuffix 配置文件的名字
     * @return Properties 配置文件读取出来的类
     * @throws IOException 读取配置文件的流异常
     */
    private static Properties loadProperties(String propertiesNameSuffix) throws IOException {
        if (PRO_MAP.isEmpty()) {
            throw new IOException("没有读取到任何的properties文件");
        }

        // 读取application.properties文件或者application.yml文件
        Properties properties = PRO_MAP.get(PropertyNameConstants.APPLICATION_PREFIX + PropertyNameConstants.PROPERTIES_SUFFIX);
        if (properties == null) {
            properties = PRO_MAP.get(PropertyNameConstants.APPLICATION_PREFIX + PropertyNameConstants.YML_SUFFIX);
            if (properties == null) {
                throw new IOException(PropertyNameConstants.APPLICATION_PREFIX + PropertyNameConstants.PROPERTIES_SUFFIX + "或" + PropertyNameConstants.APPLICATION_PREFIX + PropertyNameConstants.YML_SUFFIX + "配置文件不存在");
            }
        }
        // 获取环境信息
        String active = readActive(properties);
        Properties pay = null;
        // 获取配置文件
        if (active == null || "null".equals(active)) {
            pay = PRO_MAP.get(propertiesNameSuffix + PropertyNameConstants.PROPERTIES_SUFFIX);
            if (pay == null || pay.isEmpty()) {
                throw new FileNotFoundException("没有找到：" + propertiesNameSuffix + PropertyNameConstants.PROPERTIES_SUFFIX);
            }
            setPropertiesVal(pay, new Properties[]{properties});
            return pay;
        }
        // 考虑多配置文件的时候根据环境变量进行选择
        String[] split = active.split(",");
        return manyEnvironmental(split, propertiesNameSuffix);
    }

    /**
     * 给配置文件设值
     *
     * @param pay        支付相关的配资文件
     * @param properties 系统核心配置文件 application.properties的一系列文件
     */
    private static void setPropertiesVal(Properties pay, Properties[] properties) {
        Set<Object> objects = pay.keySet();
        for (Object key : objects) {
            Object value = pay.get(key);
            Object str = regPlaceholder(value + "", properties);
            if (value.equals(str)) {
                continue;
            }
            pay.put(key, str);
        }
    }

    /**
     * 获取配置文件中的环境信息
     *
     * @param properties application配置文件中的信息
     * @return 环境信息
     */
    private static String readActive(Properties properties) {
        String active = properties.getProperty(PropertiesKey.SPRING_PROFILES_ACTIVE);
        if (StrUtils.isBlank(active)) {
            return null;
        }
        return active;
    }

    /**
     * 匹配值中的占位符并查找值
     *
     * @param value      值
     * @param properties 要从哪些配置文件中找这些值
     * @return String 占位符中的值
     */
    private static Object regPlaceholder(String value, Properties... properties) {

        // 匹配${xxx}中的内容
        Matcher matcher = PATTERN.matcher(value);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (properties.length == 1) {
                String property = properties[0].getProperty(key);
                if (StrUtils.isBlank(property)) {
                    throw new IllegalArgumentException("在配置文件application.properties中找不到：" + key + " 的配置");
                }
                value = value.replace("${" + key + "}", property);
            } else {
                // 多个配置文件的情况下依次读取
                for (int i = properties.length - 1; i >= 0; i--) {
                    String val = properties[i].getProperty(key);
                    if (StrUtils.isNotBlank(val)) {
                        value = value.replace("${" + key + "}", val);
                        break;
                    }
                    if (i == 0 && StrUtils.isBlank(val)) {
                        throw new IllegalArgumentException("在配置文件中找不到：" + key);
                    }
                }
            }

        }
        return value;
    }

    /**
     * 类型转换企且如果转换成了String并且值为"null"则默认赋值为""
     *
     * @param property      配置文件的属性
     * @param parameterType 类的成员属性的类型
     * @return Object 返回参数转换后的值
     */
    private static Object getObject(String property, Class<?> parameterType) {
        Object o = TYPE_CONVERT.convertIfNecessary(property, parameterType);
        if (o instanceof String) {
            if ("null".equals(o + "")) {
                o = "";
            }
        }
        return o;
    }

    /**
     * 读取根目录下的问价
     *
     * @throws IOException
     */
    private static void loadAllProperties() throws IOException {
        URL resource = ClassLoader.getSystemResource("");
        // 创建文件对象
        File rootFile = new File(resource.getPath());
        // 当前目录下的所有文件
        File[] fileArr = rootFile.listFiles();
        // 判断是否有文件
        if (fileArr == null || fileArr.length == 0) {
            return;
        }
        // 遍历文件
        for (File file : fileArr) {
            if (file.isDirectory()) {
                continue;
            }
            // 文件名
            String fileName = file.getName();
            // 需要读取的文件名必须包含的字符串
            boolean isNeedFile = fileName.contains(PropertyNameConstants.ALI_PAY_PREFIX) || fileName.contains(PropertyNameConstants.WX_PAY_PREFIX) || fileName.contains(PropertyNameConstants.APPLICATION_PREFIX);
            // 获取输入流
            InputStream resourceStream = new FileInputStream(file);
            // 读取文件成properties类并且保存到map中
            if (fileName.endsWith(PropertyNameConstants.PROPERTIES_SUFFIX) && isNeedFile) {
                Properties properties = new Properties();
                properties.load(resourceStream);
                PRO_MAP.put(fileName, properties);
            } else if (fileName.endsWith(PropertyNameConstants.YML_SUFFIX) && isNeedFile) {
                // 读取yml文件成Properties类
                Yaml yaml = new Yaml();
                Properties properties = yaml.loadAs(resourceStream, Properties.class);
                PRO_MAP.put(fileName, properties);
            }
            resourceStream.close();
        }
    }

    /**
     * 读取多环境变量中的相应的配置
     * spring.profiles.active = pro,dev 依照顺序读取 三个环境相同的配置则
     * application-pro 覆盖 application-dev 覆盖 application
     * <p>
     * 支付相关的配置同核心配置文件一样的顺序进行覆盖
     *
     * @param actives              配置的环境变量的数组
     * @param propertiesNameSuffix 支付配置的前缀
     * @return Properties 返回properties文件
     * @throws FileNotFoundException
     */
    private static Properties manyEnvironmental(String[] actives, String propertiesNameSuffix) throws FileNotFoundException {
        // 当前支付对象的的融合体
        Properties payPro = PRO_MAP.get(propertiesNameSuffix + PropertyNameConstants.PROPERTIES_SUFFIX);
        // Spring 系统配置文件的集合
        List<Properties> appProList = new ArrayList<>();
        // 添加原始的application.properties
        appProList.add(PRO_MAP.get(PropertyNameConstants.APPLICATION_PREFIX + PropertyNameConstants.PROPERTIES_SUFFIX));
        // 倒序遍历环境配置
        for (int i = actives.length - 1; i >= 0; i--) {
            // 获取与环境相馆的配置文件
            Properties envProperty = PRO_MAP.get(propertiesNameSuffix + "-" + actives[i] + PropertyNameConstants.PROPERTIES_SUFFIX);

            appProList.add(PRO_MAP.get(PropertyNameConstants.APPLICATION_PREFIX + "-" + actives[i] + PropertyNameConstants.PROPERTIES_SUFFIX));
            // 如果基本的支付配置文件为空则new一个实例
            if (payPro == null || payPro.isEmpty()) {
                payPro = new Properties();
            }
            // 判断是否有这个配置文件
            if (envProperty == null) {
                continue;
            }
            // 将环境配置文件倒序put到properties
            payPro.putAll(envProperty);
        }
        // 判空
        if (payPro.isEmpty()) {
            throw new FileNotFoundException("没有找到以：" + propertiesNameSuffix + "开头的配置文件");
        }
        // 读取配置文件
        setPropertiesVal(payPro, appProList.toArray(new Properties[0]));
        return payPro;
    }
}
