/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninhhoangcuong.server;

import UDP.Student;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ninhh
 */
public class Server {

    public static Random ran;
    public static final boolean FLOAT = true;

    public static byte[] objectToByte(Student student) {
        byte[] bs = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(baos);
            oo.writeObject(student);
            oo.flush();
            bs = baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                baos.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bs;
    }

    public static Student byteToObject(byte[] bs) {
        Student student = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
        ObjectInput oi = null;
        try {
            oi = new ObjectInputStream(bais);
            student = (Student) oi.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (oi != null) {
                try {
                    oi.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return student;
    }

    public static int RandomNumber() {
        ran = new Random();
        int id = ran.nextInt(10000);
        return (id >= 0) ? id : 879;
    }

    public static float RandomNumber(boolean flag) {
        ran = new Random();
        float gpa = ran.nextFloat()*4;
        return gpa;
    }
    public static String ConvertGPA(float gpa){
        if(3.7<=gpa&&gpa<=4){
            return "A";
        }else  if(3.0<=gpa&&gpa<=3.7){
            return "B";
        }else  if(2.0<=gpa&&gpa<=3.0){
            return "C";
        }else if(1.0<=gpa&&gpa<=2.0){
            return "D";
        }if(0<=gpa&&gpa<=1.0){
            return "F";
        }
        return null;
    }
    public static void main(String[] args) {
        try {
            DatagramSocket socketServer = null;
            DatagramPacket sendPacket, receivePacket;
            int port = 1109;
            byte[] send, receive;
            //open server
            socketServer = new DatagramSocket(port);
            System.out.println("Server is runing ......");
            while (true) {
                //nhan du lieu tu client
                receive = new byte[1024];
                receivePacket = new DatagramPacket(receive, receive.length);
                socketServer.receive(receivePacket);
                //chuyen ve dang student
                Student student = byteToObject(receivePacket.getData());
                System.out.println("Client : " + student.getCode());
                //set thuoc tinh va gui lai cho client
                student.setId(RandomNumber());
                student.setGpa(RandomNumber(FLOAT));
                //send to client
                send = objectToByte(student);
                sendPacket = new DatagramPacket(send, send.length, receivePacket.getSocketAddress());
                socketServer.send(sendPacket);
                System.out.println("Send to client : " + student.toString());
                System.out.println("Send successful...");
                //convert to GAPletter
                student.setGpaLetter(ConvertGPA(student.getGpa()));
                //nhan lai va so sanh
                //nhan du lieu tu client
                receive = new byte[1024];
                receivePacket = new DatagramPacket(receive, receive.length);
                socketServer.receive(receivePacket);
                //chuyen ve dang student
                Student student2 = byteToObject(receivePacket.getData());
                System.out.println("receive from client : " + student2.toString());
                System.out.println("Result : "+(student2.getGpaLetter().equals(student.getGpaLetter())));
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
