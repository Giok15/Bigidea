package apps.generic.message;

public class MessageResult {
    private int code;
    private String message;
    private Object data;

    public MessageResult(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public MessageResult(int code, String message, Object data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public MessageResult(int code, Object data)
    {
        this.code = code;
        this.data = data;
    }

    public MessageResult(int code)
    {
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
