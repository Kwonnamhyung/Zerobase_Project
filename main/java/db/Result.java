package db;

public class Result {

    private static Result result = new Result();

    public static Result getResult() {
        return result;
    }

    public static void setResult(Result result) {
        Result.result = result;
    }

    private  String code;
    private  String message;

    public  String getCode() {
        return code;
    }

    public  void setCode(String code) {
        this.code = code;
    }

    public  String getMessage() {
        return message;
    }

    public  void setMessage(String message) {
        this.message = message;
    }

}
