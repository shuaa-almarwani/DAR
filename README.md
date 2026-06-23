<div align="center">

# 🏠 DAR – Smart Home Care System

### دار – نظام ذكي للعناية بالمنزل

<table>
  <tr>
    <td align="center" bgcolor="#E8DED2">
      <br>
      <b>Smart home-care platform that helps users organize homes, manage maintenance tasks, track invoices and bills, receive intelligent reminders, and get AI-powered home-care insights.</b>
      <br><br>
      <b>منصة ذكية تساعد المستخدمين على تنظيم منازلهم، متابعة أعمال الصيانة، حفظ الفواتير والضمانات، استقبال التذكيرات الذكية، والحصول على توصيات مدعومة بالذكاء الاصطناعي للعناية بالمنزل.</b>
      <br><br>
    </td>
  </tr>
</table>

<br>

<img src="https://img.shields.io/badge/Java-17-765345?style=for-the-badge&logo=java&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Boot-4.x-A68972?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/MySQL-Database-765345?style=for-the-badge&logo=mysql&logoColor=white" />
<img src="https://img.shields.io/badge/AWS-Deployed-A68972?style=for-the-badge&logo=amazonaws&logoColor=white" />
<img src="https://img.shields.io/badge/OpenAI-AI-765345?style=for-the-badge&logo=openai&logoColor=white" />
<img src="https://img.shields.io/badge/Twilio-Notifications-A68972?style=for-the-badge&logo=twilio&logoColor=white" />
<img src="https://img.shields.io/badge/n8n-Automation-765345?style=for-the-badge&logo=n8n&logoColor=white" />
<img src="https://img.shields.io/badge/Overpass-Location%20API-A68972?style=for-the-badge&logo=openstreetmap&logoColor=white" />
<img src="https://img.shields.io/badge/Cloudinary-Cloud%20API-765345?style=for-the-badge&logo=cloudinary&logoColor=white" />
<img src="https://img.shields.io/badge/Nominatim-Geocoding-A68972?style=for-the-badge&logo=openstreetmap&logoColor=white" />
<img src="https://img.shields.io/badge/WeatherAPI-Weather-765345?style=for-the-badge&logo=weatherapi&logoColor=white" />

</div>

---

## 🌿 Project Overview | نظرة عامة على المشروع

<table>
  <tr>
    <td bgcolor="#E8DED2">
      <b>DAR</b> is a smart home-care system designed to help users manage and organize home maintenance in one place.
      <br><br>
      The platform supports homes, home items, maintenance tasks, reminders, bills, invoices, sensors, notifications, subscriptions, payments, and AI-powered smart recommendations.
      <br><br>
      <b>دار</b> هو نظام ذكي للعناية بالمنزل يساعد المستخدم على تنظيم منزله، متابعة الصيانة، إدارة الفواتير والضمانات، واستقبال التذكيرات الذكية في مكان واحد.
    </td>
  </tr>
</table>

---

## 🎯 Project Goal

DAR aims to solve common home-care problems such as forgotten maintenance tasks, missing invoices and warranties, difficulty tracking bills, and lack of proactive smart reminders.

The system helps users:

* Organize home devices and items.
* Track maintenance history.
* Receive reminders through email, WhatsApp, and urgent calls.
* Monitor bills and invoices.
* Use AI-powered recommendations.
* Get smart alerts based on weather and sensor behavior.

---

## 🧭 Use Case Diagram

![DAR ER Diagram](./docs/use_case.png)

---

## 🗂️ ER Diagram

<table>
  <tr>
    <td bgcolor="#E8DED2">
      The ER Diagram represents the main database entities and relationships in the DAR platform, including users, homes, home items, maintenance records, reminders, bills, sensors, subscriptions, payments, and notifications.
    </td>
  </tr>
</table>

<br>

![DAR ER Diagram](./docs/er-diagram.png)

---

## 🔐 Authentication & Security

<table>
  <tr>
    <td bgcolor="#E8DED2">
      The system uses <b>JWT authentication</b> to protect user data and secure private endpoints.
    </td>
  </tr>
</table>

```http
Authorization: Bearer <token>
```

### Security Features

<table>
  <tr>
    <td>✅ JWT-Based Authentication</td>
    <td>✅ Stateless Session Management</td>
  </tr>
  <tr>
    <td>✅ Role-Based Access Control</td>
    <td>✅ Ownership Validation</td>
  </tr>
  <tr>
    <td>✅ Protected APIs</td>
    <td>✅ Secure User Resources</td>
  </tr>
