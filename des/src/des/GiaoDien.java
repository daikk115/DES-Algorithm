/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package des;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Dai
 */
public class GiaoDien extends JFrame {

    JTextArea password = new JTextArea();
//    JTextField link = new JTextField();
    JTextArea textra = new JTextArea();
    JTextArea textvao = new JTextArea();
    boolean select = true;

    public GiaoDien(String title) {
        setTitle(title);
        setLocation(100, 100);
        setSize(500, 550);
        setLayout(new BorderLayout());
        JPanel tren = new JPanel();
//        tren.setBackground(Color.yellow);
        tren.setLayout(null);
        tren.setPreferredSize(new Dimension(500, 170));
        JPanel duoi = new JPanel();
        duoi.setLayout(null);
//        duoi.setBackground(Color.red);
        duoi.setPreferredSize(new Dimension(500, 350));

        password.setBounds(20, 20, 250, 35);
        Document doc = password.getDocument();
        AbstractDocument absDoc = (AbstractDocument) doc;
        absDoc.setDocumentFilter(new DocumentSizeFilter(4));

        JLabel key = new JLabel("             Key");
        key.setBackground(Color.green);
        key.setOpaque(true);
        key.setBounds(320, 20, 100, 35);
        JButton open = new JButton("Run");
        open.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (password.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "vui lòng điền khóa trước");
                } else {
                    ThuatToan moi = new ThuatToan();
                    moi.what = select;
                    String khoa = new String(password.getText());
                    if (khoa.length() == 0) {
                        JOptionPane.showMessageDialog(null, "nhập khóa trước");
                    } else {
                        for (int i = 0; i < khoa.length(); i++) {
                            moi.khoadauvao[i] = khoa.charAt(i);
                        }
                        if (select) {
                            textra.setText("");
                            xmahoa(moi, textvao.getText().toString());
                        } else {
                            textvao.setText("");
                            xmahoa(moi, textra.getText().toString());
                        }
                    }
                }
            }
        });
        open.setBounds(320, 60, 100, 35);
        JRadioButton mahoa = new JRadioButton("MÃ HÓA", true);
        JRadioButton giaima = new JRadioButton("GIẢI MÃ", false);
        mahoa.setBounds(20, 100, 200, 35);
        mahoa.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                select = true;
            }
        });
        giaima.setBounds(200, 100, 200, 35);
        giaima.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                select = false;
            }
        });
        ButtonGroup nhom = new ButtonGroup();
        nhom.add(mahoa);
        nhom.add(giaima);

        tren.add(password);
        tren.add(key);
        tren.add(open);
        tren.add(mahoa);
        tren.add(giaima);

        JScrollPane textvaoscroll = new JScrollPane(textvao);
        textvaoscroll.setBounds(10, 20, 225, 300);
        duoi.add(textvaoscroll);

        JScrollPane textrascroll = new JScrollPane(textra);
        textrascroll.setBounds(245, 20, 225, 300);
        duoi.add(textrascroll);

        add(tren, BorderLayout.NORTH);
        add(duoi, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void xmahoa(ThuatToan des, String dauvao) {
        for (int i = 0; i < 4; i++) {
            des.dauvao4int[i] = 32;
        }
        int k = 0;
        int tmp = 32;
        int dem = 0;
        if (select) {
            for (int j = 0; j < dauvao.length(); j++) {
                tmp = dauvao.charAt(j);
                if (k == 4) {
                    des.DES();
                    for (int m = 0; m < 4; m++) {
                        dem++;
                        textra.append("" + (char) des.dauvao4int[m]);
                        des.dauvao4int[m] = 32;
                    }
                    k = 0;
                    des.dauvao4int[k] = tmp;
                    k++;
                } else {
                    des.dauvao4int[k] = tmp;
                    k++;
                }
            }
            des.DES();
            for (int m = 0; m < 4; m++) {
                dem++;
                textra.append("" + (char) des.dauvao4int[m]);
                des.dauvao4int[m] = 32;
            }
        } else {
            for (int j = 0; j < dauvao.length(); j++) {
                tmp = dauvao.charAt(j);
                if (k == 4) {
                    des.DES();
                    for (int m = 0; m < 4; m++) {
                        dem++;
                        textvao.append("" + (char) des.dauvao4int[m]);
                        des.dauvao4int[m] = 32;
                    }
                    k = 0;
                    des.dauvao4int[k] = tmp;
                    k++;
                } else {
                    des.dauvao4int[k] = tmp;
                    k++;
                }
            }
            des.DES();
            for (int m = 0; m < 4; m++) {
                dem++;
                textvao.append("" + (char) des.dauvao4int[m]);
                des.dauvao4int[m] = 32;
            }
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        GiaoDien x = new GiaoDien("Đặng Văn Đại");
    }

}

class DocumentSizeFilter extends DocumentFilter {

    int maxCharacters;

    public DocumentSizeFilter(int maxChars) {
        maxCharacters = maxChars;
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
            super.insertString(fb, offs, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters) {
            super.replace(fb, offs, length, str, a);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

}
