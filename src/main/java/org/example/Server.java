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
    private static final int POS_Y = 350;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JButton btnStart = new JButton("Запустить сервер");
    private final JButton btnStop = new JButton("Остановить сервер");
    //private final JTextArea log = new JTextArea();
    public boolean isServerWorking;
    public static File messagesLog = new File("./messages_log.txt");


    public Server() {
        isServerWorking = false;
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                System.out.println("Сервер запущен");
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                System.out.println("Сервер остановлен");
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Сервер чата");
        setAlwaysOnTop(true);
        setLayout(new GridLayout(1, 2));
        add(btnStart);
        add(btnStop);

        setVisible(true);
    }

    //Метод чтения файла лога
    public static List<String> readFile(File file) {
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
    public static void writeFile(List<String> lst, File file, String message) {
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