</table>

---

## 🌐 Deployment

<table>
  <tr>
    <td bgcolor="#E8DED2">
      The backend is deployed on <b>AWS Elastic Beanstalk</b> with a production MySQL database hosted on <b>AWS RDS</b>.
    </td>
  </tr>
</table>

### Base URL

```http
http://Dar-env.eba-yke92rm3.eu-central-1.elasticbeanstalk.com
```

---

## 📬 Postman Collection

The project APIs are documented and tested using Postman.

The collection includes:

* Authentication
* User management
* Homes
* Home items
* Maintenance
* Maintenance reminders
* Notifications
* Bills
* Purchase invoices
* Sensors
* Subscriptions
* Payments
* Chatbot

---

## 🧰 Tech Stack

<table>
  <tr bgcolor="#765345">
    <th><font color="white">Category</font></th>
    <th><font color="white">Technologies</font></th>
  </tr>
  <tr>
    <td><b>Backend</b></td>
    <td>Java 17, Spring Boot</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Security</b></td>
    <td>Spring Security, JWT Authentication</td>
  </tr>
  <tr>
    <td><b>Database</b></td>
    <td>MySQL, Spring Data JPA</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Deployment</b></td>
    <td>AWS Elastic Beanstalk, AWS RDS</td>
  </tr>
  <tr>
    <td><b>AI & Automation</b></td>
    <td>OpenAI API, Weather API, n8n Webhooks</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Notifications</b></td>
    <td>Twilio API, Java Mail Sender</td>
  </tr>
  <tr>
    <td><b>Testing & Documentation</b></td>
    <td>Postman</td>
  </tr>
</table>

---

## 👩‍💻 My Implemented Endpoints

This section highlights the API endpoints I worked on as part of my contribution to the DAR platform.

My work focused mainly on **chatbot support, maintenance tracking, maintenance reminders, notifications, smart alerts, and subscription-related features**.

### 🤖 Chatbot Endpoints

| Method | Endpoint                    | Description                                                  | Access                 |
| ------ | --------------------------- | ------------------------------------------------------------ | ---------------------- |
| `GET`  | `/api/v1/chatbot/questions` | Returns suggested chatbot questions for users.               | Public                 |
| `POST` | `/api/v1/chatbot/ask`       | Sends a user question to the chatbot and returns the answer. | Public / Authenticated |

### 🛠️ Maintenance Endpoints

| Method | Endpoint                                        | Description                                               | Access        |
| ------ | ----------------------------------------------- | --------------------------------------------------------- | ------------- |
| `GET`  | `/api/v1/maintenance/upcoming/{homeId}`         | Returns upcoming maintenance records for a specific home. | Owner / Admin |
| `GET`  | `/api/v1/maintenance/overdue/{homeId}`          | Returns overdue maintenance records for a specific home.  | Owner / Admin |
| `PUT`  | `/api/v1/maintenance/mark-done/{maintenanceId}` | Marks a maintenance record as completed.                  | Owner / Admin |

### ⏰ Maintenance Reminder Endpoints

| Method | Endpoint                                                         | Description                                             | Access        |
| ------ | ---------------------------------------------------------------- | ------------------------------------------------------- | ------------- |
| `POST` | `/api/v1/maintenance-reminder/add/{maintenanceId}`               | Adds a maintenance reminder for a maintenance record.   | Owner / Admin |
| `PUT`  | `/api/v1/maintenance-reminder/update/{id}/{homeId}/{homeItemId}` | Updates a maintenance reminder and validates ownership. | Owner / Admin |
| `PUT`  | `/api/v1/maintenance-reminder/mark-sent/{id}`                    | Marks a maintenance reminder as sent.                   | Owner / Admin |
| `GET`  | `/api/v1/maintenance-reminder/upcoming/{homeId}`                 | Returns upcoming reminders for a home.                  | Owner / Admin |
| `GET`  | `/api/v1/maintenance-reminder/today/{homeId}`                    | Returns today’s reminders for a home.                   | Owner / Admin |
| `POST` | `/api/v1/maintenance-reminder/send/{reminderId}`                 | Sends a maintenance reminder manually.                  | Owner / Admin |
| `PUT`  | `/api/v1/maintenance-reminder/reactivate/{reminderId}`           | Reactivates a reminder after it has been sent.          | Owner / Admin |
| `GET`  | `/api/v1/maintenance-reminder/summary/{homeId}`                  | Returns a summary of reminders for a home.              | Owner / Admin |
| `GET`  | `/api/v1/maintenance-reminder/ai-weather-advice/{homeId}`        | Generates AI weather-based maintenance advice.          | Owner / Admin |

