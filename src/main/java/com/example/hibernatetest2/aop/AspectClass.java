package com.example.hibernatetest2.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AspectClass {

    /**
     * Points to every function namded findById with 1 parameter in every class that ends with Service and that is placed in tables folder
     */
    @Pointcut("execution(public * com.example.hibernatetest2.tables.*.service.*Service.findById(*))") //? Sets for which functions/classes/variables/etc to look after
    public void anyFindByIdServiceMethod() {
    }

    

    /**
     * Every time something defined in anyFindIdServiceMethod() is called, calls this function before right this funciton will be called
     */
    @Before(value = "anyFindByIdServiceMethod() " + //? Pointcut, defined above
                     "&& args(id) " + //? Takes argument given to function
                     "&& target(service) " //? takes class in which function was run
                     //"&& this(serviceProxy)"
                     //"&& @within(transactional)"
            //argNames = "joinPoint,id,service,serviceProxy,transactional" //! For old versions of Java
    )
    public void addLogging (JoinPoint joinPoint, //? If you need this, it must always be first
                            Object id,
                            Object service
                            //Object serviceProxy
    ) {
        log.info ("before - invoked findById method in class {}, with id {}", service, id);
    }



    @AfterReturning(value = "anyFindByIdServiceMethod() && target(service)",
                    returning = "result")
    public void addLoggingAfterReturning(Object result, Object service) {
        log.info("after returning - invoked findById method in class {}, result {}", service, result);
    }



    @AfterThrowing(value = "anyFindByIdServiceMethod() && target(service)",
                   throwing = "ex")
    public void addLoggingAfterThrowing(Throwable ex, Object service) {
        log.info("after throwing - invoked findById method in class {}, exception {}: {}",
                 service,
                 ex.getClass(),
                 ex.getMessage()
        );
    }



    @After("anyFindByIdServiceMethod() && target(service)")
    public void addLoggingAfterFinally(Object service) {
        log.info("after (finally) - invoked findById method in class {}", service);
    }
}
