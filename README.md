# Sockets-tcp
Simple Java TCP socket apps: a chat and a number multiplier server
# 🔌 Java TCP Socket Applications – Simple Examples

This repository contains two basic Java TCP socket applications built to demonstrate fundamental concepts of client-server communication using sockets.

## 📁 Contents

### 1. 🧮 Multiplication Server
- A minimal application where:
  - The **client** sends a number
  - The **server** receives it, multiplies it by 2, and sends back the result
- Useful for learning basic socket communication and data handling with streams

### 2. 💬 Multi-Client-Server Chat(one-to-one)
- A simple **private chat system** between server and each client
- Features:
  - Supports multiple client connections via multithreading
  - The **server maintains a list** of all connected clients
   - The server can **respond individually** to each client by selecting their number from a list
  - Real-time bidirectional messaging
  -Clean client disconnection handling

## 🧠 Key Concepts Covered
- Java `Socket` and `ServerSocket` usage
- Input/output stream communication with `BufferedReader` and `PrintWriter`
- Basic networking principles using TCP
- Multi-threaded server architecture (`Runnable`, `Thread`)
- Private server-to-client messaging (not a broadcast chat)

## 🚀 Getting Started

1. Compile the code:
```bash
javac Server.java
javac client.java
javac ClientHandler.java


