/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicgraphics.cards;

import basicgraphics.BasicContainer;
import basicgraphics.BasicFrame;
import basicgraphics.images.Picture;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is intended to provide you with examples
 * for the Card layout and the dialog classes.
 * @author sbrandt
 */
public class CardLayoutExample {
    static String[][] layout = {
        {"Btn1","Btn2","Btn3"},
        {"Btn4","Card","Card"},
        {"Btn5","Card","Card"}
    };
    public static void main(String[] args) throws Exception {
        BasicFrame bf = new BasicFrame("Card Layout");
        JButton jb1 = new JButton("Vertical");
        JButton jb2 = new JButton("Horizontal");
        JButton jb3 = new JButton("Get Name");
        bf.add(layout, "Btn1", jb1);
        bf.add(layout, "Btn2", jb2);
        bf.add(layout, "Btn3", jb3);
        bf.add(layout, "Btn4", new JButton("Button 4"));
        bf.add(layout, "Btn5", new JButton("Button 5"));
        final JPanel jp = new JPanel();
        final CardLayout cards = new CardLayout();
        jp.setLayout(cards);
    
        BasicContainer card1 = new BasicContainer();
        Picture bat = new Picture("bat.png");
        ImageIcon icon1 = new ImageIcon(bat.getImage());
            
        String[][] layout1 = {
            {"Click"},
            {"Bat"}};
        card1.add(layout1,"Bat",new JLabel(icon1));
        JButton click1 = new JButton("click");
        card1.add(layout1,"Click",click1);
        
         String[][] layout2 = {
            {"Click","Bat"}};
        JButton click2 = new JButton("click");
        BasicContainer card2 = new BasicContainer();
        card2.add(layout2,"Bat",new JLabel(icon1));
        card2.add(layout2,"Click",click2);
        
        jp.add(card1,"Panel 1");
        jp.add(card2,"Panel 2");
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(jp,"Panel 1");
            }
        });
        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.show(jp,"Panel 2");
            }
        });
        bf.add(layout,"Card",jp);
        
        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog jd = new JDialog(
                        BasicFrame.getFrame(),
                        "Get Name",
                        Dialog.ModalityType.APPLICATION_MODAL);
                BasicContainer bc = new BasicContainer();
                String[][] layout = {
                    {"Name"  ,"Input"},
                    {"Submit","Submit"}
                };
                JButton submit = new JButton("submit");
                final JTextField input = new JTextField("Chris");
                bc.add(layout,"Name",new JLabel("Name"));
                bc.add(layout,"Input",input);
                bc.add(layout,"Submit",submit);
                jd.add(bc);
                submit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int rc = JOptionPane.showConfirmDialog(jd, "Text:" + input.getText());
                        System.out.println("rc=" + rc);
                        if (rc == JOptionPane.OK_OPTION) {
                            System.out.println("OK");
                        } else if (rc == JOptionPane.OK_CANCEL_OPTION) {
                            System.out.println("Cancel");
                        } else if (rc == JOptionPane.NO_OPTION) {
                            System.out.println("No");
                        }
                        jd.dispose();
                    }
                });
                jd.pack();
                jd.setVisible(true);
            }
        });
        bf.show();
    }
}
