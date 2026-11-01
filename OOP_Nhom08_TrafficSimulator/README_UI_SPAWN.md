# Điều chỉnh giao diện và vị trí xuất phát

- Bảng thêm xe chọn **Vị trí xuất phát** bằng tiếng Việt: Phía Tây, Phía Đông, Phía Bắc, Phía Nam.
- Model Direction vẫn biểu thị hướng xe chạy: xuất phát Tây => EAST; Đông => WEST; Bắc => SOUTH; Nam => NORTH.
- Hướng đích giữ nguyên: nơi xe sẽ đến; không cho quay đầu 180 độ.
- Chỉnh vạch dừng vào sát mép ngã tư, không còn các vạch trắng liền chạy vào giữa nút giao.
- Đèn phía Tây và phía Đông nằm ngang, vẫn có giây đếm ngược; đèn Bắc/Nam giữ dọc.
- Giữ nguyên VehicleFactoryTest từ bản đồng bộ JUnit và toàn bộ logic 16ms.
