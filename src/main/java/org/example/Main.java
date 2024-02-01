package org.example;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        new Client(server, 700, 500);
        new Client(server, 700, 150);
        new Client(server, 200, 500);
    }
}
