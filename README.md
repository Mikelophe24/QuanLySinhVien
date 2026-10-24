# Bài tập lớn: Ứng dụng Quản lý Sinh viên

Ứng dụng desktop viết bằng **Java Swing**, kết nối **MySQL** qua **JDBC**, tổ chức theo
kiến trúc **MVC** (Model - View - Controller).

---

## 1. Công nghệ sử dụng

| Hạng mục        | Công nghệ                          |
|-----------------|------------------------------------|
| Ngôn ngữ        | Java 17                            |
| Giao diện       | Java Swing (JFrame, JPanel, JTable, JComboBox...) |
| CSDL            | MySQL 8.0                          |
| Kết nối         | JDBC + MySQL Connector/J 8.4.0     |
| Kiến trúc       | MVC + DAO                          |
| Truy vấn        | `PreparedStatement` (chống SQL Injection) |

---

## 2. Cấu trúc thư mục

```
BTLjava/
├─ database/
│  └─ schema.sql            # Tạo CSDL qlsv + dữ liệu mẫu
├─ lib/
│  └─ mysql-connector-j-8.4.0.jar
├─ src/com/qlsv/
│  ├─ Main.java             # Điểm khởi động
│  ├─ model/                # Lớp dữ liệu (POJO): User, Lop, SinhVien, MonHoc, Diem
│  ├─ dao/                  # Truy xuất CSDL + DBConnection
│  ├─ view/                 # Giao diện Swing (các JFrame/JPanel)
│  ├─ controller/           # Xử lý sự kiện, nối View với DAO
│  └─ helper/               # ConfigManager đọc cấu hình
├─ config.properties        # Thông tin kết nối CSDL (sửa user/mật khẩu tại đây)
├─ compile.bat             # Biên dịch
└─ run.bat                 # Chạy ứng dụng
```

Đúng yêu cầu tổ chức package: `model`, `view`, `controller`, `dao`, `helper`.

---

## 3. Cơ sở dữ liệu (5 bảng có quan hệ khóa chính - khóa ngoại)

| Bảng       | Vai trò                     | Khóa ngoại                          |
|------------|-----------------------------|-------------------------------------|
| `users`    | Tài khoản đăng nhập         | —                                   |
| `lop`      | Lớp học (Thực thể 1)        | —                                   |
| `sinhvien` | Sinh viên (Thực thể 2)      | `ma_lop` → `lop`                    |
| `monhoc`   | Môn học                     | —                                   |
| `diem`     | Điểm                        | `ma_sv` → `sinhvien`, `ma_mh` → `monhoc` |

### Tạo CSDL

Mở terminal và chạy (nhập mật khẩu root khi được hỏi):

```bash
mysql -u root -p < database/schema.sql
```

File sẽ tự tạo CSDL `qlsv`, 5 bảng và nạp sẵn dữ liệu mẫu.

---

## 4. Cấu hình kết nối

Sửa file `config.properties` cho khớp máy của bạn:

```properties
db.url=jdbc:mysql://localhost:3306/qlsv?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true
db.user=root
db.password=root
```

---

## 5. Chạy ứng dụng

### Cách 1 — Dùng file bat (nhanh nhất)

```
Nhấp đúp run.bat
```

### Cách 2 — Dòng lệnh

```bash
# Biên dịch
javac -encoding UTF-8 -d build -cp "lib/mysql-connector-j-8.4.0.jar" src/com/qlsv/*.java src/com/qlsv/*/*.java

# Chạy (Windows dùng dấu ; ngăn cách classpath)
java -Dfile.encoding=UTF-8 -cp "build;lib/mysql-connector-j-8.4.0.jar" com.qlsv.Main
```

### Cách 3 — Mở bằng NetBeans / IntelliJ

1. Tạo project Java mới, trỏ **Source** tới thư mục `src`.
2. Thêm thư viện: `lib/mysql-connector-j-8.4.0.jar` vào classpath của project.
3. Đặt lớp chính (Main class) là `com.qlsv.Main` rồi Run.

### Tài khoản mẫu

| Tên đăng nhập | Mật khẩu  | Vai trò |
|---------------|-----------|---------|
| `admin`       | `admin123`| admin   |
| `user`        | `user123` | user    |

---

## 6. Các giao diện (5 giao diện)

1. **Đăng nhập** — kiểm tra tài khoản trong bảng `users`.
2. **Giao diện chính / Dashboard** — `JMenuBar` + `JToolBar` + nút lớn điều hướng.
3. **Quản lý Lớp** — CRUD đầy đủ trên bảng `lop` (Thực thể 1).
4. **Quản lý Sinh viên** — CRUD đầy đủ trên bảng `sinhvien` (Thực thể 2), liên kết
   khóa ngoại tới Lớp bằng `JComboBox`.
5. **Thống kê / Tìm kiếm nâng cao** — truy vấn có `JOIN`, `GROUP BY`, `ORDER BY`:
   - Số lượng sinh viên theo lớp.
   - Điểm trung bình theo từng sinh viên.
   - Điểm trung bình theo môn học.
   - Tìm kiếm sinh viên kết hợp: tên + lớp + điểm trung bình tối thiểu.

Ngoài ra còn có **Quản lý Môn học** và **Quản lý Điểm** (Điểm liên kết hai khóa ngoại
tới Sinh viên và Môn học) để minh họa đầy đủ quan hệ giữa các bảng.

---

## 7. Điểm kỹ thuật nổi bật

- Toàn bộ dữ liệu lấy từ MySQL, không dùng dữ liệu tĩnh.
- Mọi truy vấn dùng `PreparedStatement`.
- Bắt lỗi đầy đủ: lỗi kết nối CSDL, lỗi nhập liệu (ngày sinh, email, điểm), ràng buộc trùng khóa.
- Tách biệt rõ Model / View / Controller / DAO.
- Chú thích tiếng Việt đầy đủ, đặt tên biến/hàm nhất quán.

> Gợi ý nâng cấp: mật khẩu hiện lưu dạng văn bản thường cho dễ demo; có thể băm bằng
> BCrypt/SHA-256 để an toàn hơn trong thực tế.
