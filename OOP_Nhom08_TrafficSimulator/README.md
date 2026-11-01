# OOP_Nhom08_TrafficSimulator

Đề 8 – Mô phỏng Nút giao thông (Java Swing + JFreeChart).

## Yêu cầu
- JDK 17+
- Maven 3.8+

## Chạy
```
mvn clean package
java -jar target/OOP_Nhom08.jar
```
Hoặc chạy nhanh: `mvn compile exec:java`

Chạy kiểm thử: `mvn test`

## Cấu trúc
```
src/main/java/vn/edu/ptit/oop/nhom08/
  Main.java
  common/BaseWindow.java
  exception/    (InvalidSimulationDataException)
  model/        (Vehicle, Car, Bus, Motorcycle, Ambulance, LightState, Direction, TrafficLight, VehicleFactory)
  controller/   (LightController - interface, TrafficLightController)
  simulation/   (Simulation)
  ui/           (MainWindow, IntersectionPanel, AddVehicleDialog, SettingsDialog, AboutDialog, ControlPanel, StatisticsPanel)
src/main/resources/resources/icons/   (app.png, start.png, pause.png, reset.png, add.png)
```
