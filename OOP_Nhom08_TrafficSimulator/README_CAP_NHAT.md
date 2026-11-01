# Bản thử nghiệm: tốc độ động và dự đoán xung đột

- Simulation.java: tốc độ thực (pixel/tick) được lưu riêng; tăng tốc và phanh có giới hạn; vị trí lẻ tích lũy giữa các tick.
- Phán đoán xung đột bằng vị trí hình học và ETA ước lượng tại điểm giao nhau, với ngưỡng an toàn.
- Xe đã vào ngã tư được ưu tiên đi hết; xe rẽ trái nhường xe ngược chiều đi thẳng.
- Kiểm tra khoảng cách thực tế khi nhập làn vẫn giữ nguyên.

**Giới hạn:** ETA là mô hình xấp xỉ, không phải chứng minh tránh va chạm tuyệt đối; cần thử tình huống đông xe, giao nhau và nhập làn trên Eclipse.

Giao diện giữ nguyên để kiểm chứng logic trước khi tinh chỉnh UI.
