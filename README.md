# 🔌 Applications Socket Java – Exemples TCP & UDP

Ce dépôt contient des applications simples utilisant les sockets en Java (TCP et UDP), conçues pour illustrer les principes fondamentaux de la communication client-serveur. Chaque exemple est minimaliste, pédagogique et vise à montrer la différence entre TCP et UDP.
## 📚 About Us  
Ce projet a ete develope par  .👨‍💻 
**Le trinome:** 

1️⃣**Amira El manaa**    2️⃣**Aziza Garbâa**    3️⃣**Islem Briki** 

## 📁 Contenu

### 🔷 Applications TCP

#### 1. 🧮 Serveur de Multiplication TCP  
- Une application client-serveur de base :
  - Le **client** envoie un nombre.
  - Le **serveur** le reçoit, le multiplie par 2 et renvoie le résultat.
- Utile pour apprendre :
  - La communication via TCP  
  - La gestion des flux d'entrée/sortie

#### 2. 💬 Chat Privé Multi-Client (TCP)  
- Un système de messagerie privée entre plusieurs clients.
- Fonctionnalités :
  - Serveur multi-thread pour gérer plusieurs connexions simultanées.
  - Les clients peuvent envoyer des messages privés au format `@utilisateur message`.
  - Communication bidirectionnelle en temps réel.
  - Déconnexion propre des clients.

---

### 🟡 Applications UDP

#### 3. 🧮 Serveur de Multiplication UDP  
- Une version allégée utilisant UDP :
  - Le **client** envoie un nombre via UDP.
  - Le **serveur** répond avec ce nombre multiplié par 2.
- Permet de découvrir :
  - La communication avec des datagrammes
  - Le traitement de messages sans connexion persistante

#### 4. 🌐 Chat de Groupe UDP  
- Un petit chat de groupe basé sur UDP (multicast ou broadcast).
- Fonctionnalités :
  - Tous les clients reçoivent tous les messages envoyés.
  - Pas de messagerie privée (contrairement à la version TCP).
  - Léger et idéal pour les communications en réseau local (LAN).

---

## 🧠 Concepts Clés Abordés
- **TCP** : `Socket`, `ServerSocket`, `BufferedReader`, `PrintWriter`, `Thread`
- **UDP** : `DatagramSocket`, `DatagramPacket`
- Différences entre communication avec et sans connexion
- Architecture multi-thread (TCP)
- Formatage et traitement des messages

---
