# 🎭 Java RMI Theater Booking System

A Distributed Systems lab project (Spring 2024-25) using **Java RMI** to simulate a theater seat booking platform supporting multiple concurrent clients.

## 📌 Description

This Java RMI client-server application allows users to:
- View available theater seats per zone and cost.
- Make bookings with real-time availability check.
- Cancel existing bookings.
- View all clients and their bookings.
- Register for seat availability notifications when a zone is full.

> Seat types and prices:
- ΠΑ (Zone A): 100 seats × 50€
- ΠΒ (Zone B): 200 seats × 40€
- ΠΓ (Zone C): 300 seats × 30€
- ΚΕ (Balcony): 250 seats × 35€
- ΠΘ (Side Box): 50 seats × 25€

## 🧩 Architecture

- **THInterface**: Remote interface defining RMI methods.
- **THImpl**: Server-side implementation of the interface.
- **THServer**: RMI server that handles theater logic and booking.
- **THClient**: Console-based RMI client with command-line arguments.

## 🖥️ Usage

Compile and run the server:
```bash
javac *.java
rmiregistry &
java THServer
