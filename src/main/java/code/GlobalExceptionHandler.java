package code;

import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(SQLException.class)
  public String handleSqlError(SQLException e, Model model, HttpServletRequest req) {
    model.addAttribute("errorMessage", "Database error: " + e.getMessage());
    model.addAttribute("timestamp", LocalDateTime.now());
    model.addAttribute("path", req.getRequestURI());

    return "error/dbError";
  }
}
