# 🏥 Healthcare Management System

This project aims to develop a **Prescription and Doctor Visit Management System** for the **Ministry of Health**. Doctors can create prescriptions for patients, pharmacies can manage prescriptions and the system can detect missing prescriptions and notify pharmacies.

## 🚀 Project Components

1. **Prescription Service** - Allows doctors to create prescriptions and pharmacies to continue processing prescriptions.
2. **Doctor Visit Service ** - Records each patient's visit to the doctor.
3. **Medicine Service - Manages and regularly updates the medication list.
4. **Notification Service** - Sends notifications to pharmacies for missing medications.
5. **API Gateway** - Gateway that routes all services from a central point.
6. **Queue Management (RabbitMQ)** - Provides message queue management for processing missing prescriptions.
7. **Authentication (JWT Token)** - Performs user authentication and authorization.

## 📌 Technologies Used

- Backend:** Java 17, Spring Boot, Spring Cloud Gateway, Spring Security, Hibernate, RabbitMQ, Redis
- **Frontend:** React, Next.js, Material UI, React Hook Form
- Database:** PostgreSQL, NoSQL (MongoDB)
- **Message Queue:** RabbitMQ
- Deployment:** Google Cloud
- **Caching:** Redis

##📡 Prescription Process

- The doctor creates a prescription for the patient and saves it in the system.
- The pharmacy receives the prescription and checks the correctness of the medication.
- The system checks if there are missing medicines. If there are missing medicines, it adds them to the queue with RabbitMQ.
- Notification Service sends an e-mail notification to the pharmacy for missing prescriptions.

🔥 Notes about the Project

    Medicine Service updates the list of medicines published by the Ministry of Health on a weekly basis.
    Redis uses drug names and cached data for quick access.
    RabbitMQ manages the notification queue for missing prescriptions.
    Spring Security (JWT) handles authorization for doctors and pharmacists.

⚙️ Development Notes

    The problem of Medicine ID coming empty has been solved by adding id to the medicine search API.
    Instant submission process has been added after the prescription is created.
    JSON depth error occurred due to RabbitMQ queue, the maximum depth limit was increased.
