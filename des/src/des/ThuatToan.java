/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package des;

/**
 *
 * @author Dai
 */
public class ThuatToan {

// qua đoạn PBOX cái là L0 bị thay đổi, sai từ hàm dauvaoSBOX
    public ThuatToan() {
        what = true;
        dauvao = new int[64];
        dauvao4int = new int[]{1, 35, 69, 103};
        key16x6byte = new int[16][8];
        khoadauvao = new int[]{19, 52, 87, 121};
        L0 = new int[2];
        R0 = new int[2];
        saveR0 = new int[2];
        newR0 = new int[3];
        binary16 = new int[16];
        binary8 = new int[8];
        binary4 = new int[4];
        for (int j = 0; j < 16; j++) {
            binary16[j] = 0;
        }
        for (int j = 0; j < 8; j++) {
            binary8[j] = 0;
        }
        for (int j = 0; j < 4; j++) {
            binary4[j] = 0;
        }
        chuoi48bit = new int[48];
        dauvaoSBOX = new int[8][6];
        dauvaoPBOX = new int[32];
    }

    //lấy 56 bit từ 64 bit ra và hoán vị theo PC
    public int[] sinhkhoaPC1(int[] khoa64bit) {
        int[] khoa56bit = new int[56];
        for (int j = 0; j < 56; j++) {
            khoa56bit[j] = khoa64bit[PC1[j]];
        }
        return khoa56bit;
    }

    // dịch trái mảng int[56] ứng với 56 bit C+D;
    public int[] xoaytrai(int[] khoa56bit, int Circular_i) {

        int n = khoa56bit.length;
        int loop = 0;
        while (loop < LS[Circular_i]) {
            int C0 = khoa56bit[0];
            int D0 = khoa56bit[(n / 2)];
            for (int i = 0; i < (n / 2) - 1; i++) {
                khoa56bit[i] = khoa56bit[i + 1];
                khoa56bit[i + (n / 2)] = khoa56bit[i + (n / 2) + 1];
            }
            khoa56bit[(n / 2) - 1] = C0;
            khoa56bit[n - 1] = D0;
            loop++;
        }
        return khoa56bit;
    }

    // sinh khoa sau khi da co C0, D0
    public int[] sinhkhoaPC2(int[] khoa56bit) {
        int[] newkhoa;
        newkhoa = new int[48];
        for (int i = 0; i < 48; i++) {
            newkhoa[i] = khoa56bit[PC2[i]];
        }
        return newkhoa;
    }

    // tao 16 khoa phuc vu cho 16 vong lap
    public void tao16khoa(int[] asciikey) {
        int[] khoa64bit = new int[64];
        int[] khoa56bit = new int[56];
        int[] khoa48bit = new int[48];
        int[] khoa8bit = new int[8];

        // chuyen kieu 4 int ve 64 bit
        for (int i = 0; i < 4; i++) {
            nhiphan16(asciikey[i]);
            for (int j = 0; j < 16; j++) {
                khoa64bit[i * 16 + j] = binary16[j];
            }
        }

        // lựa chọn có hoán vị thành 56 bit
        khoa56bit = sinhkhoaPC1(khoa64bit);
        for (int i = 0; i < 16; i++) {

            // dịch trái 56 bit theo số bit dịch ở LS
            khoa56bit = xoaytrai(khoa56bit, LS[i]);
            // lựa chọn có hoán vị thành 48 bit
            khoa48bit = sinhkhoaPC2(khoa56bit);
            for (int j = 0; j < 48; j++) {
                khoa8bit[j % 8] = khoa48bit[j];
                if (j % 8 == 7) {
                    khoa8bit[j % 8] = khoa48bit[j];
                    // chuyển dữ liệu về kiểu thập phân phục vụ cho việc XOR sau này
                    key16x6byte[i][j / 8] = thapphan(khoa8bit); // đưa khóa về dạng thập phân
                }
            }
        }
    }

    // chuyen kieu 8 byte ve 64 bit
    public void tranfer() {
        for (int i = 0; i < 4; i++) {
            nhiphan16(dauvao4int[i]);
            for (int j = 0; j < 16; j++) {
                dauvao[i * 16 + j] = binary16[j];
            }
        }
    }

