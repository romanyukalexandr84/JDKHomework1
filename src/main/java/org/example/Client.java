package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class Client extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8180");
    private final JTextField tfLogin = new JTextField(String.valueOf(Users.values()[new Random().nextInt(Users.values().length)]));
    private final JPasswordField tfPassword = new JPasswordField("789456");
    private final JButton btnLogin = new JButton("Войти");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Отправить");
    private Server server;
    private boolean isUserOnline;


    public Client(Server server, int x, int y) {
        this.server = server;
        isUserOnline = false;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(x, y, WIDTH, HEIGHT);
        setTitle("Чат пользователя");

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
        for (String msg : Server.readFile(Server.messagesLog)) {
            log.append(msg + "\n");
        }

        setVisible(true);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerWorking) {
                    isUserOnline = true;
                    System.out.println("Пользователь " + tfLogin.getText() + " онлайн");
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerWorking && isUserOnline) {
                    String message = tfLogin.getText() + ": " + tfMessage.getText();
                    log.append(message + "\n");
                    if (!Server.messagesLog.exists()) {
                        try {
                            Server.messagesLog.createNewFile();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    Server.writeFile(Server.readFile(Server.messagesLog), Server.messagesLog, message);
                }
            }
        });

    }



}
