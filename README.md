
# BrightPath

**BrightPath** is a comprehensive school management mobile application designed to enhance communication, resource accessibility, and administrative efficiency within school communities. It provides an integrated, user-friendly platform that caters to both students and faculty, enabling seamless interaction, easy access to resources, efficient assignment management, and streamlined communication.

---
🎥 [Watch Demo Video](https://drive.google.com/file/d/1MsHKiSogSLvUImnH-dS0oQbZklPfNN0M/view?usp=drive_link)

## Table of Contents

- [Introduction](#introduction)  
- [Motivation](#motivation)  
- [Problem Statement](#problem-statement)  
- [Features](#features)  
- [Algorithms and Models](#algorithms-and-models)  
- [Existing Systems](#existing-systems)  
- [Technology Stack](#technology-stack)  
- [Usage](#usage)  
- [Contributing](#contributing)  
- [License](#license)  
- [Contact](#contact)  

---

## Introduction

BrightPath aims to unify and simplify the way students and faculty interact with educational resources and administrative processes. By consolidating notices, assignments, grades, galleries, and communication into one platform, BrightPath fosters a connected and organized school environment.

---

## Motivation

Educational institutions often struggle with fragmented systems that hinder effective communication and resource management. BrightPath was developed to address these challenges by providing a centralized, easy-to-use digital platform to improve access to school resources, streamline communication, and enhance administrative workflows.

---

## Problem Statement

Despite numerous educational tools, schools face difficulties in managing student-teacher interactions, educational materials, and administrative tasks due to scattered and complex systems. There is a pressing need for an integrated, role-based platform that provides both students and faculty with efficient access and management of school resources and communication channels.

---

## Features

- **Access to School Resources**: Easy access to notices, galleries, assignments, and faculty information.
- **Notice Management**: Faculty can post and manage notices visible to all students.
- **Gallery Viewing**: Interactive school event galleries for students and faculty.
- **Assignment Management**: Faculty upload assignments and set deadlines; students can download and submit assignments.
- **Marks Issuance**: Faculty can input and track student grades across semesters.
- **Personalized User Profiles**: Students and faculty can update academic and contact information.
- **Role-Based Access Control (RBAC)**: Secure, role-specific feature access.
- **Real-Time Data Synchronization**: Instant updates via Firebase backend, including offline support.
- **Notification & Reminder System**: Push notifications and event-driven reminders for important updates.

---

## Algorithms and Models

### 1. Role-Based Access Control (RBAC)
- Ensures users access only relevant features based on their role (student or faculty).
- UI adapts dynamically according to user permissions.

### 2. Data Synchronization
- Real-time syncing of data (notices, assignments) between client and Firebase backend.
- Offline access via local caching.

### 3. Notification and Reminder System
- Real-time push notifications using Firebase Cloud Messaging.
- Event-driven reminders for deadlines and exams.

---

## Existing Systems

BrightPath differentiates itself from existing platforms like:

- **Google Classroom** — Limited to class and assignment management.
- **Moodle** — Strong course management but lacks broader school-wide administrative tasks.
- **Remind** — Focuses on communication without academic management features.
- **Schoology** — Comprehensive academic tools but lacks streamlined school-wide notices.
- **Edmodo** — Classroom management with limited integration of school resources.
- **PowerSchool** — Academic tracking with minimal access to non-academic school resources.

BrightPath offers an all-in-one, user-friendly platform consolidating academic management, communication, and resource access.

---

## Technology Stack

- Kotlin
- Backend: Firebase Firestore and Firebase Cloud Messaging
- Authentication: Firebase Authentication
- Real-time Sync: Firebase Firestore
- Notifications: Firebase Cloud Messaging

---

## Usage

- Register as a student or faculty member.
- Access notices, gallery, assignments, and other resources based on your role.
- Faculty can post notices, upload assignments, and manage marks.
- Students can view and submit assignments and check grades.
- Receive notifications for important events and deadlines.

---

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

---

## License

This project is licensed under the MIT License.

---
