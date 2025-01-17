/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DAO.HoaDonDao;
import DTO.Model.HoaDon;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class HoaDonService {

    ArrayList<HoaDon> dshd = new ArrayList<>();
    private HoaDonDao hoaDonDao = new HoaDonDao();

    public HoaDonService() {
        dshd = hoaDonDao.readDB();
    }

    public ArrayList<HoaDon> getDshd() {
        return this.dshd;
    }

    public void readDB() {
        dshd = hoaDonDao.readDB();
    }

    public String getNextID() {
        return "HD" + String.valueOf(this.dshd.size() + 1);
    }

    public HoaDon getHoaDon(String mahd) {
        for (HoaDon hd : dshd) {
            if (hd.getMaHoaDon().equals(mahd)) {
                return hd;
            }
        }
        return null;
    }

    public Boolean add(HoaDon hd) {
        Boolean success = hoaDonDao.add(hd);
        System.out.print(success);
        if (success) {
            dshd.add(hd);
            return true;
        }
        return false;
    }

    public Boolean add(String maHoaDon, String maNhanVien, String maKhachHang, String maKhuyenMai, LocalDate ngayNhap, LocalTime gioNhap, float tongTien) {
        HoaDon hd = new HoaDon(maHoaDon, maNhanVien, maKhachHang, maKhuyenMai, ngayNhap, gioNhap, tongTien);
        return dshd.add(hd);
    }

    public Boolean update(String maHoaDon, String maNhanVien, String maKhachHang, String maKhuyenMai, LocalDate ngayNhap, LocalTime gioNhap, float tongTien) {
        HoaDon hd = new HoaDon(maHoaDon, maNhanVien, maKhachHang,maKhuyenMai, ngayNhap, gioNhap, tongTien);
        return update(hd);
    }

    public Boolean update(HoaDon hd) {
        Boolean success = hoaDonDao.update(hd);
        if (success) {
            for (HoaDon cthd : dshd) {
                if (cthd.getMaHoaDon().equals(hd.getMaHoaDon())) {
                    cthd = hd;
                }
            }
            return true;
        }
        return false;
    }

    public Boolean updateTongTien(String _mahd, float _tongTien) {
        Boolean success = hoaDonDao.updateTongTien(_mahd, _tongTien);
        if (success) {
            for (HoaDon cthd : dshd) {
                if (cthd.getMaHoaDon().equals(_mahd)) {
                    cthd.setTongTien(_tongTien);
                }
            }
            return true;
        }
        return false;
    }

    public Boolean delete(String maHoaDon) {
        Boolean success = hoaDonDao.delete(maHoaDon);
        if (success) {
            for (HoaDon hd : dshd) {
                if (hd.getMaHoaDon().equals(maHoaDon)) {
                    dshd.remove(hd);
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<HoaDon> search(String type, String keyword, LocalDate _ngay1, LocalDate _ngay2, double _tong1, double _tong2) {
        ArrayList<HoaDon> result = new ArrayList<>();

        dshd.forEach((hd) -> {
            switch (type) {
                case "Tất cả":
                    if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase())
                            || hd.getMaNhanVien().toLowerCase().contains(keyword.toLowerCase())
                            || hd.getMaKhachHang().toLowerCase().contains(keyword.toLowerCase())
                            || hd.getMaKhachHang().toLowerCase().contains(keyword.toLowerCase())
                            || hd.getNgayLap().toString().toLowerCase().contains(keyword.toLowerCase())
                            || hd.getGioLap().toString().toLowerCase().contains(keyword.toLowerCase())
                            || String.valueOf(hd.getTongTien()).toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }

                    break;

                case "Mã hóa đơn":
                    if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;

                case "Mã nhân viên":
                    if (hd.getMaNhanVien().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;

                case "Mã khách hàng":
                    if (hd.getMaKhachHang().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;

                case "Ngày lập":
                    if (hd.getNgayLap().toString().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Giờ lập":
                    if (hd.getGioLap().toString().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
                    break;
                case "Tổng tiền":
                    if (String.valueOf(hd.getTongTien()).toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(hd);
                    }
            }
        });

        //Ngay lap, tong tien
        for (int i = result.size() - 1; i >= 0; i--) {
            HoaDon hd = result.get(i);
            LocalDate ngaylap = hd.getNgayLap();
            float tongtien = hd.getTongTien();

            Boolean ngayKhongThoa = (_ngay1 != null && ngaylap.isBefore(_ngay1)) || (_ngay2 != null && ngaylap.isAfter(_ngay2));
            
            Boolean tienKhongThoa = (_tong1 != -1 && tongtien < _tong1) || (_tong2 != -1 && tongtien > _tong2);

            if (ngayKhongThoa || tienKhongThoa) {
                result.remove(hd);
            }
        }

        return result;
    }
}
