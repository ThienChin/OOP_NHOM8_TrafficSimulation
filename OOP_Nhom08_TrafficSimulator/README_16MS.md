# Bản thử nghiệm 16 ms và chuyển động mượt

- `Simulation.TICK_MS = 16`, khoảng 62.5 lần cập nhật/giây.
- Vận tốc nội bộ tính theo pixel/giây, giữ tốc độ tối đa gần tương đương bản 35ms trước.
- Gia tốc/phanh tính theo pixel/giây² và nhân với DT.
- Vị trí chính xác lưu dạng double trong Simulation và được dùng khi vẽ trên IntersectionPanel.
- `Vehicle.getPosition()` vẫn là int để tương thích API và JUnit hiện tại.
- Giữ các kiểm tra quỹ đạo và khoảng cách; đây là bản thử nghiệm, chưa kiểm thử trực quan nhiều xe.
