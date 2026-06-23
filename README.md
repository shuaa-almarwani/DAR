# 🏠 DAR – Smart Home Care System

> Smart home-care platform that helps users organize homes, manage maintenance tasks, track invoices and bills, receive intelligent reminders, and get AI-powered home-care insights.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![AWS](https://img.shields.io/badge/AWS-Deployed-yellow)
![OpenAI](https://img.shields.io/badge/OpenAI-AI-purple)
![Twilio](https://img.shields.io/badge/Twilio-Notifications-red)
![Weather API](https://img.shields.io/badge/Weather-API-skyblue)


---
## 🧭 System Diagrams

This section provides a visual overview of the DAR platform structure and main user interactions.

### Use Case Diagram


![DAR Use Case Diagram](./docs/use-case-diagram.png)

### ER Diagram


![DAR ER Diagram](./docs/er-diagram.png)
---

## 🔐 Authentication & Security

The system uses JWT authentication to protect user data and secure private endpoints.


```http
Authorization: Bearer <token>
```

Security features:

* JWT-Based Authentication
* Stateless Session Management
* Role-Based Access Control
* Ownership Validation for User Resources
* Protected Home, Maintenance, Reminder, Notification, Invoice, Sensor, and Payment APIs

---



Base URL:

```http
http://Dar-env.eba-yke92rm3.eu-central-1.elasticbeanstalk.com
```

---

## 🧰 Tech Stack

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* MySQL
* AWS Elastic Beanstalk
* AWS RDS
* OpenAI API
* Weather API
* Twilio API
* Java Mail Sender
* Postman

## 👩‍💻 My Implemented Endpoints

This section highlights the API endpoints I worked on as part of my contribution to the DAR platform.  
My work focused mainly on chatbot support, maintenance tracking, maintenance reminders, notifications, smart alerts, and subscription-related features.

### 🤖 Chatbot Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/chatbot/questions` | Returns suggested chatbot questions for users. | Public |
| `POST` | `/api/v1/chatbot/ask` | Sends a user question to the chatbot and returns the answer. | Public / Authenticated depending on security config |

### 🛠️ Maintenance Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/maintenance/upcoming/{homeId}` | Returns upcoming maintenance records for a specific home. | Owner / Admin |
| `GET` | `/api/v1/maintenance/overdue/{homeId}` | Returns overdue maintenance records for a specific home. | Owner / Admin |
| `PUT` | `/api/v1/maintenance/mark-done/{maintenanceId}` | Marks a maintenance record as completed. | Owner / Admin |

### ⏰ Maintenance Reminder Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/v1/maintenance-reminder/add/{maintenanceId}` | Adds a maintenance reminder for a maintenance record. | Owner / Admin |
| `PUT` | `/api/v1/maintenance-reminder/update/{id}/{homeId}/{homeItemId}` | Updates a maintenance reminder and validates ownership of the reminder, home, and item. | Owner / Admin |
| `PUT` | `/api/v1/maintenance-reminder/mark-sent/{id}` | Marks a maintenance reminder as sent. | Owner / Admin |
| `GET` | `/api/v1/maintenance-reminder/upcoming/{homeId}` | Returns upcoming reminders for a home. | Owner / Admin |
| `GET` | `/api/v1/maintenance-reminder/today/{homeId}` | Returns today’s reminders for a home. | Owner / Admin |
| `POST` | `/api/v1/maintenance-reminder/send/{reminderId}` | Sends a maintenance reminder manually. | Owner / Admin |
| `PUT` | `/api/v1/maintenance-reminder/reactivate/{reminderId}` | Reactivates a reminder after it has been sent. | Owner / Admin |
| `GET` | `/api/v1/maintenance-reminder/summary/{homeId}` | Returns a summary of reminders for a home. | Owner / Admin |
| `GET` | `/api/v1/maintenance-reminder/ai-weather-advice/{homeId}` | Generates AI weather-based maintenance advice for a home. | Owner / Admin |

### 🔔 Notification Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `PUT` | `/api/v1/notification/mark-as-read/{notificationId}` | Marks one notification as read. | Owner / Admin |
| `PUT` | `/api/v1/notification/mark-all-as-read` | Marks all notifications of the current authenticated user as read. | Authenticated |
| `DELETE` | `/api/v1/notification/delete/{notificationId}` | Deletes a notification. | Owner / Admin |
| `GET` | `/api/v1/notification/summary` | Returns notification summary for the current authenticated user. | Authenticated |
| `POST` | `/api/v1/notification/weather-alert/{homeId}` | Creates a weather alert notification for a home. | Admin |
| `POST` | `/api/v1/notification/smart-alert-intro/{userId}` | Sends the smart alert introduction notification to a user. | Admin |

### 👤 User Utility Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/user/email/{email}` | Finds a user by email. | Admin |
| `GET` | `/api/v1/user/username/{username}` | Finds a user by username. | Admin |
| `PUT` | `/api/v1/user/toggle-smart-alerts` | Enables or disables smart alerts for the current authenticated user. | Authenticated |

### 💳 User Subscription Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/user-subscription/status/{status}` | Returns user subscriptions filtered by status. | Admin |
| `DELETE` | `/api/v1/user-subscription/delete/{id}` | Deletes a user subscription. | Admin |


## 🔌 External APIs & Integrations

DAR uses external services to support smart features, automation, notifications, and payments.

| Service | Purpose |
|---|---|
| OpenAI API | Chatbot answers and AI maintenance advice |
| Weather API | Weather-based maintenance alerts |
| Twilio WhatsApp & Voice | WhatsApp reminders and urgent calls |
| Gmail SMTP | Email reminders and notifications |
| AWS Elastic Beanstalk | Backend deployment |
| AWS RDS MySQL | Production database |


## 🤖 AI Features

DAR includes AI features to make home maintenance smarter and more proactive:

- AI chatbot to answer user questions about the platform.
- AI weather-based maintenance advice for each home.
- Daily smart AI reminders for users with an active paid subscription.
