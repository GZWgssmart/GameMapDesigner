package com.gs.designer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangGenshen on 12/16/15.
 */
public class GameMapDesigner extends JFrame implements ActionListener {

    private String type;

    private JTextField widthFld;
    private JTextField heightFld;
    private JTextField sizeFld;
    private JTextField typeFld;

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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        barriers = new ArrayList<>();
        toMovedBarriers = new ArrayList<>();
    }

    private void initWidgets() {
        JLabel widthLbl = new JLabel("地图宽度:");
        JLabel heightLbl = new JLabel("地图高度:");
        JLabel sizeLbl = new JLabel("地图格子尺寸:");
        JLabel typeLbl = new JLabel("格子类型:");
        widthFld = new JTextField(4);
        heightFld = new JTextField(4);
        sizeFld = new JTextField(4);
        typeFld = new JTextField(10);
        JButton generateMapBtn = new JButton("生成地图");
        generateMapBtn.setName("generateMap");
        generateMapBtn.addActionListener(this);
        JButton saveBtn = new JButton("生成数组");
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
            // TODO show the dialog to user
        } else {
            int width = Integer.valueOf(widthStr);
            int height = Integer.valueOf(heightStr);
            int size = Integer.valueOf(sizeStr);
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

    private void saveBarriers() throws IOException {
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
        outToFile(str);
        System.out.println(str);
    }

    private void outToFile(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("barrier.txt")));
        writer.write(str);
        writer.flush();
        writer.close();
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
                try {
                    saveBarriers();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
