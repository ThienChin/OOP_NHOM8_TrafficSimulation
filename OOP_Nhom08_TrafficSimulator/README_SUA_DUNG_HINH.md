# Bản sửa hiện tượng xe đứng hình

- `Simulation.canEnterIntersection`: xe mới đến vạch dừng không còn bị coi là đã chiếm ngã tư; xe đã đi qua điểm xung đột không tiếp tục chặn xe khác.
- `Simulation.isSafeNextPosition`: khi xe đã quá gần nhau, cho phép chuyển động đang làm tăng khoảng cách để thoát tình trạng kẹt; vẫn chặn bước tiến làm giảm khoảng cách trong vùng nguy hiểm.
- Tránh tính `pathsConflict` và `firstConflictProgress` lặp lại trong cùng lần kiểm tra.
- Giữ 16ms, 300px/s xe thường, 450px/s xe cứu thương; không đổi giao diện hoặc JUnit.

Biên dịch Java chính bằng javac thành công. Chưa kiểm chứng trực quan hoặc toàn bộ JUnit. Nếu còn tình huống kẹt, hãy cung cấp mã xe/hướng và ảnh để tái hiện chính xác.
