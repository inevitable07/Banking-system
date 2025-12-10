# Simple Banking System

A secure, feature-rich CLI-based banking application built with Java and Maven. This system includes advanced security features like password authentication, PIN protection, audit logging, and an admin control panel.

##  Features

### Security Features
- **Password Authentication**: SHA-256 hashed passwords for secure login
- **PIN Protection**: 4-digit PIN required for withdrawals
- **Account Locking**: Admin can lock/unlock accounts
- **Audit Logging**: Complete activity logs for compliance

###  Customer Features
- Create new accounts with auto-generated or custom account numbers
- Deposit money (with validation)
- Withdraw money (PIN-protected)
- Check account balance
- View complete transaction history
- Secure login/logout

###  Admin Features
- View all accounts with status
- Search accounts by number
- Calculate total bank balance
- View any account's transaction history
- Lock/unlock accounts
- View comprehensive audit logs
- Bulk migrate old accounts

###  Data Management
- JSON-based data persistence using Gson
- Auto-save on all operations
- Auto-load on startup
- Transaction history with timestamps

##  Technology Stack

- **Language**: Java 11+
- **Build Tool**: Maven
- **Data Storage**: JSON (Gson library)
- **Security**: SHA-256 password hashing
- **Architecture**: Clean OOP with separation of concerns

##  Project Structure

```
simple-banking-system/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── bankapp/
│                   ├── Main.java                    # Application entry point
│                   ├── Bank.java                    # Core banking operations
│                   ├── Account.java                 # Account entity
│                   ├── Transaction.java             # Transaction entity
│                   ├── AuthService.java             # Authentication service
│                   ├── AuditService.java            # Audit logging service
│                   ├── AdminService.java            # Admin panel service
│                   ├── FileStorage.java             # JSON persistence
│                   ├── InputUtil.java               # Safe input handling
│                   ├── AccountMigrationHelper.java  # Account migration
│                   └── LocalDateTimeAdapter.java    # Gson date adapter
├── data/
│   └── bank_data.json                              # Account data (auto-generated)
├── logs/
│   └── audit.log                                   # Audit logs (auto-generated)
├── pom.xml                                         # Maven configuration
├── .gitignore
└── README.md
```

##  Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git (for cloning)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/simple-banking-system.git
   cd simple-banking-system
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Run the application**
   ```bash
   java -jar target/banking-system-executable.jar
   ```

   Or run directly with Maven:
   ```bash
   mvn exec:java -Dexec.mainClass="com.bankapp.Main"
   ```

### Using IntelliJ IDEA

1. Open IntelliJ IDEA
2. Select `File` → `Open` → Choose project folder
3. Wait for Maven to download dependencies
4. Right-click `Main.java` → `Run 'Main.main()'`

##  Usage Guide

### Default Admin Credentials
- **Username**: ADMIN (hardcoded)
- **Password**: `admin123`

### Creating a New Account
1. Choose option 2 from main menu
2. Enter customer name
3. Create a secure password
4. Create a 4-digit PIN
5. Choose auto-generated or custom account number

### Customer Login
1. Enter account number
2. Enter password
3. Access banking operations menu

### Performing Transactions
- **Deposit**: Enter amount (must be > 0)
- **Withdraw**: Enter amount + 4-digit PIN
- **Check Balance**: View current balance
- **Transaction History**: View all deposits and withdrawals

### Admin Operations
1. Login with admin credentials
2. Access admin control panel
3. Perform administrative tasks
4. View audit logs for compliance

##  Security Features

### Password Security
- Passwords are hashed using SHA-256 before storage
- Plain text passwords are never stored
- Failed login attempts are logged

### PIN Protection
- 4-digit PIN required for withdrawals
- Invalid PIN attempts are logged in audit
- PIN validation ensures only numeric input

### Audit Logging
All critical operations are logged:
- Login success/failure
- Wrong password attempts
- Wrong PIN attempts
- Withdrawals (success/failure)
- Deposits
- Account lock/unlock
- Admin actions

Log format:
```
[2025-12-10 14:30:45] ACTION=LOGIN account=1234567890 status=SUCCESS
[2025-12-10 14:31:12] ACTION=WITHDRAW account=1234567890 status=SUCCESS details=Amount=$100.00
```

##  Account Migration

For existing accounts without password/PIN:

### Automatic Migration (On Login)
- System detects old accounts
- Prompts user to set password and PIN
- Saves updated credentials

### Manual Migration
- Use option 3 from main menu
- Enter account number
- Set new password and PIN

### Bulk Migration (Admin)
- Login as admin
- Select bulk migration option
- Sets default credentials for all old accounts
- Default: password123 / PIN: 1234

##  Data Files

### bank_data.json
Stores all account information:
```json
{
  "1234567890": {
    "accountNumber": "1234567890",
    "customerName": "John Doe",
    "balance": 1000.0,
    "passwordHash": "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=",
    "pin": "1234",
    "isLocked": false,
    "transactions": [...]
  }
}
```

### audit.log
Stores all system activities:
```
[2025-12-10 14:30:45] ACTION=LOGIN account=1234567890 status=SUCCESS
[2025-12-10 14:31:12] ACTION=WITHDRAW account=1234567890 status=SUCCESS details=Amount=$100.00
```

##  Validations

- ✅ Account number uniqueness
- ✅ Non-empty customer names
- ✅ Password required for new accounts
- ✅ PIN must be exactly 4 digits
- ✅ Deposit amount must be > 0
- ✅ Withdraw amount must be > 0
- ✅ Withdraw amount must be ≤ balance
- ✅ PIN verification for withdrawals
- ✅ Locked accounts cannot login

##  Testing

### Manual Testing Scenarios

1. **Create Account**
   - Test auto-generated account numbers
   - Test custom account numbers
   - Test duplicate account number rejection

2. **Authentication**
   - Test successful login
   - Test wrong password
   - Test locked account access

3. **Transactions**
   - Test deposit with valid amount
   - Test deposit with invalid amount (0 or negative)
   - Test withdrawal with correct PIN
   - Test withdrawal with wrong PIN
   - Test withdrawal exceeding balance

4. **Admin Features**
   - Test account locking/unlocking
   - Test viewing all accounts
   - Test audit log viewing

##  Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

##  License

This project is open source and available under the [MIT License](LICENSE).

##  Author

Aashish Bhaskar
- GitHub: [@inevitable07](https://github.com/inevitable07)
- Email: your.email@example.com

## 🙏 Acknowledgments

- Built as a learning project for Java and Maven
- Demonstrates clean OOP principles
- Implements industry-standard security practices

## 📞 Support

For support, email imaashishbhaskar@gmail.com or open an issue in the GitHub repository.

---

**⭐ If you find this project helpful, please give it a star!**
