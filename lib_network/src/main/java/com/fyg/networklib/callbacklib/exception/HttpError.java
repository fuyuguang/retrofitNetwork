package com.fyg.networklib.callbacklib.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class HttpError extends RuntimeException {
    private static final long serialVersionUID = -1314024482758433L;

    @NonNull
    public String msg;


    @Nullable
    public final transient Object body;

    public HttpError(String msg) {
        this(msg, null);
    }

    public HttpError(String msg, @Nullable Object body) {
        super(msg);
        if (body instanceof Throwable) {
            initCause((Throwable) body);
        }
        this.msg = msg != null ? msg : "null";
        this.body = body;
    }


    @Nullable
    public <T> T castBody() {
        try {
            return (T) body;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String toString() {
        return "HttpError {msg="
                + msg
                + ", body="
                + body
                + '}';
    }




    public static String getExceptionInfo(Exception e){
        if (e != null && e.getStackTrace() != null){
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace.length>0){
                StringBuilder sb = new StringBuilder();
                StackTraceElement traceElement = stackTrace[0];
                String fileName = traceElement.getFileName();
                String methodName = traceElement.getMethodName();
                int lineNumber = traceElement.getLineNumber();
                return sb.append(e.getMessage()+"\n").append(traceElement.getFileName()+"\n").append(traceElement.getMethodName()+"\n"+traceElement.getLineNumber()).toString();
            }
        }
        return null;
    }

}