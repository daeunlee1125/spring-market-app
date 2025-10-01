package kr.co.shoply.config;

import kr.co.shoply.intercepter.AppInfoIntercepter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private AppInfo appInfo;

    @Bean
    public AppInfo getAppInfo() {
        appInfo = new AppInfo();
        return appInfo;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInfoIntercepter(appInfo));
    }

    @Bean
    public ModelMapper getModelMapper(){

        // DTO <-> Entity 변환 처리 컴포넌트 설정
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
