package com.example.study.norootinstalldemo.shellService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by WenTong on 2019/3/13.
 */

public class Service {
    private ServiceGetText mServiceGetText;


    public Service(ServiceGetText serviceGetText, int PORT) {
        mServiceGetText = serviceGetText;
        try {
            /** 创建ServerSocket*/
            // 创建一个ServerSocket在端口4521监听客户请求
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("server run on :" + PORT + "port");
            while (true) {
                // 侦听并接受到此Socket的连接,请求到来则产生一个Socket对象，并继续执行
                Socket socket = serverSocket.accept();
                System.out.println("accept...");
                new CreateServerThread(socket);//当有请求时，启一个线程处理
            }
        } catch (Exception e) {
            System.out.println("error happend::" + e);
        }finally{
//            serverSocket.close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        Socket socket;
        public CreateServerThread(Socket s) throws IOException {
            System.out.println("create a new server thread");
            socket = s;
            start();
        }

        public void run() {
            try {
                /** 获取客户端传来的信息 */
                // 由Socket对象得到输入流，并构造相应的BufferedReader对象
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("get bufferedReader");
                String line = bufferedReader.readLine();
                /** 发送服务端准备传输的 */
                // 由Socket对象得到输出流，并构造PrintWriter对象
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                System.out.println("get PrintWriter");
                // 获取从客户端读入的字符串
                System.out.println("while：get string from client");
                System.out.println("while：client return : " + line);
                String repeat = mServiceGetText.getText(line);
                System.out.println("while：client while return：" + repeat);
                //printWriter.print("hello Client, I am Server!");
                printWriter.print(repeat);
                System.out.println("while ：regresh");
                printWriter.flush();
                System.out.println("while,refresh return");
                System.out.println("close Socket");
                /** 关闭Socket*/
                printWriter.close();
                bufferedReader.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("socket error：" + e.toString());
            }
        }
    }

    public interface ServiceGetText{
        String getText(String text);
    }

}