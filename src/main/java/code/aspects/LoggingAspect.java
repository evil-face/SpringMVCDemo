package code.aspects;

import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
  //         type     return    pkg/class    method    args
  //      execution(    *       code.dao.*     .*      (..)   )
  @Around("execution(* code.dao.*.*(..))")
  public Object logExecutionTimeInDAO(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long end = System.currentTimeMillis();
    System.out.println(
        LocalDateTime.now().withNano(0)
            + " [ASPECT] DB call by '"
            + joinPoint.getSignature()
            + "' was executed in "
            + (end - start)
            + " ms");

    return result;
  }

  // not SpEL!
  // and more!

  //  execution(public * code.controllers.*.*(..))

  // execution(* code.dao.PersonDAO.find*(..))

  // execution(* *(..)) - DANGER
}
