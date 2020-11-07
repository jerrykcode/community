package life.majiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, HttpServletResponse response, Throwable ex, Model model) {

        String contentType = request.getContentType();

        if (contentType.equals("application/json")) {
            ResultDTO resultDTO;
            if (ex instanceof  CustomizeException)
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            else
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERR);
            try (PrintWriter writer = response.getWriter()) {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {

            }
            return null;

        } else {
            if (ex instanceof CustomizeException)
                model.addAttribute("message", ex.getMessage());
            else
                model.addAttribute("message", "服务冒烟了，要不然你稍后再试试!!!");
        }
        return new ModelAndView("error");
    }

}
