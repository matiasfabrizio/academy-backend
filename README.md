# Academia Matriz - Backend API


This repository contains the backend application for **Matriz**. It is a RESTful API built with **Spring Boot** that manages course information, student enrollments, schedules, financial details, and an automated notification system.

## 🚀 Technologies Used

* **Main Framework:** Java / Spring Boot
* **Database:** PostgreSQL (utilizing `jsonb` for dynamic lists)
* **Cloud Storage:** S3-Compatible Storage (Cloudflare R2) via AWS SDK
* **Image Processing:** Thumbnailator
* **Integrations:** n8n (Automation agents for WhatsApp & Telegram notifications)

## 🏗️ Architecture & Core Entities


The database model is designed for high performance and clear relationship mapping, avoiding complex inheritance structures in favor of optimized queries.

### 📚 Course Management (Single Table Strategy)
To optimize database performance and prevent unnecessary `JOIN` operations, the application uses a single `Course` entity.
* **`CourseType` Enum:** Differentiates the type of course (e.g., *Refuerzo*, informational courses) entirely within the application logic.
* Courses are linked to a specific `Professor` and can have multiple `Schedule` blocks.
* Dynamic text content is stored efficiently using PostgreSQL's native `jsonb` format. This is used to display the course syllabus.

### 📅 Schedules & Enrollments
* **`Schedule`:** Courses are broken down into specific schedules (defined by day, start/end time, capacity, and mode).
* **`Enrollment`:** Students (`AppUser`) do not enroll directly into a generic course. Instead, enrollments are strictly tied to a specific `Schedule`. This allows for precise capacity tracking and status management (Pending, etc.).

### 🖼️ Automated Image Processing
The backend features a dedicated `ImageProcessingService` that automatically formats and uploads images to an S3-compatible bucket (Cloudflare R2) upon creation or update:
* **Professors:** Automatically cropped and resized to a centered 800x800 square.
* **Courses/General:** Automatically scaled to fit within a 1280x720 resolution while preserving the aspect ratio.

### 💳 Financials
The API manages payment targets through a `Holder` entity. A `Holder` represents the financial entity or person receiving payments for specific `Course`s and holds a set of `BankAccount`s (tracking bank names, account numbers, and CCIs).

## ⚙️ Local Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/matriz-backend.git](https://github.com/your-username/matriz-backend.git)
    cd matriz-backend
    ```

2.  **Configure environment variables:**
    Create an `application-dev.properties` (or `.env`) file in `src/main/resources/` and define your database connection, n8n webhooks, and cloud storage credentials:
    ```properties
    # Database
    spring.datasource.url=jdbc:postgresql://localhost:5432/matriz_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password

    # Cloudflare R2 / AWS S3
    cloud.r2.bucket-name=your-bucket-name
    cloud.r2.public-url=[https://your-public-r2-url.com](https://your-public-r2-url.com)
    # Make sure to configure your AWS credentials securely via environment variables or profile

    # n8n Webhooks for WhatsApp/Telegram
    n8n.webhook.whatsapp.url=https://your-n8n-instance/webhook/whatsapp
    n8n.webhook.telegram.url=https://your-n8n-instance/webhook/telegram
    ```

3.  **Run the application:**
    Start the Spring Boot server using the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📂 Project Structure

* `/modules`: Contains the core domain boundaries (`courses`, `enrollments`, `finance`, `images`, `professor`, `schedules`).
* `/security`: Manages `AppUser` and authentication/authorization.
* `/shared`: Enums (`CourseType`, `Mode`, `BankName`, `Tag`) and shared interfaces (`PhotoOwner`) used across multiple modules.
* `/config`: Configuration files.

---
*Developed by Matias Prado.*