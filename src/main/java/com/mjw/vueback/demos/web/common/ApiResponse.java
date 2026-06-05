package com.mjw.vueback.demos.web.common;

/**
 * 统一 API 响应封装
 * 所有 Controller 的返回值都用这个包装
 * 前端 axios 拦截器可以直接读取 code/message/data
 */
public class ApiResponse<T> {

    private int code;        // 状态码（200=成功）
    private String message;  // 提示信息
    private T data;          // 实际数据（泛型）

    // 成功（带数据）
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.code = 200;
        res.message = "success";
        res.data = data;
        return res;
    }

    // 成功（无数据）
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    // 失败
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.code = code;
        res.message = message;
        return res;
    }

    // getter/setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
