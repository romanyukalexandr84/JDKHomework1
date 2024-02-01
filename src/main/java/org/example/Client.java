package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.Server.readFile;

public class Client extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8180");
    private static int userID = Users.values().length - 1;
    private final String username = String.valueOf(Users.values()[userID--]);
    final JTextField tfLogin = new JTextField(username);
    private final JPasswordField tfPassword = new JPasswordField("789456");
    private final JButton btnLogin = new JButton("Войти");
    private final JButton btnUnLogin = new JButton("Выйти");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Отправить");

    private Server server;
    public boolean isUserOnline;
    JTextArea messagesField;
    public static ArrayList<Client> clients = new ArrayList<>();

    public Client(Server server, int x, int y) {
        this.server = server;
        isUserOnline = false;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(x, y, WIDTH, HEIGHT);
        setTitle("Чат пользователя " + this.username);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelTop.add(btnUnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        messagesField = new JTextArea();
        messagesField.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(messagesField);
        add(scrollLog);

        setVisible(true);

        server.btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messagesField.setText(null);
                for (String msg : readFile(Server.messagesLog)) {
                    messagesField.append(msg + "\n");
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerWorking) {
                    isUserOnline = true;
                    String message = "Пользователь " + tfLogin.getText() + " онлайн" + "\n";
                    for (Client client : clients) {
                        client.messagesField.append(message);
                    }
                    Server.writeFile(readFile(Server.messagesLog), Server.messagesLog, message);
                }
            }
        });

        btnUnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerWorking) {
                    isUserOnline = false;
                    String message = "Пользователь " + tfLogin.getText() + " покинул чат" + "\n";
                    for (Client client : clients) {
                        client.messagesField.append(message);
                    }
                    Server.writeFile(readFile(Server.messagesLog), Server.messagesLog, message);
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server.isServerWorking && isUserOnline) {
                    String message = tfLogin.getText() + ": " + tfMessage.getText();
                    for (Client client : clients) {
                        client.messagesField.append(message + "\n");
                    }
                    if (!Server.messagesLog.exists()) {
                        try {
                            Server.messagesLog.createNewFile();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    Server.writeFile(readFile(Server.messagesLog), Server.messagesLog, message);
                }
            }
        });
        clients.add(this);
    }

}
