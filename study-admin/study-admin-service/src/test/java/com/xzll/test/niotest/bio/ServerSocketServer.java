package com.xzll.test.niotest.bio;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 10:13
 * @Description:
 */
public class ServerSocketServer {

    @Test
    public void client() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        Socket client = null;
        try {
            client = new Socket("127.0.0.1", 8080);
            writer = new PrintWriter(client.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {//每隔5秒发送一次请求
                writer.println("GET CURRENT TIME");
                writer.flush();
                String response = reader.readLine();
                System.out.println("Current Time:" + response);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                reader.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Test
    public void test() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(8080);
            System.out.println("TimeServer Started on 8080...");
            while (true) {
                Socket client = server.accept();
                //每次接收到一个新的客户端连接，启动一个新的线程来处理
                new Thread(new TimeServerHandler(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class TimeServerHandler implements Runnable {
        private Socket clientProxxy;

        public TimeServerHandler(Socket clientProxxy) {
            this.clientProxxy = clientProxxy;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            PrintWriter writer = null;
            try {
                reader = new BufferedReader(new InputStreamReader(clientProxxy.getInputStream()));
                writer = new PrintWriter(clientProxxy.getOutputStream());
                while (true) {//因为一个client可以发送多次请求，这里的每一次循环，相当于接收处理一次请求
                    String request = reader.readLine();
                    if (!"GET CURRENT TIME".equals(request)) {
                        writer.println("BAD_REQUEST");
                    } else {
                        writer.println(Calendar.getInstance().getTime().toLocaleString());
                    }
                    writer.flush();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    writer.close();
                    reader.close();
                    clientProxxy.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
