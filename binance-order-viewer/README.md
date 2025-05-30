# Binance Order Viewer

A client-server application that fetches order book data from Binance API and displays it in real-time using WebSockets.

## Project Structure

- `server`: Spring Boot server application
- `client`: Angular client application

## Prerequisites

- Java 17
- Maven
- Node.js and npm

## Running the Server

1. Navigate to the server directory:
   ```
   cd server
   ```

2. Build the application:
   ```
   mvn17 clean install
   ```

3. Run the application:
   ```
   mvn17 spring-boot:run
   ```

The server will start on port 8080 and begin fetching order book data from Binance every 5 seconds.

## Running the Client

1. Navigate to the client directory:
   ```
   cd client
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   npm start
   ```

The client will start on port 4200. Open your browser and navigate to `http://localhost:4200` to view the application.

## Features

- Real-time order book data from Binance
- WebSocket communication between server and client
- Responsive UI that works on desktop and mobile

## API Endpoint

The server fetches data from the following Binance API endpoint:
```
https://api.binance.com/api/v3/depth?symbol=BTCUSDT&limit=5
```

## WebSocket Communication

The server broadcasts order book updates to the following WebSocket topic:
```
/topic/orderbook
```

Clients can subscribe to this topic to receive real-time updates.