    // hoan vi dau vao 64 bit
    public int[] hoanviIP(int[] dauvaox) {
        int[] daurahoanvi = new int[64];
        for (int i = 0; i < 64; i++) {
            daurahoanvi[i] = dauvaox[IP[i]];
        }
        return daurahoanvi;
    }

    // hoan vi cuoi vong lap thu 16
    public int[] hoanviIP1(int[] dauvaox) {
        int[] daurahoanvi = new int[64];
        for (int i = 0; i < 64; i++) {
            daurahoanvi[i] = dauvaox[IP1[i]];
        }
        return daurahoanvi;
    }

    // hàm mở rộng 32 bit  => 48 bit;
    public int[] eBox(int[] R0) {
        int[] newR0 = new int[6];
        int[] tmp = new int[8];
        int xuongdong = 0;

        // chuyển R0 thành 32 int => mỗi int ứng với 1 bit trong R0
        for (int j = 0; j < 2; j++) {
            nhiphan16(R0[j]);
            for (int i = 0; i < 16; i++) {
                chuoi48bit[i + j * 16] = binary16[i];
            }
        }

        // dùng 32 bit đó để xử lý
        for (int j = 0; j < 48; j++) {
            tmp[xuongdong] = chuoi48bit[EBOX[j]];
            xuongdong++;
            if (xuongdong == 8) {
                newR0[j / 8] = thapphan(tmp);
                xuongdong = 0;
            }
        }
        return newR0;
    }

    // chuyen 1 so  ve 4 bit nhi phan
    public void nhiphan4(int x) { // gọi hàm nhị phân là kết quả trả về nằm ở binary;
        if (x == 0) {
            while (i4 != -1) {
                binary4[i4] = 0;
                i4--;
            }
            i4 = 3;
            return;
        } else {
            binary4[i4] = x % 2;
            i4--;
            nhiphan4(x / 2);
        }
    }

    // chuyen 1 so ve 8 bit nhi phan
    public void nhiphan8(int x) { // gọi hàm nhị phân là kết quả trả về nằm ở binary;
        if (x == 0) {
            while (i8 != -1) {
                binary8[i8] = 0;
                i8--;
            }
            i8 = 7;
            return;
        } else {
            binary8[i8] = x % 2;
            i8--;
            nhiphan8(x / 2);
        }
    }
    
    public void nhiphan16(int x) { // gọi hàm nhị phân là kết quả trả về nằm ở binary;
        if (x == 0) {
            while (i16 != -1) {
                binary16[i16] = 0;
                i16--;
            }
            i16 = 15;
            return;
        } else {
            binary16[i16] = x % 2;
            i16--;
            nhiphan16(x / 2);
        }
    }

    // chuyển dữ liệu mảng int 8 phần tủ vào mảng dauvaoSBOX[8][6]
    public void dauvaoSBOX(int[] newR0) { // chuẩn bị đầu vào cho SBOX

        // chuẩn bị đầu vào cho SBOX
        for (int j = 0; j < 3; j++) {
            nhiphan16(newR0[j]);
            for (int k = 0; k < 16; k++) {
                chuoi48bit[k + j * 16] = binary16[k];
            }
        }

        // tách chuoi48bit thành mảng 2 chiều SBOX
        for (int j = 0; j < 8; j++) {
            for (int k = 0; k < 6; k++) {
                dauvaoSBOX[j][k] = chuoi48bit[k + j * 6]; // gán từng 6 bit vào dauvaoPBOX để xử lý SBOX bên dưới
            }
        }
    }

    // chuyển kiểu dữ liệu từ bit (thực ra là mảng int, mỗi int đại diện 1 bit) => int
    public int thapphan(int[] x) {
        int k = 0;
        int n = x.length;
        int pow = 1;
        for (int j = n - 1; j > -1; j--) {
            k = k + x[j] * pow;
            pow = pow * 2;
        }
        return k;
    }

