package com.morSun.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    /**
     *  添加跨域访问允许，使用注解@CrossOrigin也可以
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许的路径
        registry.addMapping("/**")
                //允许跨域请求的域名(所有)
                .allowedOriginPatterns("*")
                //允许cookie
                .allowCredentials(true)
                //允许的请求方式
                .allowedMethods("GET","POST","DELETE","PUT")
                //允许设置header属性
                .allowedHeaders("*")
                //跨域允许请求时间
                .maxAge(3600);
    }

    /**
     *  向框架中添加消息转换器的接口 ，
     *  自动会被扫描到
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
    }

    /**
     * 添加组件fastJson提供的 消息转换器FastJsonHttpMessageConverter
     *   给redis和直接返回json不通过controller使用
     */
    @Bean
    public HttpMessageConverter fastJsonHttpMessageConverter()
    {
        // 定义一个Converter转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
            // 设置时间的转换格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //  以字符串形式返回结果 （和前端需要的就是字符类型）
        SerializeConfig.globalInstance.put(Long.class, ToStringSerializer.instance);

        fastJsonConfig.setSerializeConfig(SerializeConfig.globalInstance);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }
}
