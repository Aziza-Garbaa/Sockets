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

### 2. 💬 Client-Server Chat
- A simple one-to-one text chat between client and server
- Features:
  - Real-time bidirectional messaging
  - Graceful disconnection and stream handling

## 🧠 Key Concepts Covered
- Java `Socket` and `ServerSocket` usage
- Input/output stream communication with `BufferedReader` and `PrintWriter`
- Basic networking principles using TCP

## 🚀 Getting Started

1. Compile the code:
```bash
javac server.java
javac client.java

