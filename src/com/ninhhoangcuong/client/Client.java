/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ninhhoangcuong.client;

import UDP.Student;
import com.ninhhoangcuong.server.Server;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ninhh
 */
public class Client {

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

    public static String ConvertGPA(float gpa) {
        if (3.7 <= gpa && gpa <= 4) {
            return "A";
        } else if (3.0 <= gpa && gpa <= 3.7) {
            return "B";
        } else if (2.0 <= gpa && gpa <= 3.0) {
            return "C";
        } else if (1.0 <= gpa && gpa <= 2.0) {
            return "D";
        }
        if (0 <= gpa && gpa <= 1.0) {
            return "F";
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            DatagramSocket socketClient = null;
            DatagramPacket sendPacket,receivePacket;
            int port = 1109;
            String hostName = "localhost";
            byte[] send,receive;
            //open
            socketClient = new DatagramSocket();
            Student student= new Student("B16DCCN046");
            send = objectToByte(student);
            sendPacket = new DatagramPacket(send, send.length, InetAddress.getByName(hostName), port);
            socketClient.send(sendPacket);
            System.out.println("Send succesful...");
            //nhan lai tu server
            receive = new byte[1024];
            receivePacket = new DatagramPacket(receive, receive.length);
            socketClient.receive(receivePacket);
            Student student1 = byteToObject(receivePacket.getData());
            System.out.println("Receive from server : "+student1.toString());
            //chuyen doi sang diem chu va gui cho server
            student1.setGpaLetter(ConvertGPA(student1.getGpa()));
            send = objectToByte(student1);
            sendPacket = new DatagramPacket(send, send.length, receivePacket.getSocketAddress());
            socketClient.send(sendPacket);
            System.out.println("Send to server : " + student1.toString());
            socketClient.close();
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
