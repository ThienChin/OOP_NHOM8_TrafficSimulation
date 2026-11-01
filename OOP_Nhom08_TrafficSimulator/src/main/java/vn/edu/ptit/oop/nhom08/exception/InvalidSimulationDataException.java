package vn.edu.ptit.oop.nhom08.exception;

/**
 * Exception tự định nghĩa (checked): dữ liệu đầu vào của mô phỏng không hợp lệ
 * (mã xe, tốc độ, vị trí, thời gian đèn...). Giao diện bắt exception này và
 * hiển thị thông báo thân thiện thay vì để chương trình văng lỗi.
 */
public class InvalidSimulationDataException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String field;

    public InvalidSimulationDataException(String field, String message) {
        super(message);
        this.field = field;
    }

    /** Tên trường dữ liệu bị sai (id, speed, position, green...). */
    public String getField() {
        return field;
    }
}
