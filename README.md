# Booking-System

Booking System API
Overview

The Booking System API is a RESTful service built with Spring Boot that allows users to manage bookings and handle user authentication with JWT and refresh tokens. It features user registration, login, JWT-based authentication, and secure endpoints for creating, viewing, updating, and deleting bookings.
Features

    User Registration: Users can register with a username and password.
    JWT Authentication: JWT tokens are used to authenticate API requests.
    Refresh Tokens: Allows users to obtain new access tokens without re-authentication.
    Booking Management: CRUD operations (Create, Read, Update, Delete) for managing bookings.
    Role-based Access Control: Secures endpoints based on user roles (e.g., "USER", "ADMIN").

Tech Stack

    Back End: Spring Boot, Spring Security, JPA (Hibernate)
    Database: MS SQL Server, Flyway (for database migrations)
    Security: JWT (JSON Web Tokens) for stateless authentication
    Build Tool: Gradle

Prerequisites

    Java 17 or later
    MS SQL Server installed locally or remotely
    Gradle installed
    Postman or any API testing tool (optional, for testing the API endpoints)