    //hàm này nhận vào 6bit và cho ra 4 bit theo S-BOX thứ key
    public void sixtofour(int[] sixbit, int keySBOX) {
        int[] fourbit = new int[]{sixbit[1], sixbit[2], sixbit[3], sixbit[4]};
        int[] twobit = new int[]{sixbit[0], sixbit[5]};

        int m = thapphan(twobit);
        int n = thapphan(fourbit);
        switch (keySBOX) {
            case 1: {
                nhiphan4(SBOX1[m][n]);
                break;
            }
            case 2: {
                nhiphan4(SBOX2[m][n]);
                break;
            }
            case 3: {
                nhiphan4(SBOX3[m][n]);
                break;
            }
            case 4: {
                nhiphan4(SBOX4[m][n]);
                break;
            }
            case 5: {
                nhiphan4(SBOX5[m][n]);
                break;
            }
            case 6: {
                nhiphan4(SBOX6[m][n]);
                break;
            }
            case 7: {
                nhiphan4(SBOX7[m][n]);
                break;
            }
            case 8: {
                nhiphan4(SBOX8[m][n]);
                break;
            }

        }
    }

    // viết gọn cho hàm sixtofour(), chạy xong cho ra dauvaoPBOX;
    public void sBOX() {
        // sử dụng bảng SBOX;
        // gán giá trị cho đầu váo PBOX
        for (int j = 0; j < 8; j++) {
            sixtofour(dauvaoSBOX[j], j + 1);
            for (int k = 0; k < 4; k++) {
                dauvaoPBOX[j * 4 + k] = binary4[k];
            }
        }
    }

    public void pBOX() {
        // sử dụng hoán vị PBOX => kết quả đầu ra
        int[] tmp = new int[16];
        int xuongdong = 0;

        // hoán vị chueyern về thập phân cho vào R0; ok
        for (int j = 0; j < 32; j++) {
            tmp[xuongdong] = dauvaoPBOX[PBOX[j]];
            xuongdong++;
            if (xuongdong == 16) {
                R0[j / 16] = thapphan(tmp);
                xuongdong = 0;
            }
        }
    }

    public void DES() { //một vòng lặp
        int[] x8bit = new int[16];
        tranfer();
        // hoan vi dau vao
        dauvao = hoanviIP(dauvao); // hoãn vị đầu vào
        int circular = 0; // vong lap

        //tách dauvao đã hoán vị thành 2 nửa L0, R0
        for (int i = 0; i < 64; i++) {
            x8bit[i % 16] = dauvao[i]; // lấy từng 8 bit để chuyển về thập phân
            if (i % 16 == 15) {
                x8bit[i % 16] = dauvao[i]; // lấy từng 8 bit để chuyển về thập phân
                if (i / 16 < 2) {
                    L0[i / 16] = thapphan(x8bit);
                } else {
                    R0[i / 16 - 2] = thapphan(x8bit);
                }
                
            }
        }

        // tạo 16 cái khóa
        tao16khoa(khoadauvao);
        // bước vào vòng lặp
        while (circular < 16) {

            // lưu lại R0 để tí gán vào L0;
            for (int i = 0; i < 2; i++) {
                saveR0[i] = R0[i];
            }
            // hàm FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
            
            // mở rộng R0 thành 48bit
            newR0 = eBox(R0);
            // thực hiện XOR newR0 với khóa
            // lựa chọn xem giải mã hay mã hóa, true =  mã hóa
            if (what) {
                for (int i = 0; i < 6; i++) {
                    newR0[i] = newR0[i] ^ key16x6byte[circular][i];
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    newR0[i] = newR0[i] ^ key16x6byte[15 - circular][i];
                }
            }
            // chuẩn bị đầu vào cho SBOX, nhận vào 6 int newR0, cho ra mảng 2 chiều dauvaoSBOX[8][6]
            dauvaoSBOX(newR0);
            // chạy SBOX, cho dữ liệu ra đầu vào PBOX
            sBOX();
            // chạy PBOX để sắp xếp lại mảng 2 chiều, gán dữ liệu lại R0, và L0
            pBOX();

            // gán trả lại L0 = R0; R0 = L0(+) functionf(R0,L0)
            for (int j = 0; j < 2; j++) {
                R0[j] = R0[j] ^ L0[j]; // hàm f
            }
            for (int i = 0; i < 2; i++) {
                L0[i] = saveR0[i];
            }
            circular++;
        }

        // vì vòng cuối theo lý thuyết là không hoasn vị R0 và Lo nhưng trong thuật toán lỡ hoán vị rồi nên sắp xếp đầu
        // vào cho IP-1 là theo thứ tự chuyển mã nhị phân R0 vào trước rồi mới chuyển L0 vào sau.
        for (int i = 0; i < 2; i++) {
            nhiphan16(R0[i]);
            for (int j = 0; j < 16; j++) {
                dauvao[i * 16 + j] = binary16[j];
            }
        }
        for (int i = 0; i < 2; i++) {
            nhiphan16(L0[i]);
            for (int j = 0; j < 16; j++) {
                dauvao[i * 16 + j + 32] = binary16[j];
            }
        }
        
        dauvao = hoanviIP1(dauvao);
        for (int i = 0; i < 64; i++) {
            x8bit[i%16] = dauvao[i];
            if(i%16 == 15){
                dauvao4int[i/16] = thapphan(x8bit);
            }
        }

    }

