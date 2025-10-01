package kr.co.shoply.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.shoply.config.AppInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class AppInfoIntercepter implements HandlerInterceptor {
    /*
        Interceptor
        - 클라이언트 요청과 컨트롤러 사이에서 특정 작업을 수행하는 컴포넌트
        - HTTP 요청을 가로채고, 요청이 컨트롤러에 도달하기 전 또는 후에 추가 작업 수행
     */

    private final AppInfo appInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 컨트롤러 수행 전 실행
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 컨트롤러 수행 후 실행
        // Controller 요청 메서드의 model 참조에 추가적인 appInfo 모델 참조 수행
        if (modelAndView != null) {
            modelAndView.addObject("appInfo", appInfo);
        }
    }
}
