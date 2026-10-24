-- =====================================================================
--  CƠ SỞ DỮ LIỆU: QUẢN LÝ SINH VIÊN (qlsv)
--  Hệ quản trị: MySQL 8.0
--  Gồm 5 bảng có quan hệ Primary Key - Foreign Key:
--     users      : tài khoản đăng nhập
--     lop        : lớp học        (Thực thể 1 - Danh mục)
--     sinhvien   : sinh viên      (Thực thể 2 - Chi tiết, FK -> lop)
--     monhoc     : môn học
--     diem       : điểm           (FK -> sinhvien, FK -> monhoc)
-- =====================================================================

DROP DATABASE IF EXISTS qlsv;
CREATE DATABASE qlsv CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlsv;

-- ---------------------------------------------------------------------
-- Bảng tài khoản đăng nhập
-- ---------------------------------------------------------------------
CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    ho_ten   VARCHAR(100) NOT NULL,
    vai_tro  VARCHAR(20)  NOT NULL DEFAULT 'user'   -- 'admin' hoặc 'user'
);

-- ---------------------------------------------------------------------
-- Bảng lớp học (Thực thể 1 - Danh mục)
-- ---------------------------------------------------------------------
CREATE TABLE lop (
    ma_lop    VARCHAR(10)  PRIMARY KEY,
    ten_lop   VARCHAR(100) NOT NULL,
    khoa      VARCHAR(100) NOT NULL,            -- Khoa quản lý
    nien_khoa VARCHAR(20)                        -- Ví dụ: 2022-2026
);

-- ---------------------------------------------------------------------
-- Bảng sinh viên (Thực thể 2 - Chi tiết), liên kết tới lớp qua khóa ngoại
-- ---------------------------------------------------------------------
CREATE TABLE sinhvien (
    ma_sv      VARCHAR(10)  PRIMARY KEY,
    ho_ten     VARCHAR(100) NOT NULL,
    gioi_tinh  VARCHAR(5),                       -- Nam / Nữ
    ngay_sinh  DATE,
    email      VARCHAR(100),
    dien_thoai VARCHAR(20),
    ma_lop     VARCHAR(10),
    CONSTRAINT fk_sv_lop FOREIGN KEY (ma_lop)
        REFERENCES lop(ma_lop)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- ---------------------------------------------------------------------
-- Bảng môn học
-- ---------------------------------------------------------------------
CREATE TABLE monhoc (
    ma_mh      VARCHAR(10)  PRIMARY KEY,
    ten_mh     VARCHAR(100) NOT NULL,
    so_tin_chi INT          NOT NULL DEFAULT 3
);

-- ---------------------------------------------------------------------
-- Bảng điểm: mỗi dòng là điểm của 1 sinh viên ở 1 môn trong 1 học kỳ
-- Có 2 khóa ngoại tới sinhvien và monhoc
-- ---------------------------------------------------------------------
CREATE TABLE diem (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    ma_sv   VARCHAR(10) NOT NULL,
    ma_mh   VARCHAR(10) NOT NULL,
    diem_so DOUBLE,
    hoc_ky  VARCHAR(20),
    CONSTRAINT fk_diem_sv FOREIGN KEY (ma_sv)
        REFERENCES sinhvien(ma_sv) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_diem_mh FOREIGN KEY (ma_mh)
        REFERENCES monhoc(ma_mh) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT uq_sv_mh_hk UNIQUE (ma_sv, ma_mh, hoc_ky)
);

-- =====================================================================
--  DỮ LIỆU MẪU
-- =====================================================================

-- Tài khoản (mật khẩu để dạng chữ thường cho dễ demo)
INSERT INTO users (username, password, ho_ten, vai_tro) VALUES
('admin', 'admin123', 'Quản trị viên', 'admin'),
('user',  'user123',  'Nhân viên giáo vụ', 'user');

-- Lớp học
INSERT INTO lop (ma_lop, ten_lop, khoa, nien_khoa) VALUES
('CNTT01', 'Công nghệ thông tin 01', 'Công nghệ thông tin', '2022-2026'),
('CNTT02', 'Công nghệ thông tin 02', 'Công nghệ thông tin', '2022-2026'),
('KT01',   'Kế toán 01',             'Kinh tế',             '2023-2027'),
('QTKD01', 'Quản trị kinh doanh 01', 'Kinh tế',             '2023-2027');

-- Sinh viên
INSERT INTO sinhvien (ma_sv, ho_ten, gioi_tinh, ngay_sinh, email, dien_thoai, ma_lop) VALUES
('SV001', 'Nguyễn Văn An',   'Nam', '2004-01-15', 'an.nv@sv.edu.vn',   '0901111111', 'CNTT01'),
('SV002', 'Trần Thị Bình',   'Nữ',  '2004-03-22', 'binh.tt@sv.edu.vn', '0902222222', 'CNTT01'),
('SV003', 'Lê Hoàng Cường',  'Nam', '2004-07-09', 'cuong.lh@sv.edu.vn','0903333333', 'CNTT02'),
('SV004', 'Phạm Thị Dung',   'Nữ',  '2003-11-30', 'dung.pt@sv.edu.vn', '0904444444', 'KT01'),
('SV005', 'Vũ Minh Đức',     'Nam', '2004-05-18', 'duc.vm@sv.edu.vn',  '0905555555', 'QTKD01'),
('SV006', 'Hoàng Thị Hoa',   'Nữ',  '2004-09-02', 'hoa.ht@sv.edu.vn',  '0906666666', 'CNTT02');

-- Môn học
INSERT INTO monhoc (ma_mh, ten_mh, so_tin_chi) VALUES
('MH01', 'Lập trình Java',        3),
('MH02', 'Cơ sở dữ liệu',         3),
('MH03', 'Cấu trúc dữ liệu & GT', 4),
('MH04', 'Nguyên lý kế toán',     3);

-- Điểm
INSERT INTO diem (ma_sv, ma_mh, diem_so, hoc_ky) VALUES
('SV001', 'MH01', 8.5, 'HK1-2024'),
('SV001', 'MH02', 7.0, 'HK1-2024'),
('SV001', 'MH03', 9.0, 'HK1-2024'),
('SV002', 'MH01', 6.5, 'HK1-2024'),
('SV002', 'MH02', 8.0, 'HK1-2024'),
('SV003', 'MH01', 5.5, 'HK1-2024'),
('SV003', 'MH03', 7.5, 'HK1-2024'),
('SV004', 'MH04', 9.5, 'HK1-2024'),
('SV005', 'MH04', 6.0, 'HK1-2024'),
('SV006', 'MH01', 7.0, 'HK1-2024');

-- Kiểm tra nhanh
SELECT 'Tạo CSDL qlsv thành công!' AS thong_bao;
