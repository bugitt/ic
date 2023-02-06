package com.ryouonritsu.ic.common.annotation

import com.alibaba.fastjson2.toJSONString
import com.google.common.base.Stopwatch
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * @author ryouonritsu
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ServiceLog(
    val description: String = "",
    val printRequest: Boolean = true,
    val printResponse: Boolean = true
)

/**
 * @author ryouonritsu
 */
@Aspect
@Component
class ServiceLogAspect {
    companion object {
        private val log = LoggerFactory.getLogger(ServiceLogAspect::class.java)
    }

    @Pointcut("@annotation(com.ryouonritsu.ic.common.annotation.ServiceLog)")
    private fun serviceLog() {
    }

    @Around("serviceLog()")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val method = (joinPoint.signature as MethodSignature).method
        val serviceLog = method.getAnnotation(ServiceLog::class.java)
        val stopWatch = Stopwatch.createUnstarted()
        var result: Any? = null
        try {
            stopWatch.start()
            result = joinPoint.proceed()
        } finally {
            stopWatch.stop()
            log.info(
                "${joinPoint.target.javaClass.simpleName}.${method.name}() " +
                        "desc: ${serviceLog.description}, cost: ${stopWatch.elapsed(TimeUnit.MILLISECONDS)}ms" +
                        (if (serviceLog.printRequest) ", args: ${joinPoint.args.toJSONString()}" else "") +
                        (if (serviceLog.printResponse) ", result: ${result.toJSONString()}" else "")
            )
        }
        return result
    }
}