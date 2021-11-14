/***********************************************************
 * @Description : 登录拦截器，主要用于校验Token
 * @author      : 梁山广(Laing Shan Guang)
 * @date        : 2019-05-22 07:35
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package lsgwr.exam.interceptor;

import com.google.gson.Gson;
import lsgwr.exam.utils.JwtUtils;
import lsgwr.exam.vo.JsonData;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * https://stackoverflow.com/questions/43591582/application-properties-value-in-spring-boot-interceptor
 *
 * @author liangshanguang
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 有上面的@Component才能使得这个属性能从pplication.yml中取得拦截器的值
     */
    @Value("${interceptors.auth-ignore-uris}")
    private String authIgnoreUris;

    /**
     * 进入controller之前进行拦截
     *
     * @param request  请求体
     * @param response 响应体
     * @param handler  处理者
     * @return 是否继续往下走
     * @throws Exception 拦截中出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器啦！");
        String uri = request.getRequestURI();
        System.out.println(uri);
        System.out.println("无需拦截的接口路径：" + authIgnoreUris);
        String[] authIgnoreUriArr = authIgnoreUris.split(",");
        // 登录和注册接口不需要进行token拦截和校验
        for (String authIgnoreUri : authIgnoreUriArr) {
            if (authIgnoreUri.equals(uri)) {
                return true;
            }
        }
        // 注意要和前端适配Access-Token属性，前端会在登陆后的每个接口请求头加Access-Token属性
        String token = request.getHeader("Access-Token");
        if (token == null) {
            // token不在header中时，也可能在参数中(RequestParam)
            token = request.getParameter("token");
        }
        if (token != null) {
            // 请求中是携带参数的
            Claims claims = JwtUtils.checkJWT(token);
            if (claims == null) {
                // 返回null说明用户篡改了token，导致校验失败
                sendJsonMessage(response, JsonData.buildError("token无效，请重新登录"));
                return false;
            }
            // 用户的的主键id
            String id = (String) claims.get("id");
            // 用户名
            String username = (String) claims.get("username");
            // 把这两个参数放到请求中，从而可以在controller中获取到，不需要在controller中在用Jwt解密了,request.getAttribute("属性名")即可获取
            request.setAttribute("user_id", id);
            request.setAttribute("username", username);
            return true;
        }
        sendJsonMessage(response, JsonData.buildError("token为null,请先登录！"));
        return false;
    }

    /**
     * 响应数据给前端
     *
     * @param response 响应
     * @param obj      返回的消息体
     * @throws Exception 处理异常
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        Gson g = new Gson();
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(g.toJson(obj));
        writer.close();
        response.flushBuffer();
    }
}
