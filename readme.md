# Stock Management Application

A full-stack application for managing stock portfolios and tracking dividends, built with Spring Boot and Angular.

## Prerequisites

- Java 21
- Node.js 18+
- Docker
- Maven 3.9+

## Quick Start

### Backend

1. Start the PostgreSQL database:

- cd backend/stock-management
- docker-compose up -d

2. Run Spring Boot application:

- mvnw spring-boot:run

The backend API will be available at `http://localhost:8080`

### Frontend

1. Install dependencies:

- npm install

2. Start development server:

- npm start

The frontend will be available at `http://localhost:4200`

## Key Features

- Track stocks in portfolio
- Record and monitor dividend payments
- View portfolio performance metrics
- RESTful API backend
- Responsive Angular frontend
