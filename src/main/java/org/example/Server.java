package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends JFrame {
    private static final int POS_X = 200;
    private static final int POS_Y = 150;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JTextArea statusField;
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    public final JButton btnStart = new JButton("Запустить сервер");
    public final JButton btnStop = new JButton("Остановить сервер");
    public boolean isServerWorking;
    public ArrayList<Client> clients;
    public File messagesLog = new File("./src/main/java/org/example/chat_log.txt");

    public Server() {
        isServerWorking = false;
        clients = new ArrayList<>();
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                statusField.setText("Сервер запущен");
                String message = "Чат доступен\n";
                for (Client client : clients) {
                    client.messagesField.append(message);
                }
                writeFile(readFile(messagesLog), messagesLog, message);
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                statusField.setText("Сервер остановлен");
                String message = "Чат временно недоступен из-за работ на сервере\n" +
                        "Пользователи вышли из чата\n";
                for (Client client : clients) {
                    client.isUserOnline = false;
                    client.messagesField.append(message);
                }
                writeFile(readFile(messagesLog), messagesLog, message);
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Сервер чата");
        setAlwaysOnTop(true);
        statusField = new JTextArea();
        add(statusField, BorderLayout.SOUTH);

        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.add(btnStart);
        mainPanel.add(btnStop);
        add(mainPanel);

        setVisible(true);
    }

    //Метод чтения файла лога
    public List<String> readFile(File file) {
        List<String> lst = new ArrayList<>();
        try (FileReader fr = new FileReader(file); BufferedReader bf = new BufferedReader(fr)) {
            String line;
            while ((line = bf.readLine()) != null) {
                lst.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + file.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lst;
    }

    //Метод записи данных в файл лога
    public void writeFile(List<String> lst, File file, String message) {
        try (FileWriter fw = new FileWriter(file); BufferedWriter bf = new BufferedWriter(fw)) {
            for (String item : lst) {
                bf.write(item);
                bf.newLine();
            }
            bf.write(message);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + file.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
