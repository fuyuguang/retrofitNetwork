package com.fyg.networklib.log;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import android.view.View;

import androidx.annotation.Keep;

import com.fyg.networklib.R;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * Created by fuyuguang on 2022/6/3 3:34 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()

 */

@Aspect
@Keep
@SuppressWarnings("all")
public class CostTimeAspectJ {
    private static final String TAG = "CostTimeAspectJ";
    private static volatile boolean enabled = false;


    @Pointcut("within(@com.jiuxian.networklib.log.DebugLog *)")
    public void withinAnnotatedClass() {}

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    @Pointcut("execution(@com.jiuxian.networklib.log.DebugLog * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    @Pointcut("execution(@com.jiuxian.networklib.log.DebugLog *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

    public static void setEnabled(boolean enable) {
        enabled = enable;
    }

    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
        exitMethod(joinPoint, result, lengthMillis);

        return result;
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        Log.e(asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
        if (!enabled) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        Log.e(asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }



    @Around("execution(@com.jiuxian.networklib.log.DebouncingOnClickListener * *(..)) && execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickIntercept(ProceedingJoinPoint joinPoint) throws Throwable {
        View target = (View) joinPoint.getArgs()[0];
        Long lastTime = 0l;
        Object tag = target.getTag(R.id.view_key);
        if (tag != null && tag instanceof Long){
            lastTime = (Long) tag;
        }
        if (System.currentTimeMillis() - lastTime >= 300) {
            target.setTag(R.id.view_key,System.currentTimeMillis());
            joinPoint.proceed();
        } else {

        }
    }


//    @Around("execution(* com.jiuxian..*(..))")
//    public Object weaveAllMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (enabled){
//            long startNanoTime = System.nanoTime();
//            Object returnObject = joinPoint.proceed();
//            long stopNanoTime = System.nanoTime();
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//                Method method = signature.getMethod();
//                long toSeconds = TimeUnit.SECONDS.convert(stopNanoTime - startNanoTime, TimeUnit.NANOSECONDS);
//                if (toSeconds >= 1 && Looper.myLooper() == Looper.getMainLooper()) {
//                    Log.e(TAG, String.format(Locale.CHINA, "Method: <%s> costTime=%s s", method.toGenericString(), String.valueOf(toSeconds)));
//                    Toast.makeText(Ktx.app,method.toGenericString(),Toast.LENGTH_LONG).show();
//                }
//            }
//            return returnObject;
//        }else{
//            return joinPoint.proceed();
//        }
//    }

}
