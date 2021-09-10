package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIMain extends JFrame {
    private Game game;
    private final int BLOCK_SIZE = 100;
    private JPanel panel;
    private JLabel labelScore;

    private GUIMain(){
        game = new Game();
        initPanel();
        initFrame();
        createButtons();
        createLabels();
        addKeyListener(new KeyPressListener());
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //выходило при закрытии
        setTitle("2048");
        setVisible(true);
        setResizable(false); //размеры не изменялись
        pack(); //размеры по размеру панели
        setLocationRelativeTo(null); //чтобы по центру
    }

    private void createLabels(){
        labelScore = new JLabel("Счет: 0");
        labelScore.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(labelScore);
    }

    private void createButtons(){
        JButton button = new JButton("Новая игра");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.LIGHT_GRAY);
        button.setFocusable(false);
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.newGame();
                panel.validate();
                panel.repaint();
            }
        });
    }

    private void initPanel() {
        panel = new JPanel()
        {
            //создается анонимный класс
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int deltaY = 60;
                g.setFont(new Font("TimesRoman", Font.PLAIN, 35));

                int startX = 60;
                int startY = 10;
                for (int i = 0; i < Game.SIZE; i++) {
                    for (int j = 0; j < Game.SIZE; j++) {
                        int y = startX + BLOCK_SIZE * i;
                        int x = startY + BLOCK_SIZE * j;

                        g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                        if (game.get(i, j) != 0){
                            String val = String.valueOf(game.get(i,j));
                            int deltaX;
                            if (val.length() == 1)
                                deltaX = 35;
                            else if (val.length() == 2){
                                deltaX = 25;
                            }
                            else if (val.length() == 3){
                                deltaX = 18;
                            }
                            else {
                                deltaX = 10;
                            }
                            g.drawString(val,  x + deltaX, y + deltaY);
                        }
                    }
                }
            }
        };

        panel.setPreferredSize(new Dimension(
                Game.SIZE * BLOCK_SIZE + 20,
                Game.SIZE * BLOCK_SIZE + 70));
        add(panel);
    }

    private class KeyPressListener implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            String keyText = KeyEvent.getKeyText(code);
            DirectionType direction = DirectionType.None;
            if (keyText.equals("Up"))
                direction = DirectionType.Up;
            else if (keyText.equals("Down"))
                direction = DirectionType.Down;
            else if (keyText.equals("Left") )
                direction = DirectionType.Left;
            else if (keyText.equals("Right"))
                direction = DirectionType.Right;

            if (direction != DirectionType.None)
            {
                game.move(direction);
                panel.validate();
                panel.repaint();

                labelScore.setText("Счет: " + game.getScore());

                if (game.getStatus() != StatusType.Play){
                    String message;
                    if (game.getStatus() == StatusType.Win){
                        message = "Вы выйграли. Желаете продолжить?";
                    }
                    else {
                        message = "Вы проиграли. Желаете начать заново?";
                    }
                    int result = JOptionPane.showConfirmDialog(
                            GUIMain.this,
                            message,
                            "",
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION)
                    {
                        game.newGame();
                        panel.validate();
                        panel.repaint();
                    }
                    else
                    {
                        System.exit(0);
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public static void main(String[] args) {
        new GUIMain();
    }
}
