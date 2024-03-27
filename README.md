## Financial Tracker Web-Application (Backend Side)
Client side: [https://github.com/andreichernetskii/FinManagerBackEnd](https://github.com/andreichernetskii/FinMangerFrontEnd)

### The Genesis
This project emerged as a personal challenge for me to create a web application for tracking my financial transactions while living in a foreign country. The application serves as a practical test of my Java skills and demonstrates my ability to learn new concepts quickly and efficiently.

### Features
- **Adding New Transactions**: Incomes and expenses can be added with details such as amount, date, and category.
- **Category Management**: Automatic addition of categories for expenses or incomes to the database when adding a new transaction.
- **Sorting Transactions**: Existing transactions can be sorted by year, month, and type (income or expense), as well as by category.
- **Setting Limits**: Users can set limits based on the timeline or both timeline and category (e.g., daily limit or daily limit for groceries).
- **Transaction Management**: Users can edit or delete transactions and limits as needed.
- **Limit Notifications**: Receive notifications in the bottom right corner when a limit expires.
- **User Authentication**: The application provides user registration, login, and logout functionality using JSON Web Token (JWT) on the backend.e.

### How it works
Now the project works with Docker Compose. To run the project, simply grab the docker-compose.yml file and execute `docker-compose up`.