### 🔔 Notification Endpoints

| Method   | Endpoint                                             | Description                                        | Access        |
| -------- | ---------------------------------------------------- | -------------------------------------------------- | ------------- |
| `PUT`    | `/api/v1/notification/mark-as-read/{notificationId}` | Marks one notification as read.                    | Owner / Admin |
| `PUT`    | `/api/v1/notification/mark-all-as-read`              | Marks all user notifications as read.              | Authenticated |
| `DELETE` | `/api/v1/notification/delete/{notificationId}`       | Deletes a notification.                            | Owner / Admin |
| `GET`    | `/api/v1/notification/summary`                       | Returns notification summary for the current user. | Authenticated |
| `POST`   | `/api/v1/notification/weather-alert/{homeId}`        | Creates a weather alert notification for a home.   | Admin         |
| `POST`   | `/api/v1/notification/smart-alert-intro/{userId}`    | Sends the smart alert introduction notification.   | Admin         |

### 👤 User Utility Endpoints

| Method | Endpoint                           | Description                                            | Access        |
| ------ | ---------------------------------- | ------------------------------------------------------ | ------------- |
| `GET`  | `/api/v1/user/email/{email}`       | Finds a user by email.                                 | Admin         |
| `GET`  | `/api/v1/user/username/{username}` | Finds a user by username.                              | Admin         |
| `PUT`  | `/api/v1/user/toggle-smart-alerts` | Enables or disables smart alerts for the current user. | Authenticated |

### 💳 User Subscription Endpoints

| Method   | Endpoint                                    | Description                                    | Access |
| -------- | ------------------------------------------- | ---------------------------------------------- | ------ |
| `GET`    | `/api/v1/user-subscription/status/{status}` | Returns user subscriptions filtered by status. | Admin  |
| `DELETE` | `/api/v1/user-subscription/delete/{id}`     | Deletes a user subscription.                   | Admin  |

---

## 🔌 External APIs & Integrations

DAR uses external services to support smart features, automation, notifications, and payments.

<table>
  <tr bgcolor="#765345">
    <th><font color="white">Service</font></th>
    <th><font color="white">Purpose</font></th>
  </tr>
  <tr>
    <td><b>OpenAI API</b></td>
    <td>Chatbot answers and AI maintenance advice</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Weather API</b></td>
    <td>Weather-based maintenance alerts</td>
  </tr>
  <tr>
    <td><b>n8n Webhooks</b></td>
    <td>Sensor connection and sensor analysis automation</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Twilio WhatsApp & Voice</b></td>
    <td>WhatsApp reminders and urgent calls</td>
  </tr>
  <tr>
    <td><b>Gmail SMTP</b></td>
    <td>Email reminders and notifications</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>Lemon Squeezy</b></td>
    <td>Subscription checkout and payment links</td>
  </tr>
  <tr>
    <td><b>AWS Elastic Beanstalk</b></td>
    <td>Backend deployment</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td><b>AWS RDS MySQL</b></td>
    <td>Production database</td>
  </tr>
</table>

<br>

<table>
  <tr>
    <td bgcolor="#F7F2EC">
      API keys and secrets are stored in environment variables, not inside the source code.
    </td>
  </tr>
</table>

---

## 🤖 AI Features

<table>
  <tr>
    <td bgcolor="#E8DED2">
      DAR includes AI features to make home maintenance smarter and more proactive.
    </td>
  </tr>
</table>

<br>

<table>
  <tr>
    <td>🤖 AI chatbot to answer user questions about the platform.</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td>🌦️ AI weather-based maintenance advice for each home.</td>
  </tr>
  <tr>
    <td>🔔 Daily smart AI reminders for users with an active paid subscription.</td>
  </tr>
  <tr bgcolor="#E8DED2">
    <td>📊 AI-assisted sensor analysis through n8n webhooks.</td>
  </tr>
</table>



---

<div align="center">

<table>
  <tr>
    <td align="center" bgcolor="#E8DED2">
      <br>
      <b>DAR makes home care easier, smarter, and more organized.</b>
      <br><br>
    </td>
  </tr>
</table>

</div>