    boolean what;
    int[] newR0;
    int[] dauvao; // 64 bit
    int[] dauvao4int; // 8 byte dau vao // truyền vào ở đây 8 byte
    int[][] key16x6byte; // dùng cho việc xor, mỗi
    int[] khoadauvao; // 8 byte // truyền vào ở đây 8 byte khóa
    int[] L0;
    int[] R0;
    int[] saveR0;
    static int i16 = 15; // phục vụ cho hàm nhiphan16()
    static int i8 = 7; // phục vụ cho hàm nhiphan8()
    static int i4 = 3; // phục vụ cho hàm nhiphan4()
    int[] binary16; // đầu ra mã nhị phân
    int[] binary8; // đầu ra mã nhị phân
    int[] binary4; // dau ra SBOX
    int[] chuoi48bit;
    int[][] dauvaoSBOX;
    int[] dauvaoPBOX; // đầu vào PBOX tạm thời để dạng ma trận 2 chiều đã. để nhận dữ liệu từ đầu ra SBOX cho dễ
    static final int[][] SBOX1 = new int[][]{
        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    static final int[][] SBOX2 = new int[][]{
        {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
        {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
        {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
    };

    static final int[][] SBOX3 = new int[][]{
        {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
        {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
        {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
    };

    static final int[][] SBOX4 = new int[][]{
        {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
        {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
        {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
    };

    static final int[][] SBOX5 = new int[][]{
        {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
        {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
        {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };

    static final int[][] SBOX6 = new int[][]{
        {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
        {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
        {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
    };

    static final int[][] SBOX7 = new int[][]{
        {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
        {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
        {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
    };

    static final int[][] SBOX8 = new int[][]{
        {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
        {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
        {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    static final int[] PBOX = new int[]{
        15, 6, 19, 20,
        28, 11, 27, 16,
        0, 14, 22, 25,
        4, 17, 30, 9,
        1, 7, 23, 13,
        31, 26, 2, 8,
        18, 12, 29, 5,
        21, 10, 3, 24};
    static final int[] EBOX = new int[]{
        31, 0, 1, 2, 3, 4,
        3, 4, 5, 6, 7, 8,
        7, 8, 9, 10, 11, 12,
        11, 12, 13, 14, 15, 16,
        15, 16, 17, 18, 19, 20,
        19, 20, 21, 22, 23, 24,
        23, 24, 25, 26, 27, 28,
        27, 28, 29, 30, 31, 0
    };
    static final int[] IP = new int[]{
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7,
        56, 48, 40, 32, 24, 16, 8, 0,
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6
    };

    static final int[] IP1 = new int[]{
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25,
        32, 0, 40, 8, 48, 16, 56, 24
    };
    static final int[] PC1 = new int[]{
        56, 48, 40, 32, 24, 16, 8,
        0, 57, 49, 41, 33, 25, 17,
        9, 1, 58, 50, 42, 34, 26,
        18, 10, 2, 59, 51, 43, 35,
        62, 54, 46, 38, 30, 22, 14,
        6, 61, 53, 45, 37, 29, 21,
        13, 5, 60, 52, 44, 36, 28,
        20, 12, 4, 27, 19, 11, 3
    };

    static final int[] LS = new int[]{1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    static final int[] PC2 = new int[]{
        13, 16, 10, 23, 0, 4,
        2, 27, 14, 5, 20, 9,
        22, 18, 11, 3, 25, 7,
        15, 6, 26, 19, 12, 1,
        40, 51, 30, 36, 46, 54,
        29, 39, 50, 44, 32, 47,
        43, 48, 38, 55, 33, 52,
        45, 41, 49, 35, 28, 31
    };
}
