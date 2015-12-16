package com.gs.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangGenshen on 12/16/15.
 */
public class GameMapDesigner extends JFrame implements ActionListener {

    private int width;
    private int height;
    private int size;
    private String type;

    private JLabel widthLbl;
    private JLabel heightLbl;
    private JLabel sizeLbl;
    private JLabel typeLbl;

    private JTextField widthFld;
    private JTextField heightFld;
    private JTextField sizeFld;
    private JTextField typeFld;
    private JButton generateMapBtn;
    private JButton saveBtn;

    private JPanel centerPanel;
    private JPanel bottomPanel;

    private List<Barrier> barriers;
    private List<Barrier> toMovedBarriers;

    public GameMapDesigner() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setSize(800, 600);
        setTitle("游戏地图设计器");
        getContentPane().setLayout(new BorderLayout());
        initWidgets();
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        barriers = new ArrayList<Barrier>();
        toMovedBarriers = new ArrayList<Barrier>();
    }

    private void initWidgets() {
        widthLbl = new JLabel("地图宽度:");
        heightLbl = new JLabel("地图高度:");
        sizeLbl = new JLabel("地图格子尺寸:");
        typeLbl = new JLabel("格子类型:");
        widthFld = new JTextField(4);
        heightFld = new JTextField(4);
        sizeFld = new JTextField(4);
        typeFld = new JTextField(10);
        generateMapBtn = new JButton("生成地图");
        generateMapBtn.setName("generateMap");
        generateMapBtn.addActionListener(this);
        saveBtn = new JButton("生成数组");
        saveBtn.setName("save");
        saveBtn.addActionListener(this);
        centerPanel = new JPanel();
        centerPanel.setLayout(null);
        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(widthLbl);
        bottomPanel.add(widthFld);
        bottomPanel.add(heightLbl);
        bottomPanel.add(heightFld);
        bottomPanel.add(sizeLbl);
        bottomPanel.add(sizeFld);
        bottomPanel.add(typeLbl);
        bottomPanel.add(typeFld);
        bottomPanel.add(generateMapBtn);
        bottomPanel.add(saveBtn);
    }

    private void initCenterMap() {
        String widthStr = widthFld.getText();
        String heightStr = heightFld.getText();
        String sizeStr = sizeFld.getText();
        type = typeFld.getText();
        if(widthStr == null || widthStr.length() ==0 || heightStr == null || heightStr.length() == 0 ||
                sizeStr == null || sizeStr.length() == 0) {

        } else {
            width = Integer.valueOf(widthStr);
            height = Integer.valueOf(heightStr);
            size = Integer.valueOf(sizeStr);
            int cols = width / size;
            int rows = height / size;
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    JButton btn = new JButton();
                    btn.setName(i + "," + j);
                    btn.setBackground(Color.BLUE);
                    btn.setBounds(50 + j * size, 50 + i * size, size, size);
                    btn.addActionListener(this);
                    centerPanel.add(btn);
                }
            }
            centerPanel.validate();
            centerPanel.repaint();
        }
    }

    public void saveBarriers() {
        String str = "";
        barriers.removeAll(toMovedBarriers);
        for(int i = 0, size = barriers.size(); i < size; i++) {
            Barrier barrier = barriers.get(i);
            if((i + 1) % 10 == 0) {
                str += "\n";
            }
            if(str.equals("")) {
                str += "{" + barrier.getRow() + ", " + barrier.getCol() + ", " + barrier.getType() + "}";
            } else {
                str += ", {" + barrier.getRow() + ", " + barrier.getCol() + ", " + barrier.getType() + "}";
            }
        }
        System.out.println(str);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source instanceof JButton) {
            JButton btn = (JButton) source;
            String name = btn.getName();
            if(name.equals("generateMap")) {
                initCenterMap();
            } else if(name.equals("save")) {
                for(Barrier barrier : barriers) {
                    barrier.setType(typeFld.getText());
                }
                saveBarriers();
            } else {
                Color oColor = btn.getBackground();
                String[] rowCol = name.split(",");
                int row = Integer.valueOf(rowCol[0]);
                int col = Integer.valueOf(rowCol[1]);
                if(oColor == Color.BLUE) {
                    btn.setBackground(Color.WHITE);
                    Barrier barrier = new Barrier();
                    barrier.setRow(row);
                    barrier.setCol(col);
                    barrier.setType(type);
                    barriers.add(barrier);
                } else {
                    for(Barrier barrier : barriers) {
                        btn.setBackground(Color.BLUE);
                        if(barrier.getRow() == row && barrier.getCol() == col) {
                            toMovedBarriers.add(barrier);
                        }
                    }
                }
            }
        }
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GameMapDesigner();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
